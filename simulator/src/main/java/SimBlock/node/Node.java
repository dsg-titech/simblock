/**
 * Copyright 2019 Distributed Systems Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package SimBlock.node;

import static SimBlock.settings.SimulationConfiguration.*;
import static SimBlock.simulator.Main.*;
import static SimBlock.simulator.Network.*;
import static SimBlock.simulator.Simulator.*;
import static SimBlock.simulator.Timer.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import SimBlock.block.Block;
import SimBlock.node.consensusAlgo.AbstractConsensusAlgo;
import SimBlock.node.routingTable.AbstractRoutingTable;
import SimBlock.task.AbstractMessageTask;
import SimBlock.task.BlockMessageTask;
import SimBlock.task.InvMessageTask;
import SimBlock.task.RecMessageTask;
import SimBlock.task.AbstractMintingTask;

public class Node {
	private int nodeID;
	private int region;
	private long miningPower;
	private AbstractRoutingTable routingTable;
	private AbstractConsensusAlgo consensusAlgo;
	private Block block;
	private Set<Block> orphans = new HashSet<Block>();
	private AbstractMintingTask mintingTask = null;
	private boolean sendingBlock = false;
	private ArrayList<RecMessageTask> messageQue = new ArrayList<RecMessageTask>();
	private Set<Block> downloadingBlocks = new HashSet<Block>();

	private long processingTime = 2;

	public Node(int nodeID,int nConnection ,int region, long miningPower, String routingTableName, String consensusAlgoName){
		this.nodeID = nodeID;
		this.region = region;
		this.miningPower = miningPower;
		try {
			this.routingTable = (AbstractRoutingTable) Class.forName(routingTableName).getConstructor(Node.class).newInstance(this);
			this.consensusAlgo = (AbstractConsensusAlgo) Class.forName(consensusAlgoName).getConstructor(Node.class).newInstance(this);
			this.setnConnection(nConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNodeID(){ return this.nodeID; }
	public int getRegion(){ return this.region; }
	public long getMiningPower(){ return this.miningPower; }
	public AbstractConsensusAlgo getConsensusAlgo() { return this.consensusAlgo; }
	public AbstractRoutingTable getRoutingTable(){ return this.routingTable; }
	public Block getBlock(){ return this.block; }
	public Set<Block> getOrphans(){ return this.orphans; }

	public int getnConnection(){ return this.routingTable.getnConnection(); }
	public void setnConnection(int nConnection){ this.routingTable.setnConnection(nConnection); }
	public ArrayList<Node> getNeighbors(){ return this.routingTable.getNeighbors(); }
	public boolean addNeighbor(Node node){ return this.routingTable.addNeighbor(node); }
	public boolean removeNeighbor(Node node){ return this.routingTable.removeNeighbor(node); }

	public void joinNetwork(){
		this.routingTable.initTable();
	}
	
	public void genesisBlock(){
		Block genesis = this.consensusAlgo.genesisBlock();
		this.receiveBlock(genesis);
	}

	public void addToChain(Block newBlock) {
		if(this.mintingTask != null){
			removeTask(this.mintingTask);
			this.mintingTask = null;
		}
		this.block = newBlock;
		printAddBlock(newBlock);
		arriveBlock(newBlock, this);
	}

	private void printAddBlock(Block newBlock){
		OUT_JSON_FILE.print("{");
		OUT_JSON_FILE.print(	"\"kind\":\"add-block\",");
		OUT_JSON_FILE.print(	"\"content\":{");
		OUT_JSON_FILE.print(		"\"timestamp\":" + getCurrentTime() + ",");
		OUT_JSON_FILE.print(		"\"node-id\":" + this.getNodeID() + ",");
		OUT_JSON_FILE.print(		"\"block-id\":" + newBlock.getId());
		OUT_JSON_FILE.print(	"}");
		OUT_JSON_FILE.print("},");
		OUT_JSON_FILE.flush();
	}

	public void addOrphans(Block newBlock, Block validBlock){
		if(newBlock != validBlock){
			this.orphans.add(newBlock);
			this.orphans.remove(validBlock);
			if(newBlock.getParent() != null && validBlock.getParent() != null){
				this.addOrphans(newBlock.getParent(),validBlock.getParent());
			}
		}
	}

	public void minting(){
		AbstractMintingTask task = this.consensusAlgo.minting();
		this.mintingTask = task;
		if (task != null) putTask(task);
	}

	public void sendInv(Block block){
		for(Node to : this.routingTable.getNeighbors()){
			AbstractMessageTask task = new InvMessageTask(this,to,block);
			putTask(task);
		}
	}

	public void receiveBlock(Block receivedBlock){
		Block sameHeightBlock;

		if(this.consensusAlgo.isReceivedBlockValid(receivedBlock, this.block)){
			if (this.block != null) {
				sameHeightBlock = receivedBlock.getBlockWithHeight(this.block.getHeight());
				if(this.block != sameHeightBlock){
					this.addOrphans(this.block, sameHeightBlock);
				}
			}
			this.addToChain(receivedBlock);
			this.minting();
			this.sendInv(receivedBlock);
		}else{
			sameHeightBlock = this.block.getBlockWithHeight(receivedBlock.getHeight());
			if(!this.orphans.contains(receivedBlock) && receivedBlock != sameHeightBlock){
				this.addOrphans(receivedBlock, sameHeightBlock);
				arriveBlock(receivedBlock, this);
			}
		}

	}

	public void receiveMessage(AbstractMessageTask message){
		Node from = message.getFrom();

		if(message instanceof InvMessageTask){
			Block block = ((InvMessageTask) message).getBlock();
			if(!this.downloadingBlocks.contains(block)){
				if(this.consensusAlgo.isReceivedBlockValid(block, this.block)){
					AbstractMessageTask task = new RecMessageTask(this,from,block);
					putTask(task);
					downloadingBlocks.add(block);
				}else if(!this.orphans.contains(block) && block != this.block.getBlockWithHeight(block.getHeight())){
					// get new orphan block
					AbstractMessageTask task = new RecMessageTask(this,from,block);
					putTask(task);
					downloadingBlocks.add(block);
				}
			}
		}

		if(message instanceof RecMessageTask){
			this.messageQue.add((RecMessageTask) message);
			if(!sendingBlock){
				this.sendNextBlockMessage();
			}
		}

		if(message instanceof BlockMessageTask){
			Block block = ((BlockMessageTask) message).getBlock();
			downloadingBlocks.remove(block);
			this.receiveBlock(block);
		}
	}

	// send a block to the sender of the next queued recMessage
	public void sendNextBlockMessage(){
		if(this.messageQue.size() > 0){
			sendingBlock = true;

			Node to = this.messageQue.get(0).getFrom();
			Block block = this.messageQue.get(0).getBlock();
			this.messageQue.remove(0);
			long blockSize = BLOCKSIZE;
			long bandwidth = getBandwidth(this.getRegion(),to.getRegion());
			long delay = blockSize * 8 / (bandwidth/1000) + processingTime;
			BlockMessageTask messageTask = new BlockMessageTask(this, to, block, delay);

			putTask(messageTask);
		}else{
			sendingBlock = false;
		}
	}
}
