package com.perback.perback.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment
{
	protected BaseActivityFragments activityFragments;

	protected ViewHolder views;
	private Intent resultData;
	private int resultCode = Activity.RESULT_CANCELED;
	private OnResultListener onResultListener;
	
	public BaseFragment() {
		this(null);
	}
	
	public BaseFragment(OnResultListener onResultListener)
	{
		super();
		this.onResultListener = onResultListener;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return getLayoutResId() != View.NO_ID ? inflater.inflate(getLayoutResId(), container, false) : null;
	}
	
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (getActivity() instanceof BaseActivityFragments)
			activityFragments = (BaseActivityFragments) getActivity();

		linkUI();
		init();
		setData();
		setActions();
	}

	abstract protected int getLayoutResId();

	protected void linkUI()
	{
		views = new ViewHolder(getView());
	}

	protected void init() {}

	protected void setData() {}

	protected void setActions() {}
	
	protected String getTitle()
	{
		return getClass().getSimpleName();
	}
	
	protected void setResult(int resultCode, Intent data)
	{
		this.resultCode = resultCode;
		this.resultData = data;
	}
	
	public void remove()
	{
		if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
			getActivity().getSupportFragmentManager().popBackStack();
		else
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}
	
	public void onDetach()
	{
		super.onDetach();
		if (onResultListener != null)
			onResultListener.onResult(resultCode, resultData);
	}
}