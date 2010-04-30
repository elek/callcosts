package net.anzix.callcost;

import java.util.List;

/**
 *
 * @author elek
 */
public interface Provider {

    public String getName();

    public List<Plan> getPlans();

    public List<NetPlan> getNetPlans();

}
