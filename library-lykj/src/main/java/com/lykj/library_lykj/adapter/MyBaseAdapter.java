package com.lykj.library_lykj.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter<T> {
	protected List<T> data;

	public MyBaseAdapter(Context context, List<T> data) {
		super(context);
		this.data = data;
	}

	public void setList(List<T> data) {
		this.data = data;
	}

	public void refresh(List<T> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setOnClickListener(View v, int position) {
		if (getListener() != null) {
			v.setOnClickListener(new ViewOnClick(getListener(), getItem(position), position));
		}
	}
}
