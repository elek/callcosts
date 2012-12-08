package net.anzix.callcost;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an operator in one country.
 *
 * @author elek
 */
public class Provider {

    private int id;

    private String name;

    private Map<String, Object> properties = new HashMap();

    private Map<Integer, Plan> plans = new HashMap();

    public Provider() {

    }

    public Provider(int key) {
        this.id = key;
    }

    public String getName() {
        return name;
    }

    public Collection<Plan> getPlans() {
        return plans.values();
    }


    public void addPlan(Plan plan) {
        plans.put(plan.getId(), plan);
        plan.setProvider(this);
    }

    public Plan getPlan(String gid) {
        return plans.get(gid);
    }

    public int getId() {
        return id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plan findPlan(String s) {
        for (Plan p : getPlans()) {
            if (p.getName().equalsIgnoreCase(s)) {
                return p;
            }
        }
        return null;
    }
}
