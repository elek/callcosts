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
import net.anzix.callcost.api.NetPlan;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;

/**
 *
 * @author elek
 */
public class CustomProvider implements Provider,PropertyContainer {

    private String id;

    private String name;

    private Map<String, Object> properties = new HashMap();

    private Map<String, Plan> plans = new HashMap();

    private List<NetPlan> netplans = new ArrayList();

    public CustomProvider(String key, String name) {
        this.id = key;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public Collection<Plan> getPlans() {
        return plans.values();
    }

    public List<NetPlan> getNetPlans() {
        return netplans;
    }

    public void addPlan(Plan plan) {
        plans.put(plan.getId(), plan);
        plan.setProvider(this);
    }

    public Plan getPlan(String gid) {
        return plans.get(gid);
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }


}
