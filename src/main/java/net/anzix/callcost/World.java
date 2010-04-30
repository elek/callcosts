/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

/**
 *
 * @author elek
 */
public class World {

    static Country c;

    public static Country getDefaultCountry() {
        if (c == null) {
            c = new Hungary();
        }
        return c;

    }
}
