/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anzix.callcost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;
import net.anzix.callcost.rulefile.RuleFileParser;
import net.anzix.callcost.data.*;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Singleton to store the loaded information.
 * <p/>
 * Shared between activities.
 *
 * @author elek
 */
public class DataCache {

    public Metrics metrics = new Metrics();

    public static DataCache instance;

    private CallList callList;

    private Country country;

    private boolean uptodate = true;

    private SQLiteDatabase db;

    private boolean enableCache = false;

    public DataCache() {

    }

    public static DataCache getInstance(Context activity) {
        return getInstance(activity, null);
    }

    public static DataCache getInstance(Context a, ProgressDialog dialog) {
        Context context = a;
        if (a instanceof Activity) {
            context = ((Activity) a).getApplication();
        }
        if (instance == null) {

            instance = new DataCache();
            instance.callList = new CallList();
            long time = System.currentTimeMillis();
            instance.parseRules(context, dialog);
            instance.metrics.parsingTime = System.currentTimeMillis() - time;

            instance.load(context);


        }
        return instance;
    }

    private void load(Context context) {
        if (enableCache) {
            //retrieve cached calllogs from sqlite db
            instance.retrieveCallLogsFromDb(context);
        }
        //retrieve CallLogs from real call log
        instance.retrieveCallLogs(context, null);

        instance.retrieveSMSLogs(context);
    }

    private void retrieveSMSLogs(Context activity) {
        long timeWindow = getTimeWindow(activity);
        Cursor sms = createSMSCursor(activity, timeWindow);
        if (sms != null && sms.moveToFirst()) {
            do {
                String address = "";
                SMSRecord smsRecord = new SMSRecord(address, (int) sms.getLong(sms.getColumnIndex("date")));
                callList.addSMS(smsRecord);
            } while (sms.moveToNext());
        }
        sms.close();

    }

    private void retrieveCallLogsFromDb(Context activity) {
        CallLogHelper helper = new CallLogHelper(activity);
        db = helper.getWritableDatabase();
        Cursor c = db.query(CallLogTable.TABLE, CallLogTable.ALL_COLL, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                CallRecord r = new CallRecord(
                        c.getString(c.getColumnIndexOrThrow(CallLogTable.COL_DESTINATION)),
                        c.getInt(c.getColumnIndexOrThrow(CallLogTable.COL_TIME)),
                        c.getInt(c.getColumnIndexOrThrow(CallLogTable.COL_DURATION)));
                r.setName(c.getString(c.getColumnIndexOrThrow(CallLogTable.COL_NAME)));
                r.setProvider(c.getInt(c.getColumnIndexOrThrow(CallLogTable.COL_PROVIDER)));

                callList.addCall(r);
            } while (c.moveToNext());
        }


    }


    private void parseRules(Context activity, ProgressDialog dialog) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        String countryCode = sp.getString("country", "hu");

        RuleFileParser loader = new RuleFileParser();
        try {
            InputStream st = activity.getResources().openRawResource(R.raw.hungary);
            World world = new World();
            RuleFileParser instance = new RuleFileParser();
            instance.read(world, st);
            //TODO restore multi country behaviour
            country = world.getCountry(1);
            st.close();

        } catch (Exception ex) {
            Log.e("CALLCOST", "Can't load", ex);
        }
    }


    protected Calendar epochToCalendar(long epoch) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(epoch));
        return cal;
    }

    private void retrieveCallLogs(Context activity, ProgressDialog dialog) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        callList.setRequiredNet(Integer.parseInt(sp.getString("netusage", "0")));

        long timeWindow = getTimeWindow(activity);
        Cursor calls = createCallCursors(activity, timeWindow);

        if (calls.moveToFirst()) {
            do {
                int epoch = (int) (calls.getLong(calls.getColumnIndex(CallLog.Calls.DATE)) / 1000);
                Log.i("CALLOG", "epoch=" + epoch);
                String number = calls.getString(calls.getColumnIndex(CallLog.Calls.NUMBER));
                int destination = country.getNumberParser().detect(number);
                int duration = calls.getInt(calls.getColumnIndex(CallLog.Calls.DURATION));
                if (duration == 0) {
                    continue;
                }
                String name = calls.getString(calls.getColumnIndex(CallLog.Calls.CACHED_NAME));

                CallRecord cr = new CallRecord(destination, number, epoch, duration);
                if (name != null && name.length() > 0) {
                    cr.setName(name);
                } else {
                    cr.setName(number);
                }

                if (callList.addCall(cr) && enableCache) {
                    //new record, please save it
                    ContentValues v = new ContentValues();
                    v.put(CallLogTable.COL_TIME, cr.getTime());
                    v.put(CallLogTable.COL_DESTINATION, cr.getDestination());
                    v.put(CallLogTable.COL_DURATION, cr.getDuration());
                    v.put(CallLogTable.COL_NAME, cr.getName());
                    v.put(CallLogTable.COL_PROVIDER, cr.getProvider());
                    db.insert(CallLogTable.TABLE, null, v);
                }
            } while (calls.moveToNext());
        }

        calls.close();
    }

    protected Cursor createCallCursors(Context context, long timeWindow) {
        return context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null,
                CallLog.Calls.TYPE + "='" + CallLog.Calls.OUTGOING_TYPE + "' AND " + CallLog.Calls.DATE + ">" + timeWindow,
                null,
                CallLog.Calls.DATE);
    }

    protected Cursor createSMSCursor(Context context, long timeWindow) {
        String selection = String.format("date > ? AND type <> ?");
        String[] selectionArgs = new String[]{"" + timeWindow, "1"};
        String sortOrder = "date";
        return context.getContentResolver().query(Uri.parse("content://sms"), null, selection, selectionArgs, sortOrder);

    }

    public CallList getCallList() {
        return callList;
    }

    public static long getTimeWindow(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int dayWindow = Integer.parseInt(sp.getString("daywindow", "30"));
        long epoch = Calendar.getInstance().getTimeInMillis();
        epoch -= (dayWindow * 86400000l);
        return epoch;
    }

    public Country getCountry() {
        return country;
    }

    public void invalidate() {
        uptodate = false;
    }

    public boolean isUptodate() {
        return uptodate;
    }

    public void setUptodate(boolean uptodate) {
        this.uptodate = uptodate;
    }


    public void terminate() {
        if (db != null) {
            db.close();
        }

    }

    public void cleanDbCache(Context context) {
        db.execSQL("DELETE FROM " + CallLogTable.TABLE);
        callList = new CallList();
        load(context);
        invalidate();


    }
}
