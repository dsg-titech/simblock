package simblock.node;

import simblock.block.Block;
import simblock.block.BlockChain;
import simblock.task.*;

import java.io.PrintWriter;
import java.util.*;

import static simblock.settings.SimulationConfiguration.*;
import static simblock.simulator.Simulator.arriveBlock;
import static simblock.simulator.Timer.putTask;

public class SelfishNode extends Node {
    private final ArrayList<Block> privateChain = new ArrayList<Block>();
    private final ArrayList<Block> publicChain = new ArrayList<Block>();
    private final HashSet<Integer> seenBlock = new HashSet<>();

    private boolean attackStarted = false;

    private int selfishMinerWinBlock = 0;
    private int honestMinerWinBlock = 0;
    private int minedBlock = 0;

    protected SelfishMiningTask mintingTask = null;

    public SelfishNode(int nodeID, int numConnection, int region, long miningPower, String routingTableName,
                       String consensusAlgoName, boolean useCBR, boolean isChurnNode) {
        super(nodeID, numConnection, region, miningPower, routingTableName, consensusAlgoName, useCBR, isChurnNode);
    }

    public void selfishMinting() {
        SelfishMiningTask task = (SelfishMiningTask) this.consensusAlgo.minting(this);
        this.mintingTask = task;
        if (task != null) {
            putTask(task);
        }
    }

    public void receiveBlock(Block block) {
        boolean seenBlock = addToSeenBlock(block);
        try{
            BlockChain.getInstance().addBlock(block);
        }
        catch (Exception ex){
            System.out.println("Public Exception!");
        }
        if (this.consensusAlgo.isReceivedBlockValid(this, block, this.block) && !seenBlock) {
            if (this.block != null && !this.block.isOnSameChainAs(block)) {
                // If orphan mark orphan
                this.addOrphans(this.block, block);
            }

            if(block.getHeight() != 0){
                if(attackStarted){
                    this.honestMiningDecision(block);
                }
                else{
                    this.honestMinerWinBlock++;
                }
            }
            else{
                BlockChain.getInstance().setGenesisBlock(block);
            }
            // Else add to canonical chain
            this.addToChain(block);
            // Advertise received block
            this.sendInv(block);
        } else if (!this.orphans.contains(block) && !block.isOnSameChainAs(this.block)) {
            // TODO better understand - what if orphan is not valid?
            // If the block was not valid but was an unknown orphan and is not on the same chain as the
            // current block
            this.addOrphans(block, this.block);
            arriveBlock(block, this);
        }
    }

    public void receiveSelfishBlock(Block block){
        if (this.consensusAlgo.isReceivedBlockValid(this, block, this.block)) {
            if (this.block != null && !this.block.isOnSameChainAs(block)) {
                // If orphan mark orphan
                this.addOrphans(this.block, block);
            }

            attackStarted = true;

            int delta = calculateDelta();
            this.addToPrivateChain(block);
//            this.addToSeenBlock(block);

            if(delta == 0 && this.calculateSelfishChainLength() == 2){
                this.selfishMinerWinBlock += 2;
                for(Block privateBlock : privateChain){
                    try{
                        BlockChain.getInstance().addBlock(privateBlock);
                        BlockChain.getInstance().putBlockInMainChain(privateBlock);
                    }
                    catch (Exception ex){
                        System.out.println("Private Exception!");
                    }
                }
                this.stopAttack();
            }

            //this.selfishMinting();
        } else if (!this.orphans.contains(block) && !block.isOnSameChainAs(this.block)) {
            // TODO better understand - what if orphan is not valid?
            // If the block was not valid but was an unknown orphan and is not on the same chain as the
            // current block
            this.addOrphans(block, this.block);
            arriveBlock(block, this);
        }
    }

    public Block getBlock() {
        return privateChain.size() > 0 ? privateChain.get(privateChain.size() - 1) : this.block;
    }

