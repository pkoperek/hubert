package prototype.data.container;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;

public class DataContainerConfiguration {
    private String inputFileName;
    private String timeVariable;
    private boolean implicitTime;
    private boolean timedData;
    private boolean movingWindow;
    private int movingWindowSize;
    private int movingWindowInterval;

    @Configure
    public void setMovingWindowInterval(@Configuration(value = "data.moving.window.interval", defaultValue = "60000") int movingWindowInterval) {
        this.movingWindowInterval = movingWindowInterval;
    }

    @Configure
    public void setMovingWindowSize(@Configuration(value = "data.moving.window.size", defaultValue = "30") int movingWindowSize) {
        this.movingWindowSize = movingWindowSize;
    }

    @Configure
    public void setMovingWindow(@Configuration(value = "data.moving.window.enabled", defaultValue = "false") boolean movingWindow) {
        this.movingWindow = movingWindow;
    }

    @Configure
    public void setInputFileName(@Configuration(value = "data.file") String inputFileName) {
        this.inputFileName = inputFileName;
    }

    @Configure
    public void setTimeVariable(@Configuration(value = "data.time.variable", required = false) String timeVariable) {
        this.timeVariable = timeVariable;
    }

    @Configure
    public void setImplicitTime(@Configuration(value = "data.time.implicit", defaultValue = "false") boolean implicitTime) {
        this.implicitTime = implicitTime;
    }

    @Configure
    public void setTimedData(@Configuration(value = "data.time", defaultValue = "true") boolean timedData) {
        this.timedData = timedData;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getTimeVariable() {
        return timeVariable;
    }

    public boolean isImplicitTime() {
        return implicitTime;
    }

    public boolean isTimedData() {
        return timedData;
    }

    public boolean isMovingWindow() {
        return movingWindow;
    }

    public int getMovingWindowSize() {
        return movingWindowSize;
    }

    public int getMovingWindowInterval() {
        return movingWindowInterval;
    }
}