package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * @author elekma
 */
public class NoOption implements Option {

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        return result;
    }

    @Override
    public String getName() {
        return "NO";
    }
}
