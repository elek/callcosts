package net.anzix.callcost;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.anzix.callcost.api.CallList;
import net.anzix.callcost.api.CallRecord;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.DestinationTypeDetector;
import net.anzix.callcost.api.NetPlan;
import net.anzix.callcost.api.Plan;
import net.anzix.callcost.api.Provider;
import net.anzix.callcost.api.Tools;
import net.anzix.callcost.api.World;

/**
 *
 * @author elek
 */
public class AndroidUtils {

    public static Map<String, Object> calculateplans(Plan plan, Cursor c, DestinationTypeDetector detect, int requiredNet) {
        List<Plan> plans = new ArrayList();
        plans.add(plan);
        List<Map<String, Object>> result = calculateplans(plans, c, detect, requiredNet);
        return result.get(0);

    }

    public static CallList getCallListFromCursor(Cursor c, DestinationTypeDetector detect) {
        CallList cl = new CallList();
        if (c.moveToFirst()) {
            do {
                Date date = new Date(c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                String destination = detect.detect(number);
                int duration = c.getInt(c.getColumnIndex(CallLog.Calls.DURATION));
                if (duration == 0) {
                    continue;
                }
                String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));

                CallRecord cr = new CallRecord(destination, cal, duration);
                if (name != null && name.length() > 0) {
                    cr.setName(name);
                } else {
                    cr.setName(number);
                }



                cl.addCall(cr);
            } while (c.moveToNext());

        }
        return cl;

    }

    public static List<Map<String, Object>> calculateplans(Collection<Plan> plans, Cursor c, DestinationTypeDetector detect, int requiredNet) {
        CallList cl = getCallListFromCursor(c, detect);
        return Tools.calculateplans(plans, cl, detect, requiredNet);
    }

    public static Cursor getCursor(Activity context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int dayWindow = Integer.parseInt(sp.getString("daywindow", "30"));
        long epoch = Calendar.getInstance().getTimeInMillis();
        epoch -= (dayWindow * 86400000l);

        return context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.TYPE + "='" + CallLog.Calls.OUTGOING_TYPE + "' AND " + CallLog.Calls.DATE + ">" + epoch, null, CallLog.Calls.DATE);
    }
}
