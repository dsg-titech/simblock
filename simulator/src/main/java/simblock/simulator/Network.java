/*
 * Copyright 2019 Distributed Systems Group
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simblock.simulator;

import static simblock.settings.NetworkConfiguration.DEGREE_DISTRIBUTION;
import static simblock.settings.NetworkConfiguration.DOWNLOAD_BANDWIDTH;
import static simblock.settings.NetworkConfiguration.LATENCY;
import static simblock.settings.NetworkConfiguration.REGION_DISTRIBUTION;
import static simblock.settings.NetworkConfiguration.REGION_LIST;
import static simblock.settings.NetworkConfiguration.UPLOAD_BANDWIDTH;
import static simblock.simulator.Main.STATIC_JSON_FILE;
import static simblock.simulator.Main.random;

import java.util.List;
import simblock.settings.NetworkConfiguration;

/**
 * The type Network represents a network split in regions, each node belonging to a region with
 * an upload bandwidth
 * and a download bandwidth. Node degrees follow a predefined degree distribution.
 */
// TODO how is this degree distribution calculated and what does the double array mean
public class Network {

  /**
   * Gets latency according with 20% variance pallet distribution.
   *
   * @param from the from latency
   * @param to   the to latency
   * @return the calculated latency
   */
  public static final long getLatency(int from, int to) {
    long mean = LATENCY[from][to];
    double shape = 0.2 * mean;
    double scale = mean - 5;
    return Math.round(scale / Math.pow(random.nextDouble(), 1.0 / shape));
  }

  /**
   * Gets the minimum between the <em>from</em> upload bandwidth and <em>to</em> download
   * bandwidth.
   *
   * @param from the from index in the {@link NetworkConfiguration#UPLOAD_BANDWIDTH} array.
   * @param to   the to index in the {@link NetworkConfiguration#UPLOAD_BANDWIDTH} array.
   * @return the bandwidth
   */

  public static final long getBandwidth(int from, int to) {
    return Math.min(UPLOAD_BANDWIDTH[from], DOWNLOAD_BANDWIDTH[to]);
  }

  /**
   * Gets region list.
   *
   * @return the {@link NetworkConfiguration#REGION_LIST} list.
   */
  public static List<String> getRegionList() {
    return REGION_LIST;
  }

  /**
   * Return the number of nodes in the corresponding region as a portion the number of all nodes.
   *
   * @return an array the distribution
   */
  public static double[] getRegionDistribution() {
    return REGION_DISTRIBUTION;
  }

  /**
   * Get degree distribution double [ ].
   *
   * @return the double [ ]
   */
  //TODO
  public static double[] getDegreeDistribution() {
    return DEGREE_DISTRIBUTION;
  }

  /**
   * Prints the currently active regions to outfile.
   */
  //TODO
  public static void printRegion() {
    STATIC_JSON_FILE.print("{\"region\":[");

    int id = 0;
    for (; id < REGION_LIST.size() - 1; id++) {
      STATIC_JSON_FILE.print("{");
      STATIC_JSON_FILE.print("\"id\":" + id + ",");
      STATIC_JSON_FILE.print("\"name\":\"" + REGION_LIST.get(id) + "\"");
      STATIC_JSON_FILE.print("},");
    }

    STATIC_JSON_FILE.print("{");
    STATIC_JSON_FILE.print("\"id\":" + id + ",");
    STATIC_JSON_FILE.print("\"name\":\"" + REGION_LIST.get(id) + "\"");
    STATIC_JSON_FILE.print("}");
    STATIC_JSON_FILE.print("]}");
    STATIC_JSON_FILE.flush();
    STATIC_JSON_FILE.close();
  }
}
