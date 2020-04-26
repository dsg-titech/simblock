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

import simblock.block.Block;
import simblock.node.Node;
import simblock.task.AbstractMintingTask;

/**
 * The type Abstract consensus algorithm.
 */
public abstract class AbstractConsensusAlgo {
  private final Node selfNode;

  /**
   * Instantiates a new Abstract consensus algo.
   *
   * @param selfNode the self node
   */
  public AbstractConsensusAlgo(Node selfNode) {
    this.selfNode = selfNode;
  }

  /**
   * Gets the node using this consensus algorithm.
   *
   * @return the self node
   */
  public Node getSelfNode() {
    return this.selfNode;
  }

  /**
   * Minting abstract minting task.
   *
   * @return the abstract minting task
   */
  public abstract AbstractMintingTask minting();

  /**
   * Tests if the receivedBlock is valid with regards to the current block.
   *
   * @param receivedBlock the received block
   * @param currentBlock  the current block
   * @return true if block is valid false otherwise
   */
  public abstract boolean isReceivedBlockValid(Block receivedBlock, Block currentBlock);

  /**
   * Gets the genesis block.
   *
   * @return the genesis block
   */
  public abstract Block genesisBlock();
}
