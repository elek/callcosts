package net.anzix.callcost;

import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

    private static final int MENU_SETTINGS = 1;

    private static final int MENU_REFRESH = 2;

    private Hungary hungary = new Hungary();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_REFRESH, 0, "Refresh");
        menu.add(0, MENU_SETTINGS, 1, "Preferences");
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SETTINGS:
                Intent intent = new Intent(this, MyPreferencesActivity.class);
                startActivity(intent);
                return true;
            case MENU_REFRESH:
                refresh();
                return true;

        }
        return false;
    }

    public void refresh() {
        result.clear();
        Cursor c = Utils.getCursor(this);
        startManagingCursor(c);
        DestinationTypeDetector detect = new DestinationTypeDetector();

        Collection<Plan> plans = hungary.getAllPlans();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int requiredNet = Integer.parseInt(sp.getString("netusage", "0"));

        result = Utils.calculateplans(plans, c, detect, requiredNet);

        //calculate net usage

        Collections.sort(result, new Comparator<Map<String, Object>>() {

            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer i1 = (Integer) o1.get("costint");
                Integer i2 = (Integer) o2.get("costint");
                return i1.compareTo(i2);
            }
        });
        SimpleAdapter adapter = new SimpleAdapter(this, result, R.layout.cost, new String[]{"callplan", "cost"}, new int[]{R.id.plan, R.id.cost});
        setListAdapter(adapter);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);
        refresh();
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra("planid", (String) result.get(position).get("planid"));
        startActivity(intent);

    }

    private static class Result {

        public Result(int cost, String plan) {
            this.cost = cost;
            this.plan = plan;
        }

        public Result() {
        }
        int cost;

        String plan;

    }
}
