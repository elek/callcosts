/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.in;

import java.util.ArrayList;
import java.util.List;
import net.anzix.callcost.AbstractCountry;
import net.anzix.callcost.Country;
import net.anzix.callcost.DestinationTypeDetector;
import net.anzix.callcost.Plan;
import net.anzix.callcost.Provider;

/**
 *
 * @author elek
 */
public class India extends AbstractCountry {

    public India() {
        addProvider(new Vodafone());
    }

    public String getName() {
        return "India";
    }

    public DestinationTypeDetector getNumberParser() {
        return new DestinationTypeDetector() {

            public String detect(String number) {
                return "UNKNOWN";
            }
        };

    }

    public String getCurrency() {
        return "p";
    }
}
