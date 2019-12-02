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
import SimBlock.block.ProofOfWorkBlock;
import SimBlock.node.Node;
import SimBlock.task.MiningTask;
import static SimBlock.simulator.Main.*;

public class ProofOfWork extends AbstractConsensusAlgo {
	public ProofOfWork(Node selfNode) {
		super(selfNode);
	}

	@Override
	public MiningTask minting() {
		Node selfNode = this.getSelfNode();
		ProofOfWorkBlock parent = (ProofOfWorkBlock)selfNode.getBlock();
		long difficulty = parent.getNextDifficulty();
		double p = 1.0 / difficulty;
		double u = random.nextDouble();
		return new MiningTask(selfNode, (long)( Math.log(u) / Math.log(1.0-p) / selfNode.getMiningPower() ), difficulty);
	}

	@Override
	public boolean isReceivedBlockValid(Block receivedBlock, Block currentBlock) {
		if (!(receivedBlock instanceof ProofOfWorkBlock)) return false;
		ProofOfWorkBlock _receivedBlock = (ProofOfWorkBlock)receivedBlock;
		ProofOfWorkBlock _currentBlock = (ProofOfWorkBlock)currentBlock;
		int receivedBlockHeight = receivedBlock.getHeight();
		return (
				receivedBlockHeight == 0 ||
				_receivedBlock.getDifficulty() >= ((ProofOfWorkBlock)receivedBlock.getBlockWithHeight(receivedBlockHeight-1)).getNextDifficulty()
			) && (
				currentBlock == null ||
				_receivedBlock.getTotalDifficulty().compareTo(_currentBlock.getTotalDifficulty()) > 0
			);
	}

	@Override
	public ProofOfWorkBlock genesisBlock() {
		return ProofOfWorkBlock.genesisBlock(this.getSelfNode());
	}
}
