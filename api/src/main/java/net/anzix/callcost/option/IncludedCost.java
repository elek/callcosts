package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;

/**
 * @author elekma
 */
public class IncludedCost implements Option {

    private int includedCost;

    public IncludedCost(int includedCost) {
        this.includedCost = includedCost;
    }


    @Override
    public CalculationResult getCost(CallList list, CalculationResult origResult) {
        CalculationResult result = new CalculationResult(origResult);
        int m = includedCost;
        for (CallRecord record : list.getCalls()) {
            if (m > 0) {
                m -= result.getEstimatedCost(record);
                if (m < 0) {
                    result.setEstimatedCost(record, m * -1);
                } else {
                    result.setBillableSecs(record, 0);
                    result.setEstimatedCost(record, 0);
                }
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return "Included cost";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncludedCost that = (IncludedCost) o;

        if (includedCost != that.includedCost) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return includedCost;
    }
}
