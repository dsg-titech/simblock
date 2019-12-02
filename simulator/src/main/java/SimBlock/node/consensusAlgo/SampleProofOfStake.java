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

import SimBlock.block.Block;
import SimBlock.block.SampleProofOfStakeBlock;
import SimBlock.node.Node;
import SimBlock.task.SampleStakingTask;
import static SimBlock.simulator.Main.*;

public class SampleProofOfStake extends AbstractConsensusAlgo {
	public SampleProofOfStake(Node selfNode) {
		super(selfNode);
	}

	@Override
	public SampleStakingTask minting() {
		Node selfNode = this.getSelfNode();
		SampleProofOfStakeBlock parent = (SampleProofOfStakeBlock)selfNode.getBlock();
		long difficulty = parent.getNextDifficulty();
		double p = (double)parent.getCoinage(selfNode).getCoinage() / difficulty;
		double u = random.nextDouble();
		return p == 0 ? null : new SampleStakingTask(selfNode, (long)( Math.log(u) / Math.log(1.0-p) * 1000 ), difficulty);
	}

	@Override
	public boolean isReceivedBlockValid(Block receivedBlock, Block currentBlock) {
		if (!(receivedBlock instanceof SampleProofOfStakeBlock)) return false;
		SampleProofOfStakeBlock _receivedBlock = (SampleProofOfStakeBlock)receivedBlock;
		SampleProofOfStakeBlock _currentBlock = (SampleProofOfStakeBlock)currentBlock;
		int receivedBlockHeight = receivedBlock.getHeight();
		return (
				receivedBlockHeight == 0 ||
				_receivedBlock.getDifficulty() >= ((SampleProofOfStakeBlock)receivedBlock.getBlockWithHeight(receivedBlockHeight-1)).getNextDifficulty()
			) && (
				currentBlock == null ||
				_receivedBlock.getTotalDifficulty().compareTo(_currentBlock.getTotalDifficulty()) > 0
			);
	}

	@Override
	public SampleProofOfStakeBlock genesisBlock() {
		return SampleProofOfStakeBlock.genesisBlock(this.getSelfNode());
	}
}
