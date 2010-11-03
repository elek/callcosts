/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.data;

import java.util.ArrayList;
import java.util.List;
import net.anzix.callcost.api.NetPlan;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;

/**
 *
 * @author elek
 */
public class CalculationResult {

    public Provider provider;

    public Plan plan;

    public NetPlan netPlan;

    public int callCost;

    public int smsCost;

    public int netCost;

    /**
     * Costs of original CallList items.
     */
    public List<Integer> costs = new ArrayList();

    public Integer getAllCosts() {
        return callCost + smsCost + netCost;
    }

    public int getCallCost() {
        return callCost;
    }

    public void setCallCost(int callCost) {
        this.callCost = callCost;
    }

    public List<Integer> getCosts() {
        return costs;
    }

    public void setCosts(List<Integer> costs) {
        this.costs = costs;
    }

    public int getNetCost() {
        return netCost;
    }

    public void setNetCost(int netCost) {
        this.netCost = netCost;
    }

    public NetPlan getNetPlan() {
        return netPlan;
    }

    public void setNetPlan(NetPlan netPlan) {
        this.netPlan = netPlan;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getSmsCost() {
        return smsCost;
    }

    public void setSmsCost(int smsCost) {
        this.smsCost = smsCost;
    }
}
