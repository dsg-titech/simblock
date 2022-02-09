package simblock.task;

import simblock.simulator.Network;

/**
 * The type Partition task.
 */
public class PartitionTask implements Task {

  /**
   * Instantiates a new Partition task.
   *
   */
  public PartitionTask() {
  }

  public long getInterval() {
    // Only used as an absolute time task, this value does not matter
    return -1;
  }

  public void run() {
    // Instruct the Network class to start using new bandwidth matrices
     Network.partitioned = true;
  }
}