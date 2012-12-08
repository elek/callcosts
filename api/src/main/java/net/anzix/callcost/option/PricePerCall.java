package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;

/**
 * @author elekma
 */
public class PricePerCall implements Option {

    int price;


    public PricePerCall(int price) {
        this.price = price;
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        CalculationResult res = new CalculationResult(result);
        for (CallRecord rec : list.getCalls()) {
            res.setEstimatedCost(rec, result.getEstimatedCost(rec) + price);
        }
        return res;
    }

    @Override
    public String getName() {
        return "Connection fee";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricePerCall that = (PricePerCall) o;

        if (price != that.price) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return price;
    }
}
