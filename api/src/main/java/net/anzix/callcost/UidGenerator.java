package net.anzix.callcost;

import java.util.HashMap;
import java.util.Map;

/**
 * Generate unique IDs for countries/operators/plans.
 */
public class UidGenerator {

    private Map<Integer, String> providerCodeToName = new HashMap<Integer, String>();
    private Map<String, Integer> providerNameToCode = new HashMap<String, Integer>();

    int customInterval = 10;
    int planId = 1;

    private static UidGenerator instance = new UidGenerator();

    public void reset() {
        customInterval = 10;
        planId = 1;
    }

    public int createProviderUid(int countryCode, String name) {
        int code = customInterval++ + countryCode * 256;
        providerCodeToName.put(code, name);
        providerNameToCode.put(name, code);
        return code;
    }

    public int getProviderCode(String name) {
        if (providerNameToCode.containsKey(name)) {
            return providerNameToCode.get(name);
        }
        Provider p = Provider.valueOf(name);
        return p.ordinal();
    }

    public String getProviderName(int code) {
        if (Provider.values().length > code) {
            return Provider.values()[code].name();
        } else if (providerCodeToName.containsKey(code)) {
            return providerCodeToName.get(code);
        } else {
            throw new IllegalArgumentException("Unknown provider code " + code);
        }
    }

    public boolean doesProviderExist(String name) {
        return providerNameToCode.containsKey(name);
    }

    /**
     * @param country
     * @return
     * @todo handle all of the other countries as well.
     */
    public int createCountryUid(String country) {
        return getContryCode(country);
    }

    public int createPlanUid(int id, int id1, String name) {
        return id * 256 * 256 + id1 * 256 + planId++;
    }

    public int getContryCode(String country) {
        if (country.equalsIgnoreCase("Hungary")) {
            return 1;
        } else {
            throw new UnsupportedOperationException("Country " + country + " is unsupported");
        }
    }


    public static enum Provider {
        UNUSED, UNKNOWN, EXCLUDED, MYGROUP
    }

    public static UidGenerator getInstance() {
        return instance;
    }
}
