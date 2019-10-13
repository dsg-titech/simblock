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
package SimBlock.task;

import SimBlock.node.Block;
import SimBlock.node.Node;
import static SimBlock.simulator.Timer.*;

public class MiningTask implements Task {
	private Node miner;
	private Block parent;
	private long interval;
	private String proofOfWhat;
	private long difficulty;
	
	public MiningTask(Node miner, long interval, String proofOfWhat, long difficulty) {
		this.miner = miner;
		this.parent = miner.getBlock();
		this.interval = interval;
		this.proofOfWhat = proofOfWhat;
		this.difficulty = difficulty;
	}
	
	@Override
	public long getInterval() {
		return this.interval;
	}

	@Override
	public void run() {
		Block createdBlock = new Block(this.parent, this.miner, getCurrentTime(), this.proofOfWhat, this.difficulty, null, null);
		this.miner.receiveBlock(createdBlock);
	}

	public Block getParent(){
		return this.parent;
	}
}
