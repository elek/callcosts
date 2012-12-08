package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * @author elekma
 */
public class AndOption implements Option {

    public Option one;
    public Option two;

    public AndOption() {
    }

    public AndOption(Option one, Option two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        CalculationResult finalResult = result;


        CalculationResult r1 = one.getCost(list, result);

        if ((!finalResult.isOk() && r1.isOk()) || (finalResult.getCost() > r1.getCost())) {
            finalResult = r1;
        }

        CalculationResult r2 = two.getCost(list, finalResult);

        if ((!finalResult.isOk() && r2.isOk()) || (finalResult.getCost() > r2.getCost())) {
            finalResult = r2;
        }


        return finalResult;
    }

    @Override
    public String getName() {
        return "AND";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AndOption andOption = (AndOption) o;

        if (one != null ? !one.equals(andOption.one) : andOption.one != null) return false;
        if (two != null ? !two.equals(andOption.two) : andOption.two != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = one != null ? one.hashCode() : 0;
        result = 31 * result + (two != null ? two.hashCode() : 0);
        return result;
    }
}
