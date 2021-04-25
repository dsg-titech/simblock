package utility.graph;



import simblock.block.Block;

import java.util.ArrayList;

public class DAG<T> {
    private ArrayList<ArrayList<DAGNode<T>>> graph;
    private int height = -1;

    public DAG(){
        graph = new ArrayList<ArrayList<DAGNode<T>>>();
    }

    public void setRoot(T root){
        DAGNode<T> node = new DAGNode<T>(root);
        height = 0;

        graph.add(new ArrayList<DAGNode<T>>());
        graph.get(height).add(node);

        return;
    }

    public void addToDAG(T newValue, int newHeight){
        DAGNode<T> node = new DAGNode<T>(newValue);

        if(height >= newHeight){
            graph.get(newHeight).add(node);
        }
        else{
            graph.add(new ArrayList<DAGNode<T>>());
            height++;
            graph.get(newHeight).add(node);
        }

        return;
    }

    public ArrayList<ArrayList<DAGNode<T>>> getDAG(){
        return graph;
    }

    public int getCurrentHeight(){
        return height;
    }

    public ArrayList<T> getNodesByHeight(int height){
        ArrayList<T> result = new ArrayList<T>();

        for(DAGNode<T> node : graph.get(height)){
         result.add(node.getData());
        }

        return result;
    }

    public T getNodeByHeightRow(int height, int row){
        return graph.get(height).get(row).getData();
    }

    public int getTotalNumberOfNodes(){
        int counter = 0;

        for(int i=0; i < graph.size(); i++){
            for(int j=0; j < graph.get(i).size(); j++){
                counter++;
            }
        }

        return counter;
    }

    public void setDAGHeight(int height, ArrayList<T> inputNodes){
        ArrayList<DAGNode<T>> result = new ArrayList<>();

        for(T node : inputNodes){
            result.add(new DAGNode<T>(node));
        }

        graph.set(height, result);
    }
}
