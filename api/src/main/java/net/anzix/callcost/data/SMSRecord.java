/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.anzix.callcost.data;

/**
 * Event of sending an SMS.
 *
 * @author elek
 */
public class SMSRecord extends Record {

    public SMSRecord() {
    }

    public SMSRecord(String address, int epoch) {
        this.destination = address;
        this.time = epoch;

    }
}
