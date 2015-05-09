package com.perback.perback.base;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;

public abstract class BaseActivityFragments extends BaseActivity
{
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		fragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
			public void onBackStackChanged()
			{
				List<Fragment> fragments = fragmentManager.getFragments();
				Fragment currentFragment = null;
				for (int i = fragments.size()-1; i >=0 ; i--)
				{
					currentFragment = fragments.get(i);
					if (currentFragment != null)
						break;
				}
				if (currentFragment != null)
					currentFragment.onResume();
			}
		});
	}
	
	abstract protected int getFragmentContainerId();
	
	public void setFragment(BaseFragment fragment)
	{
		fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		fragmentManager.beginTransaction().replace(getFragmentContainerId(), fragment).commit();
	}
	
	public void addFragment(BaseFragment fragment)
	{
		fragmentManager.beginTransaction().add(getFragmentContainerId(), fragment).addToBackStack(fragment.getTitle()).commit();
	}
	
	public void onBackPressed()
	{
		if (fragmentManager.getBackStackEntryCount() > 0)
			fragmentManager.popBackStack();
		else
			super.onBackPressed();
	}
}