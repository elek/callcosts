/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import java.util.*;

/**
 * Class represents a countr.
 *
 * @author elek
 */
public class Country {

    private int id;

    private String name;

    private Map<Integer, Provider> map = new HashMap();

    private String currency;

    private int divider;

    private PatternTypeDetector dtd = new PatternTypeDetector();

    public Country() {
    }

    public Country(int id) {
        this.id = id;
    }

    public Collection<Provider> getProviders() {
        return map.values();
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public DestinationTypeDetector getNumberParser() {
        return dtd;

    }


    public Plan findPlan(String id) {
        for (Plan p : getAllPlans()) {
            if (p.getName().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Collection<Plan> getAllPlans() {
        List<Plan> all = new ArrayList();
        for (Provider provider : map.values()) {
            all.addAll(provider.getPlans());
        }
        return all;
    }


    public int getId() {
        return id;
    }

    public Provider getProvider(int id) {
        return map.get(id);
    }

    public void load() {
    }

    public void addProvider(Provider p) {
        map.put(p.getId(), p);
    }

    public int getDivider() {
        return divider;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Provider findProvider(String s) {
        for (Provider p : getProviders()) {
            if (p.getName().equalsIgnoreCase(s)) {
                return p;
            }
        }
        return null;
    }

    public void addDestinations(PatternTypeDetector.Pattern pattern) {
        dtd.addPattern(pattern);
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDtd(PatternTypeDetector dtd) {
        this.dtd = dtd;
    }

    public Plan getPlan(int planid) {
        for (Plan p : getAllPlans()) {
            if (p.getId() == planid) {
                return p;
            }
        }
        return null;
    }
}
