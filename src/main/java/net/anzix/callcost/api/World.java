/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elek
 */
public class World {

    private static World instance;

    private Map<String, Country> countries = new HashMap();

    static Country c;

    private Country currentCountry;

    public World() {
    }

    public void addCountry(Country country) {
        countries.put(country.getId(), country);

    }

    public Country getCountry(String id) {
        return countries.get(id);
    }

    public static void init(World word, String defaultCountry) {
        instance = word;
        instance.changeCountry(defaultCountry);

    }

//    public static void init(Context context) {
//        try {
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//            String countryCode = sp.getString("country", "hu");
//
//            instance = new World();
//            CustomLoader loader = new CustomLoader();
//            InputStream st = context.getResources().openRawResource(R.raw.hungary);
//            loader.read(instance, st);
//            st.close();
//            instance.changeCountry(countryCode);
//        } catch (IOException ex) {
//            Log.e("callcost", "ERROR on loading");
//        }
//    }
    public static World instance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    public void changeCountry(String current) {
        try {
            this.currentCountry = countries.get(current);
            this.currentCountry.load();
        } catch (Exception ex) {
            throw new IllegalArgumentException("No such country " + current, ex);
        }
    }

    public Country getCurrentCountry() {
        return currentCountry;

    }

    public Collection<Country> getCountries() {
        return countries.values();
    }
}
