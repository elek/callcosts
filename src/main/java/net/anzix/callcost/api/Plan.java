package net.anzix.callcost.api;

import java.util.Calendar;

/**
 *
 * @author elek
 */
public interface Plan {

    public boolean isPrepaid();

    public String getId();

    public String getName();

    public int getCost(CallList list);

    public int addSMS(Calendar time, String to);

    public int getIncludedNet();

    public void setProvider(Provider provider);

    public Provider getProvider();
}
