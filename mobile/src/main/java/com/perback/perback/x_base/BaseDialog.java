package com.perback.perback.x_base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Base class for Dialogs.
 * : Autolinks views.
 * : Common UI component structure.
 * : Common data communication support.
 */
public abstract class BaseDialog extends DialogFragment
{
	/**
	 * Reference for the parent BaseActivity for quick access.
	 */
	protected BaseActivity parentActivity;
	/**
	 * ViewHolder for accessing the views in the layout.
	 */
	protected ViewHolder views;
	private Intent resultData;
	private int resultCode = Activity.RESULT_CANCELED;
	private OnResultListener onResultListener;
	
	public BaseDialog() {
		this(null);
	}
	
	/**
	 * If needed, override and call 'super' with the required parameters.
	 * @param onResultListener Listener to be invoked when the dialog dismisses.
	 */
	public BaseDialog(OnResultListener onResultListener)
	{
		super();
		this.onResultListener = onResultListener;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		return getLayoutResId() != View.NO_ID ? inflater.inflate(getLayoutResId(), container, false) : null;
	}
	
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		parentActivity = (BaseActivity) getActivity();

		linkUI();
		init();
		setData();
		setActions();
	}

	/**
	 * Implement to return the resource id of the parent layout.
	 * @return Parent layout resource id
	 */
	abstract protected int getLayoutResId();
	/**
	 * Links views to the 'views' object. Override if you need to include other 'findViewById' calls.
	 */
	protected void linkUI()
	{
		views = new ViewHolder(getView());
	}
	/**
	 * Override to initialize class members or perform other prerequisite operations.
	 */
	protected void init() {}
	/**
	 * Override to populate or set up views with existing data.
	 */
	protected void setData() {}
	/**
	 * Override to add callbacks to actions/events.
	 */
	protected void setActions() {}
	
	/**
	 * Sets the result for the OnResultListener to be called with.
	 * @param resultCode The result code, often OnResultListener.RESULT_CANCELED or OnResultListener.RESULT_OK
	 */
	protected void setResult(int resultCode, Intent data)
	{
		this.resultCode = resultCode;
		this.resultData = data;
	}
	
	public void onDetach()
	{
		super.onDetach();
		if (onResultListener != null)
			onResultListener.onResult(resultCode, resultData);
	}
}