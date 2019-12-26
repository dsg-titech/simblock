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
import SimBlock.node.Node;
import SimBlock.task.AbstractMintingTask;

public abstract class AbstractConsensusAlgo {
	private Node selfNode;

	public AbstractConsensusAlgo(Node selfNode) {
		this.selfNode = selfNode;
	}

	public Node getSelfNode() { return this.selfNode; }

	public abstract AbstractMintingTask minting();
	public abstract boolean isReceivedBlockValid(Block receivedBlock, Block currentBlock);
	public abstract Block genesisBlock();
}
