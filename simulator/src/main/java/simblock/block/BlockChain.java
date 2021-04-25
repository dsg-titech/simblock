package simblock.block;


import utility.graph.DAG;

import java.util.ArrayList;
import java.util.Collections;

public class BlockChain {
    private static final BlockChain instance = new BlockChain();
    private DAG<Block> chain;


    private BlockChain(){
        chain = new DAG<Block>();
    }


    public static BlockChain getInstance(){
        return instance;
    }

    public void setGenesisBlock(Block genesisBlock){
        chain.setRoot(genesisBlock);
    }

    public void addBlock(Block block) throws Exception {
        int currentHeight = chain.getCurrentHeight();
        if(block.getHeight() <= currentHeight){
            if(!doesBlockExist(block)){
                chain.addToDAG(block, block.getHeight());
            }
        }
        else if(block.getHeight() <= currentHeight + 1){
            chain.addToDAG(block, block.getHeight());
        }
        else{
            throw new Exception("Problem in Adding block to chain");
        }

        return;
    }

    public void putBlockInMainChain(Block block){
        try{
            int blockHeight = block.getHeight();
            ArrayList<Block> blocksOnSameHeight = chain.getNodesByHeight(blockHeight);

            if(blocksOnSameHeight.size() == 1){
                return;
            }
            else{
                int blockIndex = -1;

                for(int i=0; i < blocksOnSameHeight.size(); i++){
                    if(block.getId() == blocksOnSameHeight.get(i).getId()){
                        blockIndex = i;
                        break;
                    }
                }

                if(blockIndex == -1){
                    throw new Exception("Cannot find Block for Swapping");
                }

                Collections.swap(blocksOnSameHeight, 0, blockIndex);
                chain.setDAGHeight(blockHeight, blocksOnSameHeight);
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public int getChainHeight(){
        return chain.getCurrentHeight();
    }

    public int getTotalNumberOfBlocksOnChain(){
        return chain.getTotalNumberOfNodes();
    }

    public boolean doesBlockExist(Block block){
        ArrayList<Block> blocksOnSameHeight = chain.getNodesByHeight(block.getHeight());

        for(Block chainBlock : blocksOnSameHeight){
            if(chainBlock.getId() == block.getId()){
                return true;
            }
        }

        return false;
    }

    public ArrayList<Block> getMainChain(){
        ArrayList<Block> mainChain = new ArrayList<>();

        for(int i=0; i < chain.getCurrentHeight(); i++){
            mainChain.add(chain.getNodesByHeight(i).get(0));
        }

        return mainChain;
    }
}
