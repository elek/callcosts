package net.anzix.callcost.rulefile;

import net.anzix.callcost.World;
import net.anzix.callcost.Converter;

/**
 * Convert money value to the internal representation.
 *
 * @author elekma
 */
public class MoneyConverter implements Converter<Integer> {

    @Override
    public Integer convert(String value) {
        return (int) (Double.parseDouble(value) * World.PRECISION);
    }
}
