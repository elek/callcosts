package net.anzix.callcost.api;

import java.util.Collection;

/**
 *
 * @author elek
 */
public interface Provider {

    public String getName();

    public Collection<Plan> getPlans();

    public Collection<NetPlan> getNetPlans();

    public void addPlan(Plan plan);

    public Plan getPlan(String gid);

    public String getId();
}
