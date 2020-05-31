/*
 * Copyright 2019 Distributed Systems Group
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simblock.node.consensus;

import static simblock.simulator.Main.random;

import java.math.BigInteger;
import simblock.block.Block;
import simblock.block.SamplePoSBlock;
import simblock.node.Node;
import simblock.task.SampleStakingTask;

/**
 * The type Sample proof of stake.
 */
@SuppressWarnings("unused")
public class SampleProofOfStake extends AbstractConsensusAlgo {
  /**
   * Instantiates a new Sample proof of stake.
   *
   * @param selfNode the self node
   */
  public SampleProofOfStake(Node selfNode) {
    super(selfNode);
  }

  @Override
  public SampleStakingTask minting() {
    Node selfNode = this.getSelfNode();
    SamplePoSBlock parent = (SamplePoSBlock) selfNode.getBlock();
    BigInteger difficulty = parent.getNextDifficulty();
    double p = parent.getCoinage(selfNode).getCoinage().doubleValue() / difficulty.doubleValue();
    double u = random.nextDouble();
    return p <= Math.pow(2, -53) ? null : new SampleStakingTask(selfNode,
                                                                (long) (Math.log(u) / Math.log(
                                                                    1.0 - p) * 1000), difficulty
    );
  }

  @SuppressWarnings("CheckStyle")
  @Override
  public boolean isReceivedBlockValid(Block receivedBlock, Block currentBlock) {
    if (!(receivedBlock instanceof SamplePoSBlock)) {
      return false;
    }
    SamplePoSBlock recPoSBlock = (SamplePoSBlock) receivedBlock;
    SamplePoSBlock currPoSBlock = (SamplePoSBlock) currentBlock;
    int receivedBlockHeight = receivedBlock.getHeight();
    SamplePoSBlock receivedBlockParent = receivedBlockHeight == 0 ? null :
        (SamplePoSBlock) receivedBlock.getBlockWithHeight(receivedBlockHeight - 1);

    //TODO - dangerous to split due to short circuit operators being used, refactor?
    return (
        receivedBlockHeight == 0 ||
            recPoSBlock.getDifficulty().compareTo(receivedBlockParent.getNextDifficulty()) >= 0
    ) && (
        currentBlock == null ||
            recPoSBlock.getTotalDifficulty().compareTo(currPoSBlock.getTotalDifficulty()) > 0
    );
  }

  @Override
  public SamplePoSBlock genesisBlock() {
    return SamplePoSBlock.genesisBlock(this.getSelfNode());
  }
}
