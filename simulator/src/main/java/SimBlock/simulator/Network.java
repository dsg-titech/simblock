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
package SimBlock.simulator;

import static SimBlock.settings.NetworkConfiguration.*;
import static SimBlock.simulator.Main.*;

import java.util.List;

public class Network {
	// latency according with 20% variance pallet distribution
	public static final long getLatency(int from, int to){
		long mean = LATENCY[from][to];
		double shape = 0.2 * mean;
		double scale = mean - 5;
		return Math.round( scale / Math.pow(random.nextDouble(),1.0/shape) );
	}

	// bandwidth
	public static final long getBandwidth(int from, int to) {
		return Math.min(UPLOAD_BANDWIDTH[from], DOWNLOAD_BANDWIDTH[to]);
	}

	public static List<String> getRegionList() {
		return REGION_LIST;
	}

	public static double[] getRegionDistribution() {
		return REGION_DISTRIBUTION;
	}

	public static double[] getDegreeDistribution() {
		return DEGREE_DISTRIBUTION;
	}

	public static void printRegion(){
		STATIC_JSON_FILE.print("{\"region\":[");

		int id = 0;
		for(; id < REGION_LIST.size() -1; id++){
			STATIC_JSON_FILE.print("{");
			STATIC_JSON_FILE.print(		"\"id\":" + id + ",");
			STATIC_JSON_FILE.print(		"\"name\":\"" + REGION_LIST.get(id) + "\"");
			STATIC_JSON_FILE.print("},");
		}

		STATIC_JSON_FILE.print(	   "{");
		STATIC_JSON_FILE.print(			"\"id\":" + id + ",");
		STATIC_JSON_FILE.print(			"\"name\":\"" + REGION_LIST.get(id) + "\"");
		STATIC_JSON_FILE.print(    "}");
		STATIC_JSON_FILE.print("]}");
		STATIC_JSON_FILE.flush();
		STATIC_JSON_FILE.close();
	}
}
