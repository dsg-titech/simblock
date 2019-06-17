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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Setting Network latency and bandwith
public class NetworkConfiguration {

	public static final List<String> REGION_LIST = new ArrayList<>(Arrays.asList("NORTH_AMERICA", "EUROPE", "SOUTH_AMERICA", "ASIA_PACIFIC", "JAPAN", "AUSTRALIA"));

	// LATENCY[i][j] is average latency from REGION_LIST[i] to REGION_LIST[j]
	public static final long[][] LATENCY = {
			{36, 119, 255, 310, 154, 208},
            {119, 12, 221, 242, 266, 350},
            {255, 221, 137, 347, 256, 269},
            {310, 242, 347, 99, 172, 278},
            {154, 266, 256, 172, 9, 163},
            {208, 350, 269, 278, 163, 22}};

	// Download bandwidth in each region, and last element is Inter-regional bandwidth
	public static final long[] DOWNLOAD_BANDWIDTH = {25000000, 24000000, 6500000, 10000000, 17500000, 14000000, 6 * 1000000};

	// Upload bandwidth in each region, and last element is Inter-regional bandwidth
	public static final long[] UPLOAD_BANDWIDTH =  { 4700000,  8100000, 1800000,  5300000,  3400000,  5200000, 6 * 1000000};

	public static final double[] REGION_DISTRIBUTION = {0.3869,0.5159,0.0113,0.0574,0.0119,0.0166};
	//double[] REGION_DISTRIBUTION = {0.3661, 0.4791, 0.0149, 0.1022, 0.0238, 0.0139};lite
	//double[] REGION_DISTRIBUTION = {0.3924, 0.4879, 0.0212, 0.0697, 0.0106, 0.0182};  //doge

	public static final double[] DEGREE_DISTRIBUTION = {0.025,0.050,0.075,0.10,0.20,0.30,0.40,0.50,0.60,0.70,0.80,0.85,0.90,0.95,0.97,0.97,0.98,0.99,0.995,1.0};
	//double[] DEGREE_DISTRIBUTION = {0.01,0.02,0.04,0.07,0.09,0.14,0.20,0.28,0.39,0.5,0.6,0.69,0.76,0.81,0.85,0.87,0.89,0.92,0.93,1.0};
	//double DEGREE_DISTRIBUTION[] = {0.00,0.00,0.00,0.00,0.00,0.00,0.00,1.0,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.0};
}