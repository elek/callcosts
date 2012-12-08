package net.anzix.callcost.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MultiListItemAdapter extends BaseAdapter {

	private List<Map<Integer, String>> results = new ArrayList<Map<Integer, String>>();
	private final LayoutInflater mInflater;
	private int layout;

	public MultiListItemAdapter(List<Map<Integer, String>> values, int layout,
			Activity parent) {
		this.results = values;
		this.mInflater = LayoutInflater.from(parent);
		this.layout = layout;
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
		Map<Integer, String> result = results.get(i);
		if (view == null) {
			view = mInflater.inflate(layout, vg, false);

		}
		for (Integer key : result.keySet()) {
			((TextView) view.findViewById(key)).setText(result.get(key));
		}

		view.requestLayout();
		return view;
	}

}
