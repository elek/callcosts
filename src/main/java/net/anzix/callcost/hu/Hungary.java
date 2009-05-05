package net.anzix.callcost.hu;

import net.anzix.callcost.AbstractCountry;
import net.anzix.callcost.DestinationTypeDetector;

/**
 *
 * @author elek
 */
public class Hungary extends AbstractCountry {

    private DestinationTypeDetector dest;

    public Hungary() { 
        addProvider(new Vodafone());
        addProvider(new TMobile());
        addProvider(new Telenor());
        dest = new DestinationTypeDetector() {

            public String detect(String number) {
                String n = number.trim().replaceAll(" ", "");
                if (n.startsWith("+36")) {
                    n = n.substring(3);
                } else if (n.startsWith("06")) {
                    n = n.substring(2);
                }
                if (n.startsWith("30")) {
                    return DestinationType.TMOBILE;
                } else if (n.startsWith("70")) {
                    return DestinationType.VODAFONE;
                } else if (n.startsWith("20")) {
                    return DestinationType.PGSM;
                } else if (n.startsWith("1")) {
                    return DestinationType.TCOM;
                } else {
                    return DestinationType.UNKNOWN;
                }
            }
        };
    }

    public String getName() {
        return "Hungary";
    }

    public DestinationTypeDetector getNumberParser() {
        return dest;
    }

    public String getCurrency() {
        return "Ft";
    }
}
