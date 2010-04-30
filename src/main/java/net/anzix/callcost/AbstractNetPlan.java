/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

/**
 *
 * @author elek
 */
public class AbstractNetPlan implements NetPlan {

    private int cost;

    private int included;

    private int overCost;

    private String name;

    private boolean prepaid;

    public AbstractNetPlan(String name, int cost, int included, int overCost) {
        this.cost = cost;
        this.included = included;
        this.overCost = overCost;
        this.name = name;
        this.prepaid = (cost == 0);
    }

    public int getCosts(int mb) {
        if (mb < included) {
            return cost;
        } else {
            if (overCost > 0) {
                return (mb - included) * overCost + cost;
            } else {
                return -1;
            }
        }
    }

    public boolean isPrepaid() {
        return prepaid;
    }

    public String getName() {
        return name;
    }

    public AbstractNetPlan setPrepaid(boolean prepaid) {
        this.prepaid = prepaid;
        return this;
    }


}
