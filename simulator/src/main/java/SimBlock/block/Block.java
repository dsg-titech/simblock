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
package SimBlock.block;

import SimBlock.node.Node;

public class Block {
	private int height;
	private Block parent;
	private Node minter;
	private long time;
	private int id;
	private static int latestId = 0;

	public Block(Block parent, Node minter, long time){
		this.height = parent == null ? 0 : parent.getHeight() + 1;
		this.parent = parent;
		this.minter = minter;
		this.time = time;
		this.id = latestId;
		latestId++;
	}

	public int getHeight(){return this.height;}
	public Block getParent(){return this.parent;}
	public Node getMinter(){return this.minter;}
	public long getTime(){return this.time;}
	public int getId() {return this.id;}
	
	public static Block genesisBlock(Node minter) {
		return new Block(null, minter, 0);
	}

	// return ancestor block that height is {height}
	public Block getBlockWithHeight(int height){
		if(this.height == height){
			return this;
		}else{
			return this.parent.getBlockWithHeight(height);
		}
	}
}
