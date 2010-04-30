package net.anzix.callcost;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public class CallLogActivity extends ListActivity {

    private Hungary hungary = new Hungary();

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);

        Plan p = hungary.getPlan(getIntent().getExtras().getString("planid"));
        p.reset();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int dayWindow = Integer.parseInt(sp.getString("daywindow", "30"));
        long epoch = Calendar.getInstance().getTimeInMillis();
        epoch -= (dayWindow * 24 * 60 * 60 * 1000);
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.TYPE + "='" + CallLog.Calls.OUTGOING_TYPE + "' AND " + CallLog.Calls.DATE + ">" + epoch, null, CallLog.Calls.DATE);

        startManagingCursor(c);
        DestinationTypeDetector detect = new DestinationTypeDetector();
        if (c.moveToFirst()) {
            do {
                Map<String, String> item = new HashMap<String, String>();
                Calendar cal = Calendar.getInstance();
                Date date = new Date(c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));
                cal.setTime(date);
                String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                String destination = detect.detect(number);
                Log.i("callcost", number + " " + destination);
                int duration = c.getInt(c.getColumnIndex(CallLog.Calls.DURATION));
                String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                if (name != null && name.length() > 0) {
                    item.put("name", name);
                } else {
                    item.put("name", number);
                }
                item.put("cost", "" + p.addCall(cal, destination, duration) + " Ft");
                item.put("duration", duration + " s");
                item.put("type", destination);
                if (duration > 0) {
                    list.add(item);
                }
            } while (c.moveToNext());
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.event, new String[]{"name", "cost", "duration", "type"}, new int[]{R.id.name, R.id.cost, R.id.duration, R.id.type});
        setListAdapter(adapter);
    }
}
