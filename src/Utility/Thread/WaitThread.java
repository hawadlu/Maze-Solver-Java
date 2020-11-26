package Utility.Thread;

/**
 * This class handles the spinning animation
 */
public class WaitThread extends Thread {
  Thread waiting;

  public WaitThread(Thread waiting) {
    this.waiting = waiting;
  }


  @Override
  public void start() {
    //Start the waiting thread
    waiting.start();

    //Wait until the other thread is complete
    try {
      waiting.join();
      stop();
    } catch (Exception interrupt) {
      interrupt.printStackTrace();
    }
  }
}
