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
package SimBlock.settings;

public class SimulationConfiguration {
	public static final int NUM_OF_NODES = 600;//600;//800;//6000;
	public static final String TABLE = "SimBlock.node.routingTable.BitcoinCoreTable";
	public static final long INTERVAL = 1000*60*10;//1000*60;//1000*30*5;//1000*60*10;
	public static final int ENDBLOCKHEIGHT = 100;
	public static final long BLOCKSIZE = 535000;//6110;//8000;//535000;//0.5MB
}