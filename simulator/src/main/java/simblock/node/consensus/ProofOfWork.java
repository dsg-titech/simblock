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

package simblock.node.consensus;

import static simblock.simulator.Main.random;

import java.math.BigInteger;
import simblock.block.Block;
import simblock.block.ProofOfWorkBlock;
import simblock.node.HonestNode;
import simblock.node.Node;
import simblock.task.AbstractMintingTask;
import simblock.task.MiningTask;
import simblock.task.SelfishMiningTask;

/**
 * The type Proof of work.
 */
@SuppressWarnings("unused")
public class ProofOfWork extends AbstractConsensusAlgo {
  private static final ProofOfWork instance = new ProofOfWork();
  private ProofOfWork(){

  }
  public static ProofOfWork getInstance(){
    return instance;
  }

  /**
   * Mints a new block by simulating Proof of Work.
   */
  @Override
  public AbstractMintingTask minting(Node selfNode) {
    ProofOfWorkBlock parent = (ProofOfWorkBlock) selfNode.getBlock();
    BigInteger difficulty = parent.getNextDifficulty();
    double p = 1.0 / difficulty.doubleValue();
    double u = random.nextDouble();
    if(selfNode instanceof HonestNode){
      return new MiningTask(selfNode, (long) (Math.log(u) / Math.log(
              1.0 - p) / selfNode.getMiningPower()), difficulty);
    }
    else{
      return new SelfishMiningTask(selfNode, (long) (Math.log(u) / Math.log(
              1.0 - p) / selfNode.getMiningPower()), difficulty);
    }
  }

  /**
   * Tests if the receivedBlock is valid with regards to the current block. The receivedBlock
   * is valid if it is an instance of a Proof of Work block and the received block needs to have
   * a bigger difficulty than its parent next difficulty and a bigger total difficulty compared to
   * the current block.
   *
   * @param receivedBlock the received block
   * @param currentBlock  the current block
   * @return true if block is valid false otherwise
   */
  @Override
  public boolean isReceivedBlockValid(Node selfNode, Block receivedBlock, Block currentBlock) {
/*    if (!(receivedBlock instanceof ProofOfWorkBlock)) {
      return false;
    }*/
/*    ProofOfWorkBlock recPoWBlock = (ProofOfWorkBlock) receivedBlock;
    ProofOfWorkBlock currPoWBlock = (ProofOfWorkBlock) currentBlock;
    int receivedBlockHeight = receivedBlock.getHeight();
    ProofOfWorkBlock receivedBlockParent = receivedBlockHeight == 0 ? null :
        (ProofOfWorkBlock) receivedBlock.getBlockWithHeight(receivedBlockHeight - 1);

    //TODO - dangerous to split due to short circuit operators being used, refactor?
    return (
        receivedBlockHeight == 0 ||
            recPoWBlock.getDifficulty().compareTo(receivedBlockParent.getNextDifficulty()) >= 0
    ) && (
        currentBlock == null ||
            recPoWBlock.getTotalDifficulty().compareTo(currPoWBlock.getTotalDifficulty()) > 0
    );*/

    return true;
  }

  @Override
  public ProofOfWorkBlock genesisBlock(Node selfNode) {
    return ProofOfWorkBlock.genesisBlock(selfNode);
  }

}
