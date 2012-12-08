/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import net.anzix.callcost.UidGenerator;
import net.anzix.callcost.DestinationTypeDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Detect the operator of a given phone number based on predefined regular expression patterns.
 *
 * @author elek
 */
public class PatternTypeDetector implements DestinationTypeDetector {

    private List<Pattern> map = new ArrayList<Pattern>();

    public int detect(String number) {
        if (map != null) {
            for (Pattern ptr : map) {
                if (number.matches(ptr.pattern)) {
                    return ptr.code;
                }
            }
        }
        return UidGenerator.Provider.UNKNOWN.ordinal();
    }

    public void addPattern(Pattern pattern) {
        map.add(pattern);
    }

    public static class Pattern {
        private String pattern;
        private String name;
        private int code;

        public Pattern() {
        }

        public Pattern(String s, String name) {
            this.pattern = s;
            setName(name);
        }

        public void setName(String name) {
            this.name = name;
            this.code = UidGenerator.getInstance().getProviderCode(name);

        }
    }


}
