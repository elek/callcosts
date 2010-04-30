/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

/**
 *
 * @author elek
 */
public class Util {

    public static int minuteBased(int sec) {
        if (sec == 0) {
            return 0;
        }
        return (sec / 60) + 1;
    }
}
