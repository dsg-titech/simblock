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

import SimBlock.block.SampleProofOfStakeBlock;
import SimBlock.node.Node;
import static SimBlock.simulator.Timer.*;

import java.math.BigInteger;

public class SampleStakingTask extends AbstractMintingTask {
	private BigInteger difficulty;
	
	public SampleStakingTask(Node minter, long interval, BigInteger difficulty) {
		super(minter, interval);
		this.difficulty = difficulty;
	}

	@Override
	public void run() {
		SampleProofOfStakeBlock createdBlock = new SampleProofOfStakeBlock((SampleProofOfStakeBlock)this.getParent(), this.getMinter(), getCurrentTime(), this.difficulty);
		this.getMinter().receiveBlock(createdBlock);
	}
}
