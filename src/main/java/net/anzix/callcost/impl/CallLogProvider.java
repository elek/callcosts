/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost.impl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import java.util.Calendar;
import java.util.Date;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.data.SMSRecord;

/**
 *
 * @author elek
 */
public class CallLogProvider {

    public static CallLogProvider instance;

    private CallList callList;

    private Country country;

    public static CallLogProvider getInstance() {
        if (instance == null) {
            instance = new CallLogProvider();
        }
        return instance;
    }

    protected Calendar epochToCalendar(long epoch) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(epoch));
        return cal;
    }

    public void reload(Activity activity) {
        callList = new CallList();

        long timeWindow = getTimeWindow(activity);
        Cursor calls = createCallCursors(activity, timeWindow);

        if (calls.moveToFirst()) {
            do {
                Calendar cal = epochToCalendar(calls.getLong(calls.getColumnIndex(CallLog.Calls.DATE)));
                String number = calls.getString(calls.getColumnIndex(CallLog.Calls.NUMBER));
                String destination = country.getNumberParser().detect(number);
                int duration = calls.getInt(calls.getColumnIndex(CallLog.Calls.DURATION));
                if (duration == 0) {
                    continue;
                }
                String name = calls.getString(calls.getColumnIndex(CallLog.Calls.CACHED_NAME));

                CallRecord cr = new CallRecord(destination, cal, duration);
                if (name != null && name.length() > 0) {
                    cr.setName(name);
                } else {
                    cr.setName(number);
                }
                callList.addCall(cr);
            } while (calls.moveToNext());
        }

        calls.close();

        Cursor sms = createSMSCursor(activity, timeWindow);
        if (sms != null && sms.moveToFirst()) {
            do {
                String address = "";
                SMSRecord smsRecord = new SMSRecord(address, epochToCalendar(sms.getLong(sms.getColumnIndex("date"))));
                callList.addSMS(smsRecord);
            } while (sms.moveToNext());
        }
        sms.close();

    }

    protected Cursor createCallCursors(Activity context, long timeWindow) {
        return context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null,
                CallLog.Calls.TYPE + "='" + CallLog.Calls.OUTGOING_TYPE + "' AND " + CallLog.Calls.DATE + ">" + timeWindow,
                null,
                CallLog.Calls.DATE);
    }

    protected Cursor createSMSCursor(Activity context, long timeWindow) {
        String selection = String.format("date > ? AND type <> ?");
        String[] selectionArgs = new String[]{"" + timeWindow, "1"};
        String sortOrder = "date";
        return context.getContentResolver().query(Uri.parse("content://sms"), null, selection, selectionArgs, sortOrder);

    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public CallList getCallList(Activity context) {
        if (callList == null) {
            reload(context);
        }
        return callList;
    }

    public static long getTimeWindow(Activity context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int dayWindow = Integer.parseInt(sp.getString("daywindow", "30"));
        long epoch = Calendar.getInstance().getTimeInMillis();
        epoch -= (dayWindow * 86400000l);
        return epoch;
    }
}
