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

package simblock.task;

/**
 * The interface Task. A task can be run and has its execution duration.
 */
public interface Task {
  /**
   * Gets the execution duration of the task in milliseconds of simulated time.
   *
   * @return the execution duration of the task
   */
  // TODO this is not an interval this is a duration
  long getInterval();

  /**
   * Run the task.
   */
  void run();
}
