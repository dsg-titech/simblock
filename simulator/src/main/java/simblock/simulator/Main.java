/*
 * Copyright 2019 Distributed Systems Group
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simblock.simulator;


import static simblock.settings.SimulationConfiguration.*;
import static simblock.simulator.Network.getDegreeDistribution;
import static simblock.simulator.Network.getRegionDistribution;
import static simblock.simulator.Simulator.addNode;
import static simblock.simulator.Simulator.getSimulatedNodes;
import static simblock.simulator.Simulator.setTargetInterval;
import static simblock.simulator.Timer.getCurrentTime;
import static simblock.simulator.Timer.getTask;
import static simblock.simulator.Timer.runTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import simblock.block.Block;
import simblock.node.HonestNode;
import simblock.node.Node;
import simblock.node.SelfishNode;
import simblock.task.AbstractMintingTask;
import simblock.task.Task;


/**
 * The type Main represents the entry point.
 */
public class Main {
  /**
   * The constant to be used as the simulation seed.
   */
  public static Random random = new Random(10);

  /**
   * The initial simulation time.
   */
  public static long simulationTime = 0;

  public static SelfishNode selfishNode;

  /**
   * The entry point.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    final long start = System.currentTimeMillis();
    setTargetInterval(INTERVAL);

    // Setup network
    constructNetworkWithAllNodes(NUM_OF_NODES);

    startSimulation();
    selfishNode.printSelfishMiningStatus();

    //TODO logger
    System.out.println();

    Set<Block> blocks = new HashSet<>();

    // Get the latest block from the first simulated node
    Block block = getSimulatedNodes().get(0).getBlock();

    //Update the list of known blocks by adding the parents of the aforementioned block
    while (block.getParent() != null) {
      blocks.add(block);
      block = block.getParent();
    }

    Set<Block> orphans = new HashSet<>();
    int averageOrphansSize = 0;
    // Gather all known orphans
    for (Node node : getSimulatedNodes()) {
      orphans.addAll(node.getOrphans());
      averageOrphansSize += node.getOrphans().size();
    }
    averageOrphansSize = averageOrphansSize / getSimulatedNodes().size();

    // Record orphans to the list of all known blocks
    blocks.addAll(orphans);

    ArrayList<Block> blockList = new ArrayList<>(blocks);

    //Sort the blocks first by time, then by hash code
    blockList.sort((a, b) -> {
      int order = Long.signum(a.getTime() - b.getTime());
      if (order != 0) {
        return order;
      }
      order = System.identityHashCode(a) - System.identityHashCode(b);
      return order;
    });

    //Log all orphans
    // TODO move to method and use logger
//    for (Block orphan : orphans) {
//      System.out.println(orphan + ":" + orphan.getHeight());
//    }
//    System.out.println(averageOrphansSize);

    long end = System.currentTimeMillis();
    simulationTime += end - start;
  }


  //TODO　以下の初期生成はシナリオを読み込むようにする予定
  //ノードを参加させるタスクを作る(ノードの参加と，リンクの貼り始めるタスクは分ける)
  //シナリオファイルで上の参加タスクをTimer入れていく．

  // TRANSLATED FROM ABOVE STATEMENT
  // The following initial generation will load the scenario
  // Create a task to join the node (separate the task of joining the node and the task of
  // starting to paste the link)
  // Add the above participating tasks with a timer in the scenario file.

  /**
   * Populate the list using the distribution.
   *
   * @param distribution the distribution
   * @param facum        whether the distribution is cumulative distribution
   * @return array list
   */
  //TODO explanation on facum etc.
  public static ArrayList<Integer> makeRandomListFollowDistribution(double[] distribution, boolean facum) {
    ArrayList<Integer> list = new ArrayList<>();
    int index = 0;

    if (facum) {
      for (; index < distribution.length; index++) {
        while (list.size() <= NUM_OF_NODES * distribution[index]) {
          list.add(index);
        }
      }
      while (list.size() < NUM_OF_NODES) {
        list.add(index);
      }
    } else {
      double acumulative = 0.0;
      for (; index < distribution.length; index++) {
        acumulative += distribution[index];
        while (list.size() <= NUM_OF_NODES * acumulative) {
          list.add(index);
        }
      }
      while (list.size() < NUM_OF_NODES) {
        list.add(index);
      }
    }

    Collections.shuffle(list, random);
    return list;
  }

