package hubert.evolution.engine;

import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 21.03.13
 * Time: 19:14
 */
public interface Tournament {
    IGPProgram[] getWinners(IGPProgram[] parents, IGPProgram[] children);
}
