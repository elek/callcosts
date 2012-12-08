package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;

/**
 * @author elekma
 */
public class FreeCalls implements Option {

    int freeCalls;

    public FreeCalls(int freeCalls) {
        this.freeCalls = freeCalls;
    }

    public FreeCalls() {

    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult origResult) {
        CalculationResult result = new CalculationResult(origResult);
        int m = freeCalls;
        for (CallRecord record : list.getCalls()) {
            if (m > 0) {
                result.setBillableSecs(record, 0);
                result.setEstimatedCost(record, 0);
                m--;
            } else {
                break;
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return "Free calls";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreeCalls freeCalls1 = (FreeCalls) o;

        if (freeCalls != freeCalls1.freeCalls) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return freeCalls;
    }
}
