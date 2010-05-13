/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.DestinationTypeDetector;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;
import net.anzix.callcost.api.Tools;

/**
 *
 * @author elek
 */
public class CustomCountry implements Country, PropertyContainer {

    private String id;

    private String name;

    private Map<String, Provider> map = new HashMap();

    private Map<String, Object> properties = new HashMap();

    private DestinationTypeDetector dtd;

    public CustomCountry(String key, String value) {
        this.id = key;
        this.name = value;
    }

    public Collection<Provider> getProviders() {
        return map.values();
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return (String) properties.get("currency");
    }

    public DestinationTypeDetector getNumberParser() {
        if (dtd == null) {
            dtd = new PatternTypeDetector((Map) properties.get("destinations"));
        }
        return dtd;

    }

    public Collection<Plan> getAllPlans() {
        List<Plan> all = new ArrayList();
        for (Provider provider : map.values()) {
            all.addAll(provider.getPlans());
        }
        return all;
    }

    public Plan getPlan(String id) {
        String pkey = id.substring(0, id.lastIndexOf("."));
        Provider p = map.get(pkey);
        if (p != null) {
            return p.getPlan(id);
        } else {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public Provider getProvider(String id) {
        return map.get(id);
    }

    public void load() {
    }

    public void addProvider(Provider p) {
        map.put(p.getId(), p);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public int getDivider() {
        return Tools.getInt(properties,"divider",0);
    }
}
