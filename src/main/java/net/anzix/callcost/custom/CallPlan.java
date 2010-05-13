package net.anzix.callcost.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.anzix.callcost.api.CallList;
import net.anzix.callcost.api.CallRecord;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;
import net.anzix.callcost.api.Tools;
import net.anzix.callcost.def.CostCondition;
import net.anzix.callcost.def.Day;

/**
 *
 * @author elek
 */
public class CallPlan implements Plan, PropertyContainer {

    private String id;

    private String name;

    private Map<String, Object> properties = new HashMap();

    private Provider provider;

    private List<CostCondition> conds = new ArrayList();

    public CallPlan(String key, String name) {
        this.name = name;
        this.id = key;
    }

    public boolean isPrepaid() {
        return Boolean.parseBoolean((String) properties.get("prepaid"));
    }

    public String getName() {
        return name;
    }

    public int getIncludedNet() {
        return intParam("netIncluded", 0);
    }

    public void reset() {
    }

    public String getId() {
        return id;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void addCond(CostCondition cond) {
        conds.add(cond);
    }

    public int getCost(CallList list) {
        if (properties.get("rules") != null) {
            parseRules();

        }
        int freeCall = intParam("freeCall", 0);
        int cost = intParam("monthlyFee", 0);
        int includedCost = intParam("includedCost", 0);
        int freeSec = intParam("freeMinutes", 0) * 60;
        int unit = intParam("base", 60);
        int ppm = intParam("pricePerMin", -1);
        if (ppm != -1) {
            addCond(new CostCondition(ppm));
        }

        for (CallRecord record : list.getCalls()) {
            record.setCalculatedCost(0);
            if (freeCall > 0) {
                freeCall--;
                continue;
            }
            int realSec = Tools.getDuration(record.getDuration(), unit);
            System.out.println("REALS" + realSec);
            if (freeSec > 0) {
                freeSec -= realSec;
                realSec = 0;
            }
            if (freeSec < 0) {
                realSec = freeSec * -1;
            }
            if (realSec > 0) {
                for (CostCondition cond : conds) {
                    if (cond.match(record.getDate(), record.getDestination())) {
                        int c = Tools.unitBasedPrice(record.getDuration(), unit, cond.getCost());
                        if (includedCost > 0) {
                            includedCost -= c;
                            c = 0;
                        }
                        if (includedCost < 0) {
                            c = includedCost * -1;
                        }
                        if (c > 0) {
                            record.setCalculatedCost(c);
                            cost += c;
                        }
                        break;
                    }
                }
            }


        }
        return cost;
    }

    public int addSMS(Calendar time, String to) {
        return 0;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    private int intParam(String string, int def) {
        String val = (String) properties.get(string);
        if (val == null) {
            return def;
        } else if (val.equals("unlimited")) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(val);
    }

    private void parseRules() {
        Map rules = (Map) properties.get("rules");
        for (Map<String, String> ruleParameters : ((Map<String, Map<String, String>>) rules).values()) {
            String value = ruleParameters.get("cost");
            CostCondition cond = new CostCondition(Integer.parseInt(value));

            value = ruleParameters.get("from");
            if (value != null) {
                cond.from(Integer.parseInt(value));
            }

            value = ruleParameters.get("to");
            if (value != null) {
                cond.to(Integer.parseInt(value));
            }

            value = ruleParameters.get("destination");
            if (value != null) {
                for (String dest : value.split(",")) {
                    cond.addDestination(dest);
                }
            }
            value = ruleParameters.get("day");
            if (value != null) {
                for (String day : value.split(",")) {
                    cond.day(Day.valueOf(day));
                }
            }
            conds.add(cond);
        }
        properties.remove("rules");
    }
}