    public void printSelfishMiningStatus(){
        if(privateChain.size() > 0){
            //this.selfishMinerWinBlock += this.privateChain.size();
        }
        System.out.println("Selfish Win Block : " + this.selfishMinerWinBlock);
        System.out.println("Honest win Block : " + this.honestMinerWinBlock);
        this.minedBlock = this.selfishMinerWinBlock + this.honestMinerWinBlock;
        System.out.println("Mined Block : " + this.minedBlock);
        System.out.println("Relative Revenue : " + ((double)selfishMinerWinBlock / this.minedBlock));
        System.out.println("**********************************************************************");
        int a = BlockChain.getInstance().getTotalNumberOfBlocksOnChain();
        ArrayList<Block> mainChain = BlockChain.getInstance().getMainChain();
        int newSelfishWin = 0;
        int newHonestWin = 0;
        for(Block mainBlock : mainChain){
            if(mainBlock.getMinter() instanceof SelfishNode){
                newSelfishWin++;
            }
            else{
                newHonestWin++;
            }
        }
        System.out.println("New Selfish Win Block : " + newSelfishWin);
        System.out.println("New Honest win Block : " + newHonestWin);
        int newMinedBlock = newSelfishWin + newHonestWin;
        System.out.println("New Mined Block : " + newMinedBlock);
        System.out.println("Relative Revenue : " + ((double)newSelfishWin / newMinedBlock));
        System.out.println("New Main Chain Size : " + BlockChain.getInstance().getTotalNumberOfBlocksOnChain());
    }

    public int getSeenBlockSize(){
        return this.seenBlock.size();
    }

    public int getPrivateChainSize(){
        return this.privateChain.size();
    }

    public int getPublicChainSize(){
        return this.publicChain.size();
    }

    private int calculateDelta(){
        return this.privateChain.size() - this.publicChain.size();
    }

    private int calculateSelfishChainLength(){
        return this.privateChain.size();
    }

    private void addToPublicChain(Block block){
        try{
            int forkHeight = privateChain.get(0).getHeight();
            if(block.getHeight() >= forkHeight){
                this.publicChain.add(block);
            }
        }
        catch (Exception ex){
            System.out.println("Exception");
        }
    }

    private void addToPrivateChain(Block block){
        if(!isBlockInPrivateChain(block) && !seenBlock(block)){
            this.privateChain.add(block);
        }
    }

    private boolean isBlockInPrivateChain(Block block){
        for(Block privateBlock : privateChain){
            if(privateBlock.getHeight() == block.getHeight()){
                return true;
            }
        }

        return false;
    }

    private void stopAttack(){
        for(Block privateBlock : privateChain){
            this.addToSeenBlock(privateBlock);
            this.addToChain(privateBlock);
            this.sendInv(privateBlock);
        }
        this.privateChain.clear();
        this.publicChain.clear();
        this.attackStarted = false;
    }

    private void honestMiningDecision(Block honestBlock){
        int delta = this.calculateDelta();
        this.addToPublicChain(block);

        if(delta == 0 && this.privateChain.size() == 0){
            //!< State 2
            this.honestMinerWinBlock += 1;
            BlockChain.getInstance().putBlockInMainChain(honestBlock);
            this.stopAttack();
        }
        else if(delta == 0 && this.privateChain.size() == 1){
            //!< State 3
            float generatedRandom = (float)Math.random();

            if(generatedRandom < SELFISH_MINER_GAMMA){
                this.honestMinerWinBlock += 1;
                this.selfishMinerWinBlock += 1;
                BlockChain.getInstance().putBlockInMainChain(privateChain.get(0));
                BlockChain.getInstance().putBlockInMainChain(honestBlock);
            }
            else{
             this.honestMinerWinBlock += 2;
            }

            this.stopAttack();
        }
        else if(delta == 1){
            //!< State 4
            //!< Nothing to do...Another state define result of this state
        }
        else if(delta == 2){
            //!< State 5
            this.selfishMinerWinBlock += this.privateChain.size();
            for(Block privateBlock : privateChain){
                try{
                    BlockChain.getInstance().addBlock(privateBlock);
                    BlockChain.getInstance().putBlockInMainChain(privateBlock);
                }
                catch (Exception ex){
                    System.out.println("Private 2 Exception!");
                }
            }
            this.stopAttack();
        }
        else if(delta > 2){
            //!< State 6
            //!< Nothing to do...Another state define result of this state
        }
    }

    private boolean addToSeenBlock(Block block){
        for(int seen : seenBlock){
            if(seen == block.getHeight()){
                return true;
            }
        }
        seenBlock.add(block.getHeight());
        System.out.println(block.getHeight());
        return false;
    }

    private  boolean seenBlock(Block block){
        for(int seen : seenBlock){
            if(seen == block.getHeight()){
                return true;
            }
        }

        return false;
    }
}
