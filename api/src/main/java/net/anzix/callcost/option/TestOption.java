package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * @author elekma
 */
public class TestOption implements Option {
    private String param;
    private int num = 1;

    public TestOption(String param, int a) {
        this.param = param;
        this.num = a;
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        return result;
    }

    @Override
    public String getName() {
        return "testOption " + param + " " + num;
    }
}
