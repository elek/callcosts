package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.SMSRecord;

/**
 * @author elekma
 */
public class PricePerSms implements Option {

    int price;


    public PricePerSms(int price) {
        this.price = price;
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        CalculationResult res = new CalculationResult(result);
        for (SMSRecord r : list.getSMSs()){
            res.setEstimatedCost(r,price);
        }
        return res;
    }

    @Override
    public String getName() {
        return "Price per sms";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricePerSms that = (PricePerSms) o;

        if (price != that.price) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return price;
    }
}
