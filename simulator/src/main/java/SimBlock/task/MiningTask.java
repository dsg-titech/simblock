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
import static SimBlock.simulator.Simulator.*;
import static SimBlock.simulator.Main.*;

public class MiningTask implements Task {
	private Node miningNode;
	private Block parentBlock;
	private long interval;
	
	public MiningTask(Node miningNode) {
		this.miningNode = miningNode;
		this.parentBlock = miningNode.getBlock();
	
		double p = getDifficulty();
		double u = random.nextDouble();
		this.interval = (long)(  ( Math.log(u) / Math.log(1.0-p) ) / this.miningNode.getPower() );
	}
	
	@Override
	public long getInterval() {
		return this.interval;
	}

	@Override
	public void run() {
		Block createdBlock = new Block(this.parentBlock.getHeight() + 1, this.parentBlock, this.miningNode ,getCurrentTime());
		this.miningNode.receiveBlock(createdBlock);
	}

	public Block getParent(){
		return this.parentBlock;
	}
}
