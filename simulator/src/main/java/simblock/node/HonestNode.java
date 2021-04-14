package simblock.node;

public class HonestNode extends Node{
    /**
     * Instantiates a new Node.
     *
     * @param nodeID            the node id
     * @param numConnection     the number of connections a node can have
     * @param region            the region
     * @param miningPower       the mining power
     * @param routingTableName  the routing table name
     * @param consensusAlgoName the consensus algorithm name
     * @param useCBR            whether the node uses compact block relay
     * @param isChurnNode       whether the node causes churn
     */
    public HonestNode(int nodeID, int numConnection, int region, long miningPower, String routingTableName, String consensusAlgoName, boolean useCBR, boolean isChurnNode) {
        super(nodeID, numConnection, region, miningPower, routingTableName, consensusAlgoName, useCBR, isChurnNode);
    }
}
