/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Top level container object contains all of country/mobile operator/plan data.
 *
 * @author elek
 */
public class World {

    public static final int PRECISION = 100;

    private Map<Integer, Country> countries = new HashMap();

    public World() {
    }

    public static int toInternal(int amount) {
        return amount * PRECISION;
    }

    public void addCountry(Country country) {
        countries.put(country.getId(), country);

    }

    public Country getCountry(int id) {
        return countries.get(id);
    }


    public Collection<Country> getCountries() {
        return countries.values();
    }
}
