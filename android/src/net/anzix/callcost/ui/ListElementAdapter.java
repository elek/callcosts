package net.anzix.callcost.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.anzix.callcost.R;

import java.util.List;

public class ListElementAdapter extends BaseAdapter {

    private boolean allLink = false;

    private List<MemberElement> items;

    private final Context context;


    public ListElementAdapter(Context context, List<MemberElement> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        MemberElement e = items.get(position);
        if (e instanceof ListElement) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return allLink;
    }

    @Override
    public boolean isEnabled(int position) {
        MemberElement e = items.get(position);
        return allLink || (e instanceof ListElement && ((ListElement) e).getLink() != null);
//        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        MemberElement p = items.get(position);

        //if (v == null) {
        LayoutInflater vi = LayoutInflater.from(context);
        if (p instanceof SeparatorElement) {
            v = vi.inflate(R.layout.separator, null);
//            v.setClickable(false);
//            v.setEnabled(false);
        } else {
            v = vi.inflate(R.layout.triple_list_item, null);
//            v.setClickable(false);
//            v.setEnabled(false);
        }
        //}


        if (p instanceof ListElement) {
            ListElement le = (ListElement) p;
            ((TextView) v.findViewById(R.id.label)).setText(le.getLabel());
            ((TextView) v.findViewById(R.id.description)).setText(le.getDescription());
            ((TextView) v.findViewById(R.id.value)).setText(le.getValue());
            Log.i("xxxv", v.toString() + " " + le.getLabel());
            if (le.getLink() != null) {
                Log.i("xxx", le.getLink().toString());
                ((ImageView) v.findViewById(R.id.arrow)).setVisibility(View.VISIBLE);
//                v.setClickable(true);
//                v.setEnabled(true);
            }
        } else if (p instanceof SeparatorElement) {
            SeparatorElement se = (SeparatorElement) p;
            ((TextView) v.findViewById(R.id.separator)).setText(se.getLabel());
        }

        return v;
    }

    public void setAllLink(boolean allLink) {
        this.allLink = allLink;
    }
}
