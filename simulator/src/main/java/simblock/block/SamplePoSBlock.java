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

import static simblock.settings.SimulationConfiguration.AVERAGE_COINS;
import static simblock.settings.SimulationConfiguration.STAKING_REWARD;
import static simblock.settings.SimulationConfiguration.STDEV_OF_COINS;
import static simblock.simulator.Main.random;
import static simblock.simulator.Simulator.getSimulatedNodes;
import static simblock.simulator.Simulator.getTargetInterval;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import simblock.node.Node;

/**
 * The type Sample proof of stake block.
 */
public class SamplePoSBlock extends Block {
  private final Map<Node, Coinage> coinages;
  private static Map<Node, Coinage> genesisCoinages;
  private final BigInteger difficulty;
  private final BigInteger totalDifficulty;
  private final BigInteger nextDifficulty;

  /**
   * Instantiates a new Sample proof of stake block.
   *
   * @param parent     the parent
   * @param minter     the minter
   * @param time       the time
   * @param difficulty the difficulty
   */
  public SamplePoSBlock(
      SamplePoSBlock parent, Node minter, long time, BigInteger difficulty
  ) {
    super(parent, minter, time);

    this.coinages = new HashMap<>();
    if (parent == null) {
      for (Node node : getSimulatedNodes()) {
        this.coinages.put(node, genesisCoinages.get(node).clone());
      }
    } else {
      for (Node node : getSimulatedNodes()) {
        this.coinages.put(node, parent.getCoinage(node).clone());
        this.coinages.get(node).increaseAge();
      }
      this.coinages.get(minter).reward(STAKING_REWARD);
      this.coinages.get(minter).resetAge();
    }

    BigInteger totalCoinage = BigInteger.ZERO;
    for (Node node : getSimulatedNodes()) {
      totalCoinage = totalCoinage.add(this.coinages.get(node).getCoinage());
    }

    this.difficulty = difficulty;
    if (parent == null) {
      this.totalDifficulty = BigInteger.ZERO.add(difficulty);
    } else {
      this.totalDifficulty = parent.getTotalDifficulty().add(difficulty);
    }
    this.nextDifficulty = totalCoinage.multiply(
            BigInteger.valueOf(getTargetInterval())).divide(BigInteger.valueOf(1000)
    );
  }

  /**
   * Gets coinage.
   *
   * @param node the node
   * @return the coinage
   */
  //TODO Coinage is related to proof of stake obviously
  public Coinage getCoinage(Node node) {
    return this.coinages.get(node);
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

  private static Coinage genCoinage() {
    double r = random.nextGaussian();
    BigInteger coins = BigInteger.valueOf(Math.max((int) (r * STDEV_OF_COINS + AVERAGE_COINS), 0));
    return new Coinage(coins, 1);
  }

  /**
   * Genesis block sample proof of stake block.
   *
   * @param minter the minter
   * @return the sample proof of stake block
   */
  public static SamplePoSBlock genesisBlock(Node minter) {
    genesisCoinages = new HashMap<>();
    for (Node node : getSimulatedNodes()) {
      genesisCoinages.put(node, genCoinage());
    }
    return new SamplePoSBlock(null, minter, 0, BigInteger.ZERO);
  }
}
