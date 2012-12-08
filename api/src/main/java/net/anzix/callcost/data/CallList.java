/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.data;

import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.data.SMSRecord;

import java.util.*;

/**
 * Lists of SMS and voice calls. The base data for the calculation.
 *
 * @author elek
 */
public class CallList {

    private Map<Integer, CallRecord> calls = new TreeMap<Integer, CallRecord>();

    private List<SMSRecord> smss = new ArrayList();

    private int requiredNet;

    public boolean addCall(CallRecord call) {
        if (calls.containsKey(call.getTime())) {
            return false;
        } else {
            calls.put(call.getTime(), call);
            return true;
        }
    }

    public void addSMS(SMSRecord record) {
        smss.add(record);
    }

    public Collection<CallRecord> getCalls() {
        return calls.values();
    }


    public List<SMSRecord> getSMSs() {
        return smss;
    }

    public void setSmss(List<SMSRecord> smss) {
        this.smss = smss;
    }

    public int getRequiredNet() {
        return requiredNet;
    }

    public void setRequiredNet(int requiredNet) {
        this.requiredNet = requiredNet;
    }


}
