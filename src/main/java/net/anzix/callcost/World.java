/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import net.anzix.callcost.hu.Hungary;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import net.anzix.callcost.in.India;

/**
 *
 * @author elek
 */
public class World {

    private static World instance;

    private Map<String, Class<? extends Country>> countries = new HashMap();

    static Country c;

    private String current;

    private Country currentCountry;

    public World(String country) {
        countries.put("hu", Hungary.class);
        countries.put("in", India.class);
        current = country;
    }

    public static void init(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String countryCode = sp.getString("country", "hu");
        Log.i("callcost","country "+countryCode);
        instance = new World(countryCode);
        instance.changeCountry(countryCode);
    }

    public static World instance() {
        return instance;
    }

    public void changeCountry(String current) {
        try {
            this.current = current;
            this.currentCountry = countries.get(current).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("No such country " + current, ex);
        }
    }

    public Country getCurrentCountry() {
        return currentCountry;

    }
}
