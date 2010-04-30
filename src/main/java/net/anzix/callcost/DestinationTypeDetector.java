/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

/**
 *
 * @author elek
 */
public class DestinationTypeDetector {

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
}
