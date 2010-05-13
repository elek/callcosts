/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elek
 */
public abstract class AbstractProvider implements Provider {

    private List<Plan> plans = new ArrayList<Plan>();

    private List<NetPlan> netplans = new ArrayList<NetPlan>();

    public List<Plan> getPlans() {
        return plans;
    }

    public List<NetPlan> getNetPlans() {
        return netplans;
    }

    public void addPlan(AbstractPlan plan) {
        plans.add(plan);
        plan.setProvider(this);
    }

    public void addNetPlan(AbstractNetPlan plan) {
        netplans.add(plan);
    }
}
