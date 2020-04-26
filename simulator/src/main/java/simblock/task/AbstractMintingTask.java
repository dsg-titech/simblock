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

package simblock.task;

import simblock.block.Block;
import simblock.node.Node;

/**
 * The type Abstract minting task represents .
 */
public abstract class AbstractMintingTask implements Task {
  /**
   * The node to mint the block.
   */
  private final Node minter;
  /**
   * The parent block.
   */
  private final Block parent;

  /**
   * Block interval in milliseconds.
   */
  private final long interval;

  /**
   * Instantiates a new Abstract minting task.
   *
   * @param minter   the minter
   * @param interval the interval in milliseconds
   */
  public AbstractMintingTask(Node minter, long interval) {
    this.parent = minter.getBlock();
    this.minter = minter;
    this.interval = interval;
  }

  /**
   * Gets minter.
   *
   * @return the minter
   */
  public Node getMinter() {
    return minter;
  }

  /**
   * Gets the minted blocks parent.
   *
   * @return the parent
   */
  public Block getParent() {
    return parent;
  }

  @Override
  public long getInterval() {
    return this.interval;
  }
}
