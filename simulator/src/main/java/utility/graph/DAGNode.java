package utility.graph;

public class DAGNode<T> {
    private T data;

    public DAGNode(T data){
        this.data = data;
    }

    public T getData(){
        return this.data;
    }
}
