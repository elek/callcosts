package net.anzix.callcost.data;

import net.anzix.callcost.option.Option;
import net.anzix.callcost.Plan;

import java.util.*;

/**
 * Result of a cost calculation for a given plan.
 *
 * @author elekma
 */
public class CalculationResult {

    private Plan plan;

    private List<Option> options = new ArrayList<Option>();

    private int monthlyFee;

    private int unpaidNetUsage;

    private Map<Record, Integer> recordCosts = new HashMap<Record, Integer>();

    private Map<CallRecord, Integer> callBillableSecs = new HashMap<CallRecord, Integer>();

    private Map<Option, Integer> optionCosts = new HashMap<Option, Integer>();

    public CalculationResult(CalculationResult result) {
        this.plan = result.plan;
        this.options = new ArrayList<Option>(result.options);
        this.monthlyFee = result.monthlyFee;
        this.unpaidNetUsage = result.unpaidNetUsage;
        this.recordCosts = new HashMap<Record, Integer>(result.recordCosts);
        this.callBillableSecs = new HashMap<CallRecord, Integer>(result.callBillableSecs);
        this.optionCosts = new HashMap<Option, Integer>(result.optionCosts);
    }

    public CalculationResult(Plan plan, int cost) {
        this.plan = plan;
        this.monthlyFee = cost;
    }

    public CalculationResult(Plan callPlan) {
        this.plan = callPlan;
    }


    public Plan getPlan() {
        return plan;
    }

    public List<Option> getOptions() {
        return options;
    }

    public int getCost() {
        int res = this.monthlyFee;
        for (Record r : recordCosts.keySet()) {
            res += recordCosts.get(r);
        }
        for (Option o : optionCosts.keySet()) {
            res += optionCosts.get(o);
        }
        return res;
    }

    public int getMonthlyFee() {
        return monthlyFee;
    }

    public int getCost(Collection<? extends Record> records) {
        int cost = 0;
        for (Record r : records) {
            if (recordCosts.containsKey(r)) {
                cost += recordCosts.get(r);
            }
        }
        return cost;
    }

    public int getCost(Option o) {
        if (optionCosts.containsKey(o)) {
            return optionCosts.get(o);
        } else {
            return 0;
        }
    }

    public int getUnpaidNetUsage() {
        return unpaidNetUsage;
    }

    public void setUnpaidNetUsage(int unpaidNetUsage) {
        this.unpaidNetUsage = unpaidNetUsage;
    }

    public void setBillableSecs(CallRecord record, int sec) {
        callBillableSecs.put(record, sec);
    }

    public int getBillableSecs(CallRecord record) {
        return callBillableSecs.get(record);
    }

    public void setEstimatedCost(Record record, int cost) {
        recordCosts.put(record, cost);
    }

    public int getEstimatedCost(CallRecord record) {
        return recordCosts.get(record);
    }

    public void addOption(Option o) {
        options.add(o);
    }

    public boolean isOk() {
        return unpaidNetUsage <= 0;
    }

    public void addOption(Option option, int price) {
        addOption(option);
        optionCosts.put(option, price);
    }

    public void reduceUnpaidNet(int i) {
        unpaidNetUsage -= i;
        if (unpaidNetUsage < 0) {
            unpaidNetUsage = 0;
        }
    }
}
