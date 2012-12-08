/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Condition to define the price of given date/destination.
 *
 * @author elek
 */
public class CostCondition {

    private int from = 0;

    private int to = 24;

    private Set<Integer> destinations = new HashSet();

    private Set<Day> dayConstraints = new HashSet();

    private int pricePerMin;

    private int pricePerEvent;

    private int pricePerMinAfter;

    private int costIncluded;

    private Type type = Type.CALL;

    public CostCondition() {
    }

    public CostCondition(int pricePerMin) {
        this.pricePerMin = pricePerMin;
    }

    public CostCondition from(int h) {
        this.from = h;
        return this;
    }

    public CostCondition to(int h) {
        this.to = h;
        return this;
    }

    public CostCondition addDestination(int constraint) {
        destinations.add(constraint);
        return this;
    }

    public CostCondition day(Day day) {
        dayConstraints.add(day);
        return this;
    }

    public boolean match(Calendar cal, int tono) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < from) {
            return false;
        }
        if (hour >= to) {
            return false;
        }
        if (destinations.size() > 0) {
            boolean matched = false;
            for (Integer to : destinations) {
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


    public void setDestination(String dest) {
        destinations = new TreeSet<Integer>();
        for (String d : dest.split(",")) {
            destinations.add(UidGenerator.getInstance().getProviderCode(d.trim()));
        }
    }

    public void setDays(String val) {
        dayConstraints.add(Day.valueOf(val));
    }

    public static enum Type {
        CALL, SMS;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Set<Integer> getDestinations() {
        return destinations;
    }

    public void setDestinations(Set<Integer> destinations) {
        this.destinations = destinations;
    }

    public Set<Day> getDayConstraints() {
        return dayConstraints;
    }

    public void setDayConstraints(Set<Day> dayConstraints) {
        this.dayConstraints = dayConstraints;
    }

    public int getPricePerMin() {
        return pricePerMin;
    }

    public void setPricePerMin(int pricePerMin) {
        this.pricePerMin = pricePerMin;
    }

    public int getPricePerEvent() {
        return pricePerEvent;
    }

    public void setPricePerEvent(int pricePerEvent) {
        this.pricePerEvent = pricePerEvent;
    }

    public int getPricePerMinAfter() {
        return pricePerMinAfter;
    }

    public void setPricePerMinAfter(int pricePerMinAfter) {
        this.pricePerMinAfter = pricePerMinAfter;
    }

    public int getCostIncluded() {
        return costIncluded;
    }

    public void setCostIncluded(int costIncluded) {
        this.costIncluded = costIncluded;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CostCondition that = (CostCondition) o;

        if (costIncluded != that.costIncluded) return false;
        if (from != that.from) return false;
        if (pricePerEvent != that.pricePerEvent) return false;
        if (pricePerMin != that.pricePerMin) return false;
        if (pricePerMinAfter != that.pricePerMinAfter) return false;
        if (to != that.to) return false;
        if (dayConstraints != null ? !dayConstraints.equals(that.dayConstraints) : that.dayConstraints != null)
            return false;
        if (destinations != null ? !destinations.equals(that.destinations) : that.destinations != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        result = 31 * result + (destinations != null ? destinations.hashCode() : 0);
        result = 31 * result + (dayConstraints != null ? dayConstraints.hashCode() : 0);
        result = 31 * result + pricePerMin;
        result = 31 * result + pricePerEvent;
        result = 31 * result + pricePerMinAfter;
        result = 31 * result + costIncluded;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
