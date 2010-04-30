/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.tmobile;

import net.anzix.callcost.AbstractNetPlan;
import net.anzix.callcost.AbstractProvider;

/**
 *
 * @author elek
 */
public class TMobile extends AbstractProvider {

    public TMobile() {
        addPlan(new MediaMania("Média Mánia S", 4490, 26, 29).setIncludedNet(500));
        addPlan(new MediaMania("Média Mánia M", 7990, 22, 25).setIncludedNet(750));
        addPlan(new MediaMania("Média Mánia L", 10990, 19, 21).setIncludedNet(1000));
        addPlan(new MediaMania("Média Mánia XL", 15990, 17, 19).setIncludedNet(2000));

        addPlan(new Domino7());
        addPlan(new DominoNap());

        addNetPlan(new AbstractNetPlan("Net 40", 690, 40, 200));
        addNetPlan(new AbstractNetPlan("Net 100", 990, 100, 200));
        addNetPlan(new AbstractNetPlan("Net 500", 1990, 500, 100));
       
    }

    public String getName() {
        return "T-Mobile";
    }
}
