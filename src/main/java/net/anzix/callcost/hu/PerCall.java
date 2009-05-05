/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.hu;

import java.util.Calendar;
import net.anzix.callcost.AbstractPlan;

/**
 *
 * @author elek
 */
public class PerCall extends AbstractPlan {

    private int smsCost;

    private int callCost;

    private int callLimit;

    private int currentCall;

    private String name;

    public PerCall(String name, int mothlyFee, int callLimit, int callCost, int smsCost) {
        this.name = name;
        setMonthlyCost(mothlyFee);
        this.smsCost = smsCost;
        this.callCost = callCost;
        this.callLimit = callLimit;
    }

    public int call(Calendar time, String to, int durationSec) {
        if (currentCall > 0) {
            currentCall--;
            return 0;
        } else {
            return callCost;
        }
    }

    public int sms(Calendar time, String to) {
        return smsCost;
    }

    @Override
    public void reset() {
        super.reset();
        currentCall = callLimit;
    }

    public boolean isPrepaid() {
        return false;
    }

    public String getName() {
        return name;
    }
}
