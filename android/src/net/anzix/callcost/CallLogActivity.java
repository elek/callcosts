package net.anzix.callcost;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.ui.ListElement;
import net.anzix.callcost.ui.ListElementAdapter;
import net.anzix.callcost.ui.MemberElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Activity to display the call log.
 *
 * @author elek
 */
public class CallLogActivity extends ListActivity {


    private DataCache clp;

    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
        clp = DataCache.getInstance(this);

        Country country = clp.getCountry();

        Plan p = country.getPlan(getIntent().getExtras().getInt("planid"));

        CalculationResult result = p.calculateCost(clp.getCallList());
        List<MemberElement> list = new ArrayList<MemberElement>();
        for (CallRecord rec : clp.getCallList().getCalls()) {
            list.add(new ListElement(
                    rec.getName(),
                    sdf.format(rec.getDate().getTime()) + " / " + rec.getDuration() + " s / " + UidGenerator.getInstance().getProviderName(rec.getProvider()),
                    Tools.printNumber(result.getEstimatedCost(rec), clp.getCountry())));
        }
        registerForContextMenu(getListView());
        ListElementAdapter a = new ListElementAdapter(this, list);
        a.setAllLink(true);
        setListAdapter(a);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calllog_options, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.exclude_all:
                clp.invalidate();
                return true;
            case R.id.exclude_one:
                clp.invalidate();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
