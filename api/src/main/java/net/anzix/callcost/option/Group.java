package net.anzix.callcost.option;

import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * @author elekma
 */
public class Group implements Option {

    private String name;

    private Option options;

    @Override
    public CalculationResult getCost(CallList list, CalculationResult result) {
        return options.getCost(list, result);
    }

    @Override
    public String getName() {
        return "Option group";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (options != null ? !options.equals(group.options) : group.options != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return options != null ? options.hashCode() : 0;
    }
}
