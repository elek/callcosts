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
public class Telenor extends AbstractProvider {

    public Telenor() {

        addPlan(new PerCall("40 hívás", 4960, 40, 124, 32));
        addPlan(new PerCall("100 hívás", 8900, 100, 89, 22));
        addPlan(new PerCall("200 hívás", 16800, 200, 84, 19));

        addPlan(new SimplePlan("Pannon 35", 1295, 37, 37, 35));
        addPlan(new SimplePlan("Pannon 60", 2100, 35, 35, 60));
        addPlan(new SimplePlan("Pannon 120", 3840, 32, 32, 120));
        addPlan(new SimplePlan("Pannon 180", 5220, 29, 29, 180));
        addPlan(new SimplePlan("Pannon 360", 8460, 24, 24, 360));
        addPlan(new SimplePlan("Pannon 600", 12600, 21, 21, 600));
        addPlan(new SimplePlan("Pannon 1200", 21600, 18, 18, 1200));


        addPlan(new SimplePlan("Mobil kvartett 150", 4990, 29, 29, 150).setIncludedNet(150));
        addPlan(new SimplePlan("Mobil kvartett 400", 4990, 29, 29, 400).setIncludedNet(1000));
        addPlan(new SimplePlan("Mobil kvartett 800", 4990, 29, 29, 800).setIncludedNet(3000));

        addNetPlan(new AbstractNetPlan("Internet Mini", 480, 20, 32));
        addNetPlan(new AbstractNetPlan("Internet Classic", 990, 100, 32));
        addNetPlan(new AbstractNetPlan("Internet Plus", 1990, 200, -1));
    }

    public String getName() {
        return "Pgsm";
    }
}
