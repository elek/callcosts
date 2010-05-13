/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.custom;

import java.util.Map;
import net.anzix.callcost.api.DestinationTypeDetector;

/**
 *
 * @author elek
 */
public class PatternTypeDetector implements DestinationTypeDetector {

    private Map map;

    public PatternTypeDetector(Map parameters) {
        this.map = parameters;
    }

    public String detect(String number) {
        if (map != null) {
            for (Map rec : ((Map<String, Map>) map).values()) {
                String pattern = rec.get("pattern").toString();
                if (number.matches(pattern)) {
                    return rec.get("name").toString();
                }
            }
        }
        return "UNKNOWN";
    }

    public Map getMap() {
        return map;
    }

    
}
