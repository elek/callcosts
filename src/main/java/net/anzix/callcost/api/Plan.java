package net.anzix.callcost.api;

import net.anzix.callcost.data.CallList;
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

    public int getSMSCost(CallList list);

    public int getNetCost(int usage);

    public int getIncludedNet();

    public void setProvider(Provider provider);

    public Provider getProvider();
}
