package com.perback.perback.x_base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

public abstract class BaseActivity extends FragmentActivity
{
	protected FragmentManager fragmentManager;
	protected ViewHolder views;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		fragmentManager = getSupportFragmentManager();
		if (getLayoutResId() != View.NO_ID)
			setContentView(getLayoutResId());

		linkUI();
		init();
		setData();
		setActions();
	}

	abstract protected int getLayoutResId();

	protected void linkUI()
	{
		views = new ViewHolder(findViewById(android.R.id.content));
	}

	protected void init() {}

	protected void setData() {}

	protected void setActions() {}
	
	public void setContentView(View view)
	{
		super.setContentView(view);
		linkUI();
	}

	public void showMessage(String message, DialogInterface.OnClickListener clickListener) {
	}
}