  /**
   * Populate the list using the rate.
   *
   * @param rate the rate of true
   * @return array list
   */
  public static ArrayList<Boolean> makeRandomList(float rate){
		ArrayList<Boolean> list = new ArrayList<Boolean>();
		for(int i=0; i < NUM_OF_NODES; i++){
			list.add(i < NUM_OF_NODES*rate);
		}
		Collections.shuffle(list, random);
		return list;
	}

  /**
   * Generates a random mining power expressed as Hash Rate, and is the number of mining (hash
   * calculation) executed per millisecond.
   *
   * @return the number of hash  calculations executed per millisecond.
   */
/*  public static int genMiningPower() {
    double r = random.nextGaussian();

    return Math.max((int) (r * STDEV_OF_MINING_POWER + AVERAGE_MINING_POWER), 1);
  }*/

  public static int getSelfishMiningPower(){
    return (int)(SELFISH_MINER_ALPHA * TOTAL_MINING_POWER);
  }

  public static int getHonestMiningPower(){
    float honestMiningPower = (1 - SELFISH_MINER_ALPHA) * TOTAL_MINING_POWER;

    return (int)(honestMiningPower / (NUM_OF_NODES - 1));
  }

  /**
   * Construct network with the provided number of nodes.
   *
   * @param numNodes the num nodes
   */
  public static void constructNetworkWithAllNodes(int numNodes) {

    // Random distribution of nodes per region
    double[] regionDistribution = getRegionDistribution();
    List<Integer> regionList = makeRandomListFollowDistribution(regionDistribution, false);

    // Random distribution of node degrees
    double[] degreeDistribution = getDegreeDistribution();
    List<Integer> degreeList = makeRandomListFollowDistribution(degreeDistribution, true);

    // List of nodes using compact block relay.
    List<Boolean> useCBRNodes = makeRandomList(CBR_USAGE_RATE);

    // List of churn nodes.
    List<Boolean> churnNodes = makeRandomList(CHURN_NODE_RATE);

    // Selfish Miner --> Node ID 1
    //Others Start From 2
    selfishNode = new SelfishNode(
            1, degreeList.get(0) + 1, regionList.get(0), getSelfishMiningPower(), TABLE,
            ALGO, useCBRNodes.get(0), churnNodes.get(0)
    );
    addNode(selfishNode);

    for (int id = 2; id <= numNodes; id++) {
      // Each node gets assigned a region, its degree, mining power, routing table and
      // consensus algorithm
      Node node = new HonestNode(
          id, degreeList.get(id - 1) + 1, regionList.get(id - 1), getHonestMiningPower(), TABLE,
          ALGO, useCBRNodes.get(id - 1), churnNodes.get(id - 1)
      );
      // Add the node to the list of simulated nodes
      addNode(node);
    }

    // Link newly generated nodes
    for (Node node : getSimulatedNodes()) {
      node.joinNetwork();
    }

    // Designates a random node (nodes in list are randomized) to mint the genesis block
    getSimulatedNodes().get(0).genesisBlock();
  }

  public static void startSimulation(){

    for(int i=0; i < Iteration_Number; i++){
      // Do message passing task!
      Task currentTask = getTask();
      while(currentTask != null){
        if (currentTask instanceof AbstractMintingTask) {
          AbstractMintingTask task = (AbstractMintingTask) getTask();
        }
        runTask();
        currentTask = getTask();
      }

      float generatedRandom = (float)Math.random();
      if(generatedRandom < SELFISH_MINER_ALPHA){
        selfishNode.selfishMinting();
      }
      else{
        getSimulatedNodes().get(1).minting();
      }
    }
  }

}
