/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.def;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author elek
 */
public class CostCondition {

    private int fromHour = 0;

    private int toHour = 24;

    private Set<String> destinations = new HashSet();

    private Set<Day> dayConstraints = new HashSet();

    private int cost;

    public CostCondition(int cost) {
        this.cost = cost;
    }

    public CostCondition from(int h) {
        this.fromHour = h;
        return this;
    }

    public CostCondition to(int h) {
        this.toHour = h;
        return this;
    }

    public CostCondition addDestination(String constraint) {
        destinations.add(constraint);
        return this;
    }

    public CostCondition day(Day day) {
        dayConstraints.add(day);
        return this;
    }

    public boolean match(Calendar cal, String tono) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < fromHour) {
            return false;
        }
        if (hour >= toHour) {
            return false;
        }
        if (destinations.size() > 0) {
            boolean matched = false;
            for (String to : destinations) {
                if (to.equals("*") || to.equals(tono)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        if (dayConstraints.size() > 0) {
            boolean matched = false;
            for (Day d : dayConstraints) {
                if (d.match(cal)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    public int getCost() {
        return cost;
    }

    
}
