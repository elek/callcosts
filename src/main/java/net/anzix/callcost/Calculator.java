package net.anzix.callcost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.anzix.callcost.api.NetPlan;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;

/**
 * Alrogirthm to calculate the best plans.
 * @author elek
 */
public class Calculator {

    public static NetPlan findCheapestNet(Provider p, int mb) {
        NetPlan cheap = null;
        int cost = 0;
        for (NetPlan plan : p.getNetPlans()) {
            if (cheap == null) {
                if (plan.getCosts(mb) >= 0) {
                    cheap = plan;
                    cost = plan.getCosts(mb);
                }
            } else if (plan.getCosts(mb) < cost && plan.getCosts(mb) >= 0) {
                cost = plan.getCosts(mb);
                cheap = plan;
            }
        }
        return cheap;
    }

    public static CalculationResult calculateplan(Plan plan, CallList cl, int requiredNet) {

        CalculationResult result = new CalculationResult();
        result.setPlan(plan);
        result.setProvider(plan.getProvider());
        result.setCallCost(plan.getCost(cl));
        result.setSmsCost(plan.getSMSCost(cl));

        int missingMb = requiredNet - plan.getIncludedNet();
        int netCost = plan.getNetCost(requiredNet);
        if (netCost > -1) {
            result.setNetCost(netCost);
        }

        if (missingMb > 0 || netCost == -1) {
            NetPlan np = findCheapestNet(plan.getProvider(), missingMb);
            if (np != null) {
                int cost = np.getCosts(missingMb);
                if (netCost==-1 || cost < netCost) {
                    result.setNetPlan(np);
                    result.setNetCost(cost);
                }
            }
        }
        return result;

    }

    public static List<CalculationResult> calculateplans(Collection<Plan> plans, CallList cl, int requiredNet) {
        List<CalculationResult> results = new ArrayList<CalculationResult>();
        for (Plan p : plans) {
            results.add(calculateplan(p, cl, requiredNet));
        }
        return results;

    }
}
