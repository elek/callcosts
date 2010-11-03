/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;
import net.anzix.callcost.def.CostCondition;

/**
 *
 * @author elek
 */
public abstract class AbstractPlan implements Plan {

    private int currentCost = 0;

    private int monthlyCost = 0;

    private Provider provider;

    private List<CostCondition> conds = new ArrayList();

    public int addCall(Calendar time, String to, int durationSec) {
        int call = call(time, to, durationSec);
        currentCost += call;
        return call;

    }

    public int addSMS(Calendar time, String to) {
        int sms = sms(time, to);
        currentCost += sms;
        return sms;
    }

    public abstract int sms(Calendar time, String to);

    public abstract int call(Calendar time, String to, int durationSec);

    public int getCosts() {
        return currentCost;
    }

    public int getIncludedNet() {
        return 0;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void reset() {
        currentCost = monthlyCost;
    }

    public void setMonthlyCost(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }

    public String getId() {
        return getProvider().getName() + "--" + getName();
    }
}
