package simblock.task;

import simblock.block.ProofOfWorkBlock;
import simblock.node.Node;
import simblock.node.SelfishNode;

import java.math.BigInteger;

import static simblock.simulator.Timer.getCurrentTime;

public class SelfishMiningTask extends AbstractMintingTask {
    private final BigInteger difficulty;
    /**
     * Instantiates a new Abstract minting task.
     *
     * @param minter   the minter
     * @param interval the interval in milliseconds
     */
    public SelfishMiningTask(Node minter, long interval, BigInteger difficulty) {
        super(minter, interval);
        this.difficulty = difficulty;
    }

    @Override
    public void run() {
        ProofOfWorkBlock createdBlock = new ProofOfWorkBlock(
                (ProofOfWorkBlock) this.getParent(), this.getMinter(), getCurrentTime(),
                this.difficulty
        );
        ((SelfishNode)this.getMinter()).receiveSelfishBlock(createdBlock);
    }
}
