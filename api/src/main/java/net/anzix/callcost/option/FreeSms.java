package net.anzix.callcost.option;

import net.anzix.callcost.Tools;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.SMSRecord;

/**
 * @author elekma
 */
public class FreeSms implements Option {

    int freeSms;

    private String name;

    private String destination;

    public FreeSms(Integer freeSms) {
        this.freeSms = freeSms * 60;
    }

    public FreeSms() {
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult origResult) {
        CalculationResult result = new CalculationResult(origResult);
        int smsNo = freeSms;
        for (SMSRecord sms : list.getSMSs()){
            if (smsNo>0){
                smsNo--;
                result.setEstimatedCost(sms,0);
            }
        }
        return result;
    }


    public String getName() {
        return Tools.resolve(name, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FreeSms freeSms1 = (FreeSms) o;

        if (freeSms != freeSms1.freeSms) return false;
        if (destination != null ? !destination.equals(freeSms1.destination) : freeSms1.destination != null)
            return false;
        if (name != null ? !name.equals(freeSms1.name) : freeSms1.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = freeSms;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }
}
