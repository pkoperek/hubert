package hubert.evolution.engine.dc;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 15:25
 */
public abstract class LoggedCallable implements Callable<Void> {
    private static final Logger logger = Logger.getLogger(LoggedCallable.class);

    @Override
    public Void call() throws Exception {
        try {
            callLogged();
        } catch (Exception e) {
            logger.error("Problem executing callable task!", e);
        }

        return null;
    }

    protected abstract void callLogged();
}
