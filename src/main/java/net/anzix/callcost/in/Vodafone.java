/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.in;

import net.anzix.callcost.AbstractProvider;
import net.anzix.callcost.def.SimplePlan;

/**
 *
 * @author elek
 */
public class Vodafone extends AbstractProvider {

    public Vodafone() {
        addPlan(new SimplePlan("Test", 99, 12, 12, 100));
    }

    public String getName() {
        return "Vodafone";
    }
}
