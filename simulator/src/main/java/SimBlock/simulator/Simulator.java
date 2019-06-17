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
package SimBlock.simulator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import SimBlock.node.Block;
import SimBlock.node.Node;
import static SimBlock.simulator.Timer.*;


public class Simulator {
	private static ArrayList<Node> SimulatedNodes = new ArrayList<Node>();
	private static long TargetInterval;// = 1000*60*10;//msec
	private static double difficulty;
	
	public static ArrayList<Node> getSimulatedNodes(){ return SimulatedNodes; }
	public static double getDifficulty(){ return difficulty; }
	public static void setTargetInterval(long interval){ TargetInterval = interval; }
	
	public static void addNode(Node node){
		SimulatedNodes.add(node);
		setDifficulty();
	}
	
	public static void removeNode(Node node){
		SimulatedNodes.remove(node);
		setDifficulty();
	}
	
	public static void addNodeWithConnection(Node node){
		node.joinNetwork();
		addNode(node);
		for(Node existingNode: SimulatedNodes){
			existingNode.addNeighbor(node);
		}
	}
	
	// calculate difficulty from totalPower
	private static void setDifficulty(){
		double totalPower = 0.0;
		
		for(Node node : SimulatedNodes){
			totalPower += node.getPower();
		}
		
		if(totalPower != 0.0){
			difficulty =  1.0 / (totalPower * TargetInterval);
		}
	}

	
	
	//
	// Record block propagation time
	// For saving memory, Record only the latest 10 Blocks
	//
	private static ArrayList<Block> observedBlocks = new ArrayList<Block>();
	private static ArrayList<LinkedHashMap<Integer, Long>> observedPropagations = new ArrayList<LinkedHashMap<Integer, Long>>();
	
	public static void arriveBlock(Block block,Node node){
		if(observedBlocks.contains(block)){
			LinkedHashMap<Integer, Long> Propagation = observedPropagations.get(observedBlocks.indexOf(block));
			Propagation.put(node.getNodeID(), getCurrentTime() - block.getTime());
		}else{
			if(observedBlocks.size() > 10){
				printPropagation(observedBlocks.get(0),observedPropagations.get(0));
				observedBlocks.remove(0);
				observedPropagations.remove(0);
			}
			LinkedHashMap<Integer, Long> propagation = new LinkedHashMap<Integer, Long>();
			propagation.put(node.getNodeID(), getCurrentTime() - block.getTime());
			observedBlocks.add(block);
			observedPropagations.add(propagation);
		}
	}
	
	public static void printPropagation(Block block,LinkedHashMap<Integer, Long> propagation){
		System.out.println(block + ":" + block.getHeight());
		for(Map.Entry<Integer, Long> timeEntry : propagation.entrySet()){
			System.out.println(timeEntry.getKey() + "," + timeEntry.getValue());
		}
		System.out.println();
	}
	
	public static void printAllPropagation(){
		for(int i=0;i < observedBlocks.size();i++){
			printPropagation(observedBlocks.get(i), observedPropagations.get(i));
		}
	}
	
}
