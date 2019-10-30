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
package SimBlock.node.consensusAlgo;

import SimBlock.node.Node;
import SimBlock.task.MiningTask;
import SimBlock.node.Block;
import static SimBlock.simulator.Main.*;

public class SampleProofOfStake extends AbstractConsensusAlgo {
	public SampleProofOfStake(Node selfNode) {
		super(selfNode);
	}

	@Override
	public MiningTask mining() {
		Node selfNode = this.getSelfNode();
		long difficulty = selfNode.getBlock().getNextDifficulty("stake");
		double p = (double)selfNode.getBlock().getCoinage(selfNode).getCoinage() / difficulty;
		double u = random.nextDouble();
		return p == 0 ? null : new MiningTask(selfNode, (long)( Math.log(u) / Math.log(1.0-p) * 1000 ), "stake", difficulty);
	}

	@Override
	public boolean isReceivedBlockValid(Block receivedBlock, Block currentBlock) {
		return (
				receivedBlock.getHeight() == 0 ||
				receivedBlock.getProofOfWhat() == "stake"
			) && (
				receivedBlock.getHeight() == 0 ||
				receivedBlock.getDifficulty() >= receivedBlock.getBlockWithHeight(receivedBlock.getHeight()-1).getNextDifficulty("stake")
			) && (
				currentBlock == null ||
				receivedBlock.getTotalDifficulty() > currentBlock.getTotalDifficulty()
			);
	}
}
