package net.anzix.callcost;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elek
 */
public class PlanActivity extends ListActivity {

    private Plan p;

    private static final int MENU_DETAILS = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_DETAILS, 1, "Call log");
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DETAILS:
                Intent intent = new Intent(this, CallLogActivity.class);
                intent.putExtra("planid", p.getId());
                startActivity(intent);
                return true;

        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);
        refresh();
    }

    private void refresh() {
        p = World.getDefaultCountry().getPlan(getIntent().getExtras().getString("planid"));

        p.reset();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int dayWindow = Integer.parseInt(sp.getString("daywindow", "30"));
        long epoch = Calendar.getInstance().getTimeInMillis();
        epoch -= (dayWindow * 24 * 60 * 60 * 1000);
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.TYPE + "='" + CallLog.Calls.OUTGOING_TYPE + "' AND " + CallLog.Calls.DATE + ">" + epoch, null, CallLog.Calls.DATE);

        startManagingCursor(c);
        DestinationTypeDetector detect = new DestinationTypeDetector();
        int requiredNet = Integer.parseInt(sp.getString("netusage", "0"));

        Map<String, Object> result = Utils.calculateplans(p, c, detect, requiredNet);

        List<Map<String, String>> list = new ArrayList();

        Map<String, String> row = new HashMap();
        row.put("text1", p.getProvider().getName());
        row.put("text2", "Provider");
        list.add(row);

        row = new HashMap();
        row.put("text1", p.getName());
        row.put("text2", "Suggested call plan");
        list.add(row);

        row = new HashMap();
        row.put("text1", result.get("callcost").toString());
        row.put("text2", "Cost of calls");
        list.add(row);


        if (result.get("netplan") != null) {
            row = new HashMap();
            row.put("text1", result.get("netplan").toString());
            row.put("text2", "Suggested net plan");
            list.add(row);

            row = new HashMap();
            row.put("text1", result.get("netcost").toString());
            row.put("text2", "Cost of net usage");
            list.add(row);
        }

        row = new HashMap();
        row.put("text1", result.get("cost").toString());
        row.put("text2", "All cost");
        list.add(row);

        setListAdapter(new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, new String[]{"text1", "text2"}, new int[]{android.R.id.text1, android.R.id.text2}));


    }
}
