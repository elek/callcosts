/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import android.database.Cursor;
import android.provider.CallLog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public class Utils {

    public static Map<String, Object> calculateplans(Plan plan, Cursor c, DestinationTypeDetector detect, int requiredNet) {
        List<Plan> plans = new ArrayList();
        plans.add(plan);
        List<Map<String, Object>> result = calculateplans(plans, c, detect, requiredNet);
        return result.get(0);

    }

    public static List<Map<String, Object>> calculateplans(Collection<Plan> plans, Cursor c, DestinationTypeDetector detect, int requiredNet) {
        for (Plan p : plans) {
            p.reset();
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (c.moveToFirst()) {
            do {
                Date date = new Date(c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                String destination = detect.detect(number);
                int duration = c.getInt(c.getColumnIndex(CallLog.Calls.DURATION));
                for (Plan plan : plans) {
                    plan.addCall(cal, destination, duration);
                }
            } while (c.moveToNext());

        }

        for (Plan p : plans) {
            Map<String, Object> item = new HashMap();
            item.put("planid", p.getId());
            item.put("provider", p.getProvider().getName());
            item.put("callplan", p.getName());
            item.put("callcost", p.getCosts() + "Ft");

            item.put("costint", p.getCosts());
            item.put("cost", p.getCosts() + " Ft");

            int net = requiredNet - p.getIncludedNet();
            if (net > 0) {
                NetPlan np = findCheapestNet(p.getProvider(), net);
                if (np != null) {
                    item.put("netplan", np.getName());
                    int npcost = np.getCosts(net);
                    item.put("netcost", npcost + " Ft");

                    item.put("costint", p.getCosts() + npcost);
                    item.put("cost", (p.getCosts() + npcost) + " Ft");

                }
            }


            result.add(item);
        }
        return result;
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
}