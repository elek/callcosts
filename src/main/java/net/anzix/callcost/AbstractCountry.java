/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public abstract class AbstractCountry implements Country {

    private List<Provider> providers = new ArrayList<Provider>();

    private Map<String, Plan> plans = null;

    public void addProvider(Provider p) {
        providers.add(p);
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public Plan getPlan(String id) {
        if (plans == null) {
            indexPlans();
        }
        return plans.get(id);
    }

    protected void indexPlans() {
        plans = new HashMap<String, Plan>();
        for (Provider prov : providers) {
            for (Plan plan : prov.getPlans()) {
                plans.put(plan.getId(), plan);
            }


        }
    }

    public Collection<Plan> getAllPlans() {
        if (plans == null) {
            indexPlans();
        }
        return plans.values();
    }
}
