/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.hu;


import net.anzix.callcost.hu.DestinationType;
import net.anzix.callcost.def.CostCondition;
import net.anzix.callcost.def.Day;
import net.anzix.callcost.def.SimplePlan;

/**
 *
 * @author elek
 */
public class Domino7 extends SimplePlan {

    public Domino7() {
        addCond(new CostCondition(80).from(7).to(20).day(Day.WEEKDAY).to(DestinationType.TMOBILE));
        addCond(new CostCondition(11).from(22).to(DestinationType.TMOBILE));
        addCond(new CostCondition(11).to(7).to(DestinationType.TMOBILE));
        addCond(new CostCondition(22).to(DestinationType.TMOBILE));

        addCond(new CostCondition(90).from(7).to(20).day(Day.WEEKDAY).to(DestinationType.TCOM));
        addCond(new CostCondition(22).to(DestinationType.TCOM));

        addCond(new CostCondition(90).from(7).to(20).day(Day.WEEKDAY).to(DestinationType.VODAFONE).to(DestinationType.PGSM));
        addCond(new CostCondition(33).to(DestinationType.VODAFONE).to(DestinationType.PGSM));
    }

    @Override
    public boolean isPrepaid() {
        return true;
    }

    @Override
    public String getName() {
        return "Dominó 7";
    }
}
