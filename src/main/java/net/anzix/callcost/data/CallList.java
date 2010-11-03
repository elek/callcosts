/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elek
 */
public class CallList {

    private List<CallRecord> calls = new ArrayList();

    private List<SMSRecord> smss = new ArrayList();

    public void addCall(CallRecord call) {
        calls.add(call);
    }

    public List<SMSRecord> getSMSs() {
        return smss;
    }

    public List<CallRecord> getCalls() {
        return calls;
    }

    public void setCalls(List<CallRecord> calls) {
        this.calls = calls;
    }

    public void addSMS(SMSRecord smsRecord) {
        smss.add(smsRecord);
    }

    
   
}
