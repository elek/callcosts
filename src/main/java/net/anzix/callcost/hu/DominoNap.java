/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.hu;

import net.anzix.callcost.hu.DestinationType;
import net.anzix.callcost.def.CostCondition;
import net.anzix.callcost.def.SimplePlan;

/**
 *
 * @author elek
 */
public class DominoNap extends SimplePlan {

    public DominoNap() {
        addCond(new CostCondition(41).to(DestinationType.PGSM).to(DestinationType.VODAFONE));
        addCond(new CostCondition(38));
    }

    @Override
    public boolean isPrepaid() {
        return true;
    }

    @Override
    public String getName() {
        return "Dominó nap";
    }
}
