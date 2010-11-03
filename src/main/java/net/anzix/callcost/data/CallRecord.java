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
public class CallRecord {

    private String name;

    private String destination;

    private Calendar date;

    private int duration;

    private int calculatedCost;

    public CallRecord(String destination, Calendar date, int duration) {
        this.destination = destination;
        this.date = date;
        this.duration = duration;
    }

    public CallRecord() {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCalculatedCost() {
        return calculatedCost;
    }

    public void setCalculatedCost(int calculatedCost) {
        this.calculatedCost = calculatedCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
