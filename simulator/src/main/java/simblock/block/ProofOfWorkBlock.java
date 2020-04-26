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

package simblock.block;

import static simblock.simulator.Simulator.getSimulatedNodes;
import static simblock.simulator.Simulator.getTargetInterval;

import java.math.BigInteger;
import simblock.node.Node;


/**
 * The type Proof of work block.
 */
public class ProofOfWorkBlock extends Block {
  private final BigInteger difficulty;
  private final BigInteger totalDifficulty;
  private final BigInteger nextDifficulty;
  private static BigInteger genesisNextDifficulty;

  /**
   * Instantiates a new Proof of work block.
   *
   * @param parent     the parent
   * @param minter     the minter
   * @param time       the time
   * @param difficulty the difficulty
   */
  public ProofOfWorkBlock(ProofOfWorkBlock parent, Node minter, long time, BigInteger difficulty) {
    super(parent, minter, time);
    this.difficulty = difficulty;

    if (parent == null) {
      this.totalDifficulty = BigInteger.ZERO.add(difficulty);
      this.nextDifficulty = ProofOfWorkBlock.genesisNextDifficulty;
    } else {
      this.totalDifficulty = parent.getTotalDifficulty().add(difficulty);
      // TODO: difficulty adjustment
      this.nextDifficulty = parent.getNextDifficulty();
    }

  }

  /**
   * Gets difficulty.
   *
   * @return the difficulty
   */
  public BigInteger getDifficulty() {
    return this.difficulty;
  }

  /**
   * Gets total difficulty.
   *
   * @return the total difficulty
   */
  public BigInteger getTotalDifficulty() {
    return this.totalDifficulty;
  }

  /**
   * Gets next difficulty.
   *
   * @return the next difficulty
   */
  public BigInteger getNextDifficulty() {
    return this.nextDifficulty;
  }

  /**
   * Generates the genesis block, gets the total mining power and adjusts the difficulty of the
   * next block accordingly.
   *
   * @param minter the minter
   * @return the genesis block
   */
  public static ProofOfWorkBlock genesisBlock(Node minter) {
    long totalMiningPower = 0;
    for (Node node : getSimulatedNodes()) {
      totalMiningPower += node.getMiningPower();
    }
    genesisNextDifficulty = BigInteger.valueOf(totalMiningPower * getTargetInterval());
    return new ProofOfWorkBlock(null, minter, 0, BigInteger.ZERO);
  }
}
