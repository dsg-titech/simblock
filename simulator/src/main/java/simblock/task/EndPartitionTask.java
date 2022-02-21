package simblock.task;

import simblock.simulator.Network;

/**
 * The type EndPartition task.
 */
public class EndPartitionTask implements Task {

  /**
   * Instantiates a new EndPartition task.
   *
   */
  public EndPartitionTask() {
  }

  public long getInterval() {
    // Only used as an absolute time task, this value does not matter
    return -1;
  }

  public void run() {
    // Instruct the Network class to resume using old bandwidth matrices
     Network.partitioned = false;
  }
}