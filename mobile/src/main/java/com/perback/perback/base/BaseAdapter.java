package com.perback.perback.base;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class BaseAdapter<T> extends ArrayAdapter<T>
{
	protected Context context;
	private LayoutInflater layoutInflater;
	private int layoutResId;
	
	public BaseAdapter(Context context, int layoutResId, ArrayList<T> dataSet)
	{
		super(context, 0, dataSet);
		this.context = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResId = layoutResId;
	}
	
	public View getView(int pos, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(layoutResId, null);
			convertView.setTag(new ViewHolder(convertView));
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		setData(viewHolder, getItem(pos), pos);
		setActions(viewHolder, getItem(pos), pos);
		return convertView;
	}
	
	protected abstract void setData(ViewHolder views, T item, int pos);
	
	protected void setActions(ViewHolder views, final T item, final int pos) {}
}