package net.anzix.callcost;

import net.anzix.callcost.api.World;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.Plan;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.anzix.callcost.impl.Calculator;
import net.anzix.callcost.impl.CallLogProvider;
import net.anzix.callcost.impl.Tools;
import net.anzix.callcost.data.CalculationResult;

/**
 *
 * @author elek
 */
public class PlanActivity extends ListActivity {

    private Plan p;

    private static final int MENU_DETAILS = 1;

    private CallLogProvider clp = CallLogProvider.getInstance();

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
        Country country = World.instance().getCurrentCountry();
        p = country.getPlan(getIntent().getExtras().getString("planid"));


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int requiredNet = Integer.parseInt(sp.getString("netusage", "0"));
        CalculationResult result = Calculator.calculateplan(p, clp.getCallList(this), requiredNet);

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
        row.put("text1", Tools.printNumber(result.getCallCost(), country));
        row.put("text2", "Cost of calls");
        list.add(row);

        row = new HashMap();
        row.put("text1", Tools.printNumber(result.getSmsCost(), country));
        row.put("text2", "Cost of SMSs");
        list.add(row);


        if (result.getNetPlan() != null) {
            row = new HashMap();
            row.put("text1", result.getNetPlan().getName());
            row.put("text2", "Suggested net plan");
            list.add(row);
        }
        if (result.getNetCost() > 0) {
            row = new HashMap();
            row.put("text1", Tools.printNumber(result.getNetCost(), country));
            row.put("text2", "Cost of net usage");
            list.add(row);
        }

        row = new HashMap();
        row.put("text1", Tools.printNumber(result.getAllCosts(), country));
        row.put("text2", "All cost");
        list.add(row);

        setListAdapter(new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, new String[]{"text1", "text2"}, new int[]{android.R.id.text1, android.R.id.text2}));


    }
}
