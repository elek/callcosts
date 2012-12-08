package net.anzix.callcost;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import net.anzix.callcost.option.Option;
import net.anzix.callcost.data.CalculationResult;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.ui.ListElement;
import net.anzix.callcost.ui.ListElementAdapter;
import net.anzix.callcost.ui.MemberElement;
import net.anzix.callcost.ui.SeparatorElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author elek
 */
public class PlanActivity extends ListActivity {

    private Plan p;

    private static final int MENU_DETAILS = 1;

    private DataCache clp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);
        clp = DataCache.getInstance(this);
        refresh();
    }

    private void refresh() {
        Country country = clp.getCountry();
        p = country.getPlan(getIntent().getExtras().getInt("planid"));
        CalculationResult result = p.calculateCost(clp.getCallList());


        final List<MemberElement> list = new ArrayList<MemberElement>();
        list.add(new ListElement(p.getProvider().getName(), getResources().getString(R.string.plan_provider_desc)));
        list.add(new ListElement(p.getName(), getResources().getString(R.string.plan_plan_desc)));
        list.add(new ListElement(Tools.printNumber(result.getCost(), country), getResources().getString(R.string.plan_cost_desc)));

        list.add(new SeparatorElement(getResources().getString(R.string.plan_detail_separator)));


        CallList callList = clp.getCallList();
        list.add(new ListElement(Tools.printNumber(result.getMonthlyFee(), country), getResources().getString(R.string.plan_costmonthly_desc)));
        ListElement calls = new ListElement(Tools.printNumber(result.getCost(callList.getCalls()), country), getResources().getString(R.string.plan_costofcall_desc));
        calls.setLink(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PlanActivity.this, CallLogActivity.class);
                intent.putExtra("planid", p.getId());
                startActivity(intent);
            }
        });
        list.add(calls);


        list.add(new ListElement(Tools.printNumber(result.getCost(callList.getSMSs()), country), getResources().getString(R.string.plan_costofsms_desc)));

        list.add(new SeparatorElement(getResources().getString(R.string.plan_option_separator)));
        for (Option o : result.getOptions()) {
            list.add(new ListElement(o.getName(), getResources().getString(R.string.plan_option_desc), Tools.printNumber(result.getCost(o), country)));
        }
        setListAdapter(new ListElementAdapter(this, list));
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MemberElement e = list.get(i);
                if (e instanceof ListElement) {
                    AdapterView.OnItemClickListener link = ((ListElement) e).getLink();
                    if (link != null) {
                        link.onItemClick(adapterView, view, i, l);
                    }
                }

            }
        });

    }
}
