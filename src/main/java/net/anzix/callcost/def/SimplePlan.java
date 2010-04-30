/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.def;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.anzix.callcost.AbstractPlan;
import net.anzix.callcost.Util;

/**
 *
 * @author elek
 */
public class SimplePlan extends AbstractPlan {

    private int includedMinutes = 0;

    private int unit = 60;

    private int smsPrice = 0;

    private List<CostCondition> conds = new ArrayList();

    private String name;

    private boolean prepaid = false;

    private int includedNet;

    public SimplePlan() {
    }

    public void addCond(CostCondition cond) {
        conds.add(cond);
    }

    public SimplePlan(String name, int monthlyCost, int smsPrice, int overCost, int includedMinutes) {
        this.includedMinutes = includedMinutes;
        setMonthlyCost(monthlyCost);
        this.smsPrice = smsPrice;
        conds.add(new CostCondition(overCost));
        this.name = name;
    }

    public int call(Calendar time, String to, int durationSec) {
        int min = Util.minuteBased(durationSec);
        int dura = durationSec;
        if (includedMinutes > 0) {
            includedMinutes -= min;
            if (includedMinutes < 0) {
                dura = includedMinutes - 1;
            }
        }
        if (dura > 0) {
            for (CostCondition cond : conds) {
                if (cond.match(time, to)) {
                    return cond.getCost() * min;

                }
            }
        }
        return 0;
    }

    @Override
    public int sms(Calendar time, String to) {
        return smsPrice;
    }

    public String getName() {
        return name;
    }

    public boolean isPrepaid() {
        return prepaid;
    }

    public SimplePlan setIncludedNet(int i) {
        this.includedNet = i;
        return this;
    }

    public SimplePlan setPrepaid(boolean prepaid) {
        this.prepaid = prepaid;
        return this;
    }

    @Override
    public int getIncludedNet() {
        return includedNet;
    }

  
}
