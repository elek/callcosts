/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.data;

/**
 * @author elek
 */
public class CallRecord extends Record {

    private int duration;

    public CallRecord(String destination, int epoch, int duration) {
        this.destination = destination;
        this.time = epoch;
        this.duration = duration;
    }

    public CallRecord(int provider, String destination, int epoch, int duration) {
        this.provider = provider;
        this.destination = destination;
        this.time = epoch;
        this.duration = duration;
    }

    public CallRecord() {
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }



}
