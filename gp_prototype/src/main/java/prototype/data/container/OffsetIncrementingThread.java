package prototype.data.container;

import org.apache.log4j.Logger;

/**
 * User: koperek
 * Date: 29.03.13
 * Time: 22:47
 */
public class OffsetIncrementingThread extends Thread {

    private final static Logger logger = Logger.getLogger(OffsetIncrementingThread.class);
    private final OffsetDataContainer offsetDataContainer;
    private final int sleepTime;

    public OffsetIncrementingThread(OffsetDataContainer offsetDataContainer, int sleepTime) {
        this.offsetDataContainer = offsetDataContainer;
        this.sleepTime = sleepTime;
        this.setName("Offset Incrementing Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        boolean canIncrement = true;
        while (canIncrement) {
            try {
                Thread.sleep(sleepTime);
                offsetDataContainer.incrementOffset();
            } catch (InterruptedException e) {
                logger.error("Problem with incrementing!", e);
            }
        }
    }

}
