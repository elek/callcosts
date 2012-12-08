package net.anzix.callcost.data;

import net.anzix.callcost.DestinationTypeDetector;

import java.util.Calendar;

/**
 * Super class for any call or SMS event.
 */
public class Record {


    /**
     * Name of the destination's owner.
     */
    protected String name;

    /**
     * Destination number.
     */
    protected String destination;

    /**
     * The resolved type of the provider of the destination.
     */
    protected int provider;
    /**
     * Date of the event
     * sec since 1970.
     */
    protected int time;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    public int getTime() {
        return time;
    }


    public void classify(DestinationTypeDetector dtd) {
        this.provider = dtd.detect(destination);
    }

    public Calendar getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long)time * 1000l);
        return cal;
    }
}
