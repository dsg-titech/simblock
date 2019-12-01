package SimBlock.task;

import SimBlock.block.Block;
import SimBlock.node.Node;

public abstract class AbstractMintingTask implements Task {
	private Node minter;
	private Block parent;
	private long interval;
	
	public AbstractMintingTask(Node minter, long interval) {
		this.parent = minter.getBlock();
		this.minter = minter;
		this.interval = interval;
	}

	public Node getMinter() {
		return minter;
	}

	public Block getParent() {
		return parent;
	}
	
	@Override
	public long getInterval() {
		return this.interval;
	}
}
