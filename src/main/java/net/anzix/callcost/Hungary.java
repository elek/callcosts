package net.anzix.callcost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.anzix.callcost.pannon.Telenor;
import net.anzix.callcost.tmobile.TMobile;
import net.anzix.callcost.vodafone.Vodafone;

/**
 *
 * @author elek
 */
public class Hungary implements Country{

    private List<Provider> providers = new ArrayList<Provider>();

    private Map<String, Plan> plans = null;

    public Hungary() {
        providers.add(new Vodafone());
        providers.add(new TMobile());
        providers.add(new Telenor());
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
