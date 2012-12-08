package net.anzix.callcost;

import net.anzix.callcost.option.Option;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.data.SMSRecord;

/**
 * Class represents a mobile plan.
 *
 * @author elek
 */
public interface Plan {

    public boolean isPrepaid();

    public int getId();

    public String getName();

    public CalculationResult calculateCost(CallList list);

    public CalculationResult recaculateCost(CallList list, CalculationResult res, CallRecord[] calls, SMSRecord[] smss, boolean net);

    public void setProvider(Provider provider);

    public Provider getProvider();

    public Option getOptions();


}
