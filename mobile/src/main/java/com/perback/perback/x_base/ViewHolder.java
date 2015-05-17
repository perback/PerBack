package com.perback.perback.x_base;

import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;


public class ViewHolder
{
	private SparseArray<View> views;
	
	public ViewHolder(View root)
	{
		views = new SparseArray<View>();
		linkViews(root);
	}
	
	private void linkViews(View root)
	{
		if (root.getId() != View.NO_ID)
			views.append(root.getId(), root);
		if (root instanceof ViewGroup)
			for (int i = 0; i < ((ViewGroup)root).getChildCount(); i++)
				linkViews(((ViewGroup)root).getChildAt(i));
	}
	
	public View get(int id) {
		return views.get(id);
	}
	public ViewGroup getViewGroup(int id) {
		return (ViewGroup) views.get(id);
	}
	@SuppressWarnings("unchecked")
	public AdapterView<Adapter> getAdapterView(int id) {
		return (AdapterView<Adapter>) views.get(id);
	}
	public ViewPager getViewPager(int id) {
		return (ViewPager) views.get(id);
	}
	public RadioGroup getRadioGroup(int id) {
		return (RadioGroup) views.get(id);
	}
	public TextView getTextView(int id) {
		return (TextView) views.get(id);
	}
	public EditText getEditText(int id) {
		return (EditText) views.get(id);
	}
	public Button getButton(int id) {
		return (Button) views.get(id);
	}
	public CompoundButton getCompoundButton(int id) {
		return ((CompoundButton) views.get(id));
	}
	public ProgressBar getProgressBar(int id) {
		return (ProgressBar) views.get(id);
	}
	public ImageView getImageView(int id) {
		return (ImageView) views.get(id);
	}
	public WebView getWebView(int id) {
		return (WebView) views.get(id);
	}
	public VideoView getVideoView(int id) {
		return (VideoView) views.get(id);
	}
}