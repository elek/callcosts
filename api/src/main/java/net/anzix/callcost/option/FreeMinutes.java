package net.anzix.callcost.option;

import net.anzix.callcost.Tools;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.data.SMSRecord;

/**
 * @author elekma
 */
public class FreeMinutes implements Option {

    int freeSecs;

    private String name;

    private String destination;

    public FreeMinutes(Integer freeMinutes) {
        this.freeSecs = freeMinutes * 60;
    }

    public FreeMinutes() {
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult origResult) {
        CalculationResult result = new CalculationResult(origResult);
        result.addOption(this);
        int m = freeSecs;
        int costImprovements = 0;
        for (CallRecord record : list.getCalls()) {
            if (m > 0) {
                m -= result.getBillableSecs(record);
                if (m < 0) {
                    result.setBillableSecs(record, m * -1);
                    int costBefore = result.getEstimatedCost(record);
                    result = result.getPlan().recaculateCost(list, result,
                            new CallRecord[]{record}, new SMSRecord[0], false);
                    costImprovements += costBefore - result.getEstimatedCost(record);
                } else {
                    costImprovements += origResult.getEstimatedCost(record);
                    result.setBillableSecs(record, 0);
                    result.setEstimatedCost(record, 0);
                }
            }
        }
        return result;
    }

    public void setFreeMins(int mins) {
        this.freeSecs = mins * 60;
    }

    public String getName() {
        return Tools.resolve(name, this);
    }

    public int getFreeMins() {
        return freeSecs / 60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreeMinutes that = (FreeMinutes) o;

        if (freeSecs != that.freeSecs) return false;
        if (destination != null ? !destination.equals(that.destination) : that.destination != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = freeSecs;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }
}
