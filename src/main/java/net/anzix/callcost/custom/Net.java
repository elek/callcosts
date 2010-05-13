/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.custom;

import java.util.HashMap;
import java.util.Map;
import net.anzix.callcost.api.NetPlan;

/**
 *
 * @author elek
 */
public class Net implements NetPlan, PropertyContainer {

    private Map<String, Object> properties = new HashMap();

    private String name;

    private String id;

    public Net(String id, String value) {
        this.id = id;
        this.name = value;
    }

    public boolean isPrepaid() {
        String prepaid = (String) properties.get("prepaid");
        return prepaid != null && prepaid.equalsIgnoreCase("true");
    }

    public String getName() {
        return name;
    }

    public int getCosts(int mb) {
        int cost = intParam("monthlyFee", 0);

        int included = intParam("includedMb", 0);

        int overCost = intParam("pricePerMb", 0);

        if (mb < included) {
            return cost;
        } else {
            if (overCost > 0) {
                return (mb - included) * overCost + cost;
            } else {
                return -1;
            }
        }
    }

    public Map<String, Object> getProperties() {
        return properties;
    }


    private int intParam(String string, int def) {
        String val = (String) properties.get(string);
        if (val == null) {
            return def;
        }
        return Integer.parseInt(val);
    }
}
