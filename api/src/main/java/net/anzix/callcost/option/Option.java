package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * Option to get additional discounts.
 *
 * @author elekma
 */
public interface Option {

    public CalculationResult getCost(CallList list, CalculationResult result);

    public String getName();

}
