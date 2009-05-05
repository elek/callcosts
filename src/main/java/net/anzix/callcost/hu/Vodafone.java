/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.hu;

import net.anzix.callcost.AbstractNetPlan;
import net.anzix.callcost.AbstractProvider;
import net.anzix.callcost.def.SimplePlan;

/**
 *
 * @author elek
 */
public class Vodafone extends AbstractProvider {

    public String getName() {
        return "Vodafone";
    }

    public Vodafone() {
        addPlan(new SimplePlan("Vodafone 50 plus", 1750, 35, 35, 50));
        addPlan(new SimplePlan("Vodafone 100 plus", 300, 33, 33, 100));
        addPlan(new SimplePlan("Vodafone 200 plus", 5000, 29, 29, 200));
        addPlan(new SimplePlan("Vodafone 400 plus", 8000, 25, 25, 400));

        addPlan(new SimplePlan("Vitamax standard", 0, 41, 41, 0).setPrepaid(true));

        addPlan(new SimplePlan("Multimédia 5000", 5000, 28, 28, 200).setIncludedNet(200));
        addPlan(new SimplePlan("Multimédia 7900", 7900, 24, 24, 300).setIncludedNet(500));
        addPlan(new SimplePlan("Multimédia 10500", 10500, 21, 21, 500).setIncludedNet(3000));


        addNetPlan(new AbstractNetPlan("Internet 100", 1600, 100, 50));
        addNetPlan(new AbstractNetPlan("Internet 1G", 3000, 1000, 31));
        addNetPlan(new AbstractNetPlan("Internet 5G", 5500, 5000, 6));

    }
}
