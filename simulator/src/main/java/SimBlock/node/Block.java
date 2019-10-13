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

import static SimBlock.simulator.Simulator.*;
import static SimBlock.settings.SimulationConfiguration.*;
import java.util.Map;
import java.util.HashMap;

public class Block {
	private int height;
	private Block parent;
	private Node creator;
	private long time;
	private String proofOfWhat;
	private Map<Node, Coinage> coinages;
	private long difficulty;
	private long totalDifficulty;
	private Map<String, Long> nextDifficulties;
	private int id;
	private static int latestId = 0;

	public Block(Block parent, Node creator, long time, String proofOfWhat, long difficulty, Map<String, Long> genesisNextDifficulties, Map<Node, Coinage>genesisCoinages){
		this.height = parent == null ? 0 : parent.getHeight() + 1;
		this.parent = parent;
		this.creator = creator;
		this.time = time;
		this.proofOfWhat = proofOfWhat;
		this.difficulty = difficulty;
		this.totalDifficulty = difficulty + (parent == null ? 0 : parent.getTotalDifficulty());
		if (parent == null) {
			this.nextDifficulties = genesisNextDifficulties;

			this.coinages = genesisCoinages;
		} else {
			// TODO: difficulty adjustment
			this.nextDifficulties = new HashMap<String, Long>();
			this.nextDifficulties.put("work", parent.getNextDifficulty("work"));
			this.nextDifficulties.put("stake", parent.getNextDifficulty("stake"));
			
			this.coinages = new HashMap<Node, Coinage>();
			for (Node node : getSimulatedNodes()) {
				this.coinages.put(node, parent.getCoinage(node).clone());
				this.coinages.get(node).increaseAge();
			}
			if (proofOfWhat == "stake") {
				this.coinages.get(creator).resetAge();
				this.coinages.get(creator).reward(STAKING_REWARD);
			}
		}
		this.id = latestId;
		latestId++;
	}

	public int getHeight(){return this.height;}
	public Block getParent(){return this.parent;}
	public Node getCreator(){return this.creator;}
	public long getTime(){return this.time;}
	public String getProofOfWhat(){return proofOfWhat;}
	public Coinage getCoinage(Node node){return this.coinages.get(node);}
	public long getDifficulty() {return this.difficulty;}
	public long getTotalDifficulty() {return this.totalDifficulty;}
	public long getNextDifficulty(String proofOfWhat) {return this.nextDifficulties.get(proofOfWhat);}
	public int getId() {return this.id;}


	// return ancestor block that height is {height}
	public Block getBlockWithHeight(int height){
		if(this.height == height){
			return this;
		}else{
			return this.parent.getBlockWithHeight(height);
		}
	}

}
