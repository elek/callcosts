package net.anzix.callcost;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.*;
import net.anzix.callcost.data.CalculationResult;

import java.util.*;


/**
 * Main application entry point.
 *
 * @author elek
 */
public class CostActivity extends SherlockListActivity {

    List<CalculationResult> results = new ArrayList<CalculationResult>();

    private DataCache clp;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);
        new Loader(this, true).execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Loader(this, false).execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, PlanActivity.class);
        intent.putExtra("planid", results.get(position).getPlan().getId());
        startActivity(intent);

    }

    private class CalculationAdapter extends BaseAdapter {

        private List<CalculationResult> results = new ArrayList<CalculationResult>();

        private final LayoutInflater mInflater;

        public CalculationAdapter(List<CalculationResult> result) {
            this.results = result;
            this.mInflater = LayoutInflater.from(CostActivity.this);
        }

        public void setResults(List<CalculationResult> results) {
            this.results = results;
        }

        public int getCount() {
            return results.size();
        }

        public Object getItem(int i) {
            return results.get(i);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View view, ViewGroup vg) {
            CalculationResult result = results.get(i);
            if (view == null) {
                view = mInflater.inflate(R.layout.triple_list_item, vg, false);

            }
            ((TextView) view.findViewById(R.id.label)).setText(result.getPlan().getName());
            ((TextView) view.findViewById(R.id.description)).setText(result.getPlan().getProvider().getName());
            Country country = clp.getCountry();
            ((TextView) view.findViewById(R.id.value)).setText(Tools.printNumber(result.getCost(), country, 0, false));
            view.requestLayout();
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSherlock().getMenuInflater().inflate(R.menu.cost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_preferences:
                startActivity(new Intent(this, MyPreferencesActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return false;
    }

    private class Loader extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        boolean firstTime = true;

        private Loader(Activity context, boolean firstTime) {
            this.dialog = new ProgressDialog(context);
            this.firstTime = firstTime;
        }

        @Override
        protected void onPreExecute() {
            if (firstTime) {
                dialog.setMessage("Calculating...");
                dialog.show();
            }

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            clp = DataCache.getInstance(CostActivity.this, dialog);
            calculate();
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (firstTime) {
                CalculationAdapter adapter = new CalculationAdapter(new ArrayList<CalculationResult>(results));
                setListAdapter(adapter);
            } else {
                ((CalculationAdapter) getListAdapter()).setResults(new ArrayList<CalculationResult>(results));
                ((CalculationAdapter) getListAdapter()).notifyDataSetChanged();
            }

            clp.setUptodate(true);
            if (firstTime) {
                dialog.dismiss();
            }
        }

        public void calculate() {
            results.clear();
            long time = System.currentTimeMillis();

            Country country = clp.getCountry();
            Collection<Plan> plans = country.getAllPlans();

            for (Plan p : plans) {
                CalculationResult result = p.calculateCost(clp.getCallList());
                if (result.getUnpaidNetUsage() <= 0) {
                    results.add(result);
                }
            }


            Collections.sort(results, new Comparator<CalculationResult>() {

                public int compare(CalculationResult c1, CalculationResult c2) {
                    return new Integer(c1.getCost()).compareTo(new Integer(c2.getCost()));
                }
            });
            clp.metrics.calculationTime = System.currentTimeMillis() - time;
        }
    }


}
