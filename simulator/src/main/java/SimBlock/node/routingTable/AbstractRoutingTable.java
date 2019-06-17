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
package SimBlock.node.routingTable;

import java.util.ArrayList;

import SimBlock.node.Node;

public abstract class AbstractRoutingTable {
	private Node selfNode;
	private int nConnection = 8;

	public AbstractRoutingTable(Node selfNode){
		this.selfNode = selfNode;
	}
	protected Node getSelfNode() {return selfNode;}
	public void setnConnection(int nConnection) {this.nConnection = nConnection;}
	public int getnConnection(){return this.nConnection;}
	public abstract void initTable();
	public abstract ArrayList<Node> getNeighbors();
	public abstract boolean addNeighbor(Node node);
	public abstract boolean removeNeighbor(Node node);

	public boolean addInbound(Node from){return false;};
	public boolean removeInbound(Node from){return false;};
	public void acceptBlock(){};

}
