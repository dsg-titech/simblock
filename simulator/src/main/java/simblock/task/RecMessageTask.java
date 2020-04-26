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
 * The type Rec message task receives a block.
 */

public class RecMessageTask extends AbstractMessageTask {

  /**
   * The block to  be received.
   */
  private final Block block;

  /**
   * Instantiates a new Rec message task.
   *
   * @param from  the sending node
   * @param to    the receiving node
   * @param block the block to be received
   */
  public RecMessageTask(Node from, Node to, Block block) {
    super(from, to);
    this.block = block;
  }

  /**
   * Gets the block to be received.
   *
   * @return the block
   */
  public Block getBlock() {
    return this.block;
  }

}
