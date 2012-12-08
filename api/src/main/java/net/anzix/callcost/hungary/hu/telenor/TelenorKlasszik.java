package net.anzix.callcost.hungary.hu.telenor;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.option.Option;
import net.anzix.callcost.rulefile.MoneyConverter;

/**
 * @author elekma
 */
public class TelenorKlasszik implements Option {

    private final int internRatio;
    private final int externRatio;
    private final int firstMinFee;
    private final int afterMinFee;
    private final int connectionFee;

    public TelenorKlasszik(int internRatio, int externRatio, String firstMinFee, String afterMinFee) {
        MoneyConverter conv = new MoneyConverter();
        this.internRatio = internRatio;
        this.externRatio = externRatio;
        this.firstMinFee = conv.convert(firstMinFee);
        this.afterMinFee = conv.convert(afterMinFee);
        connectionFee = conv.convert("2.5");


    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        CalculationResult res = new CalculationResult(result);
        ValueHolder internLimit = new ValueHolder(res.getCost() * internRatio / 100);
        ValueHolder externLimit = new ValueHolder(res.getCost() * externRatio / 100);
        int allCost = res.getCost();
        for (CallRecord rec : list.getCalls()) {
//            if (rec.getProvider().equals("TELENOR")) {
//                allCost += calculate(internLimit, res, rec);
//            } else {
//                allCost += calculate(externLimit, res, rec);
//            }

        }
        return res;
    }

    private int calculate(ValueHolder internLimit, CalculationResult res, CallRecord rec) {
        int duration = res.getBillableSecs(rec);
        if (internLimit.value > 0) {
            int cost = duration / 60 * firstMinFee;
            if (internLimit.value > cost) {
                internLimit.value -= cost;
                res.setEstimatedCost(rec, 0);
                return 0;
            } else {
                cost -= internLimit.value;
                internLimit.value = 0;
                int remainingSec = cost * 60 / firstMinFee;
                res.setEstimatedCost(rec, remainingSec * afterMinFee / 60);
                return remainingSec * afterMinFee / 60;
            }
        } else {
            res.setEstimatedCost(rec, duration * afterMinFee / 60);
            return duration * afterMinFee / 60;
        }
    }

    @Override
    public String getName() {
        return null;
    }

    private static class ValueHolder {
        public Integer value;

        private ValueHolder(Integer value) {
            this.value = value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TelenorKlasszik that = (TelenorKlasszik) o;

        if (afterMinFee != that.afterMinFee) return false;
        if (externRatio != that.externRatio) return false;
        if (firstMinFee != that.firstMinFee) return false;
        if (internRatio != that.internRatio) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = internRatio;
        result = 31 * result + externRatio;
        result = 31 * result + firstMinFee;
        result = 31 * result + afterMinFee;
        return result;
    }
}
