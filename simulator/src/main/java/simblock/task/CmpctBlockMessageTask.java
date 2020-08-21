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

import static simblock.simulator.Main.OUT_JSON_FILE;
import static simblock.simulator.Network.getLatency;
import static simblock.simulator.Timer.getCurrentTime;

import simblock.block.Block;
import simblock.node.Node;

/**
 * The type Compact block message task.
 */
// Compact block relay protocol Wiki: https://github.com/bitcoin/bips/blob/master/bip-0152.mediawiki
public class CmpctBlockMessageTask extends AbstractMessageTask {
  /**
   * The {@link Block} that is sent.
   */
  private final Block block;

  /**
   * The block message sending delay in milliseconds.
   */
  private final long interval;

  /**
   * Instantiates a new Compact block message task.
   *
   * @param from  the sender
   * @param to    the receiver
   * @param block the block instance
   * @param delay the delay of the message transmission
   */
  public CmpctBlockMessageTask(Node from, Node to, Block block, long delay) {
    super(from, to);
    this.block = block;
    this.interval = getLatency(this.getFrom().getRegion(), this.getTo().getRegion()) + delay;
  }


  @Override
  public long getInterval() {
    return this.interval;
  }

  /**
   * Sends a new compact block message from the sender to the receiver and logs the event.
   */
  @Override
  public void run() {

    this.getFrom().sendNextBlockMessage();

    OUT_JSON_FILE.print("{");
    OUT_JSON_FILE.print("\"kind\":\"flow-block\",");
    OUT_JSON_FILE.print("\"content\":{");
    OUT_JSON_FILE.print("\"transmission-timestamp\":" + (getCurrentTime() - this.interval) + ",");
    OUT_JSON_FILE.print("\"reception-timestamp\":" + getCurrentTime() + ",");
    OUT_JSON_FILE.print("\"begin-node-id\":" + getFrom().getNodeID() + ",");
    OUT_JSON_FILE.print("\"end-node-id\":" + getTo().getNodeID() + ",");
    OUT_JSON_FILE.print("\"block-id\":" + block.getId());
    OUT_JSON_FILE.print("}");
    OUT_JSON_FILE.print("},");
    OUT_JSON_FILE.flush();

    super.run();
  }

  /**
   * Get block.
   *
   * @return the block
   */
  public Block getBlock() {
    return this.block;
  }
}