/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.data;

import java.util.Calendar;

/**
 *
 * @author elek
 */
public class SMSRecord {

    private String destination;

    private Calendar date;

    public SMSRecord() {
    }

    public SMSRecord(String destination, Calendar date) {
        this.destination = destination;
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }



}
