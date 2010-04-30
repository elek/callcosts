package net.anzix.callcost;

import java.util.Calendar;

/**
 *
 * @author elek
 */
public interface Plan {

    public boolean isPrepaid();

    public String getName();

    public int addCall(Calendar time, String to, int durationSec);

    public int addSMS(Calendar time, String to);

    public int getCosts();

    public int getIncludedNet();

    public Provider getProvider();

    public void reset();

    public String getId();
}
