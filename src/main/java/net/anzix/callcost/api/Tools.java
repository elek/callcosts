/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public class Tools {

    public static String printNumber(int no, Country c) {
        int divider = c.getDivider();
        String currentcy = c.getCurrency();
        String number = "" + no;

        while (number.length() <= divider) {
            number = "0" + number;
        }
        int sep = number.length() - divider;
        return number.substring(0, sep) + (divider > 0 ? ("." + number.substring(sep)) : "") + " " + currentcy;



    }

    public static int getInt(Map<String, Object> properties, String string, int def) {
        String val = (String) properties.get(string);
        if (val == null) {
            return def;
        } else if (val.equals("unlimited")) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(val);
    }

    public static int getDuration(int sec, int unit) {
        int secs = ((sec / unit) + 1) * unit;
        if (unit == 1) {
            unit--;
        }
        return secs;
    }

    public static int unitBasedPrice(int sec, int unit, int price) {
        if (sec == 0) {
            return 0;
        }
        return getDuration(sec, unit) * price / 60;
    }

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

    public static List<Map<String, Object>> calculateplans(Collection<Plan> plans, CallList cl, DestinationTypeDetector detect, int requiredNet) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Country country = World.instance().getCurrentCountry();

        for (Plan p : plans) {
            Map<String, Object> item = new HashMap();

            item.put("planid", p.getId());
            item.put("provider", p.getProvider().getName());
            item.put("callplan", p.getName());
            int callcost = p.getCost(cl);
            item.put("callcost", Tools.printNumber(callcost, country));

            item.put("costint", callcost);
            item.put("cost", Tools.printNumber(callcost, country));

            int net = requiredNet - p.getIncludedNet();
            if (net > 0) {
                NetPlan np = findCheapestNet(p.getProvider(), net);
                if (np != null) {
                    item.put("netplan", np.getName());
                    int npcost = np.getCosts(net);
                    item.put("netcost", Tools.printNumber(npcost, country));

                    item.put("costint", callcost + npcost);
                    item.put("cost", Tools.printNumber(callcost + npcost, country));

                }
            }


            result.add(item);
        }
        return result;

    }
}
