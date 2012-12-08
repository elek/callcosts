package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * @author elekma
 */
public class OrOption implements Option {

    public Option one;
    public Option two;

    public OrOption() {
    }

    public OrOption(Option one, Option two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        CalculationResult r1 = one.getCost(list, result);
        CalculationResult r2 = two.getCost(list, result);
        if (r1.getUnpaidNetUsage() == 0 && r2.getUnpaidNetUsage() == 0) {
            if (r1.getCost() < r2.getCost()) {
                return r1;
            } else {
                return r2;
            }
        } else if (r1.getUnpaidNetUsage() > 0) {
            return r2;
        } else {
            return r1;
        }
    }

    @Override
    public String getName() {
        return "OR";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrOption orOption = (OrOption) o;

        if (one != null ? !one.equals(orOption.one) : orOption.one != null) return false;
        if (two != null ? !two.equals(orOption.two) : orOption.two != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = one != null ? one.hashCode() : 0;
        result = 31 * result + (two != null ? two.hashCode() : 0);
        return result;
    }
}
