package com.perback.perback.x_base;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Base class for a splash screen Activity.
 * : Manages background operations in relation to a minimum splash screen display time.
 */
public abstract class BaseActivitySplash extends BaseActivity
{
	private int lockCount;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		ArrayList<Runnable> operations = getOperations();
		lockCount = 1 + operations.size();
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				operationDone();
			}
		}, getTimeOut());
		
		for (Runnable operation : operations)
			operation.run();
	}
	
	/**
	 * Call to signal a background operation is done.
	 */
	protected void operationDone()
	{
		lockCount--;
		if (lockCount == 0)
			onAllOperationsDone();
	}
	
	/**
	 * Called when all operations are done.
	 * By default, starts an Activity of the type returned by 'getNextActivity', and finishes this one.
	 */
	protected void onAllOperationsDone()
	{
		if (!isFinishing())
		{
			startActivity(new Intent(this, getNextActivity()));
			finish();
		}
	}
	
	/**
	 * Override to return the minimum desired splash screen display time.
	 * @return The minimum splash screen time in miliseconds.
	 */
	protected int getTimeOut() {
		return 0;
	}
	/**
	 * Override to return the set of operations to be performed in the background during the splash screen.
	 * @return The set of background operations.
	 */
	protected ArrayList<Runnable> getOperations() {
		return new ArrayList<Runnable>();
	}
	/**
	 * Override to return the type of the Activity to be launched when the splash screen in finished.
	 * @return The class of the next activity.
	 */
	protected Class<? extends Activity> getNextActivity() {
		return null;
	}
	
	public void onBackPressed() {}
}