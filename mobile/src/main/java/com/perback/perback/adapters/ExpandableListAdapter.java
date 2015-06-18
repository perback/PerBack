package com.perback.perback.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perback.perback.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ralucapostelnicu on 16/06/15.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;

    /*
     * Here we have a Hashmap containing a String key
     * (can be Integer or other type but I was testing
     * with contacts so I used contact name as the key)
    */
    private HashMap<String, List<String>> mListDataChild;

    // ArrayList that is what each key in the above
    // hashmap points to
    private ArrayList<String> mListDataGroup;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;

    private HashMap<Integer, Boolean> mGroupCheckStates;

    // Our getChildView & getGroupView use the viewholder patter
    // Here are the viewholders defined, the inner classes are
    // at the bottom
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    /*
          *  For the purpose of this document, I'm only using a single
     *	textview in the group (parent) and child, but you're limited only
     *	by your XML view for each group item :)
    */
    private String groupText;
    private String childText;

    /*  Here's the constructor we'll use to pass in our calling
     *  activity's context, group items, and child items
    */
    public ExpandableListAdapter(Context context,
                                          ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild) {

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;

        // Initialize our hashmap containing our check states here
        mChildCheckStates = new HashMap<Integer, boolean[]>();
        mGroupCheckStates = new HashMap<Integer, Boolean>();
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    /*
     * This defaults to "public object getGroup" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public Object getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListGroupItems".
        //  Here is where I call the getter to get that text
        groupText = (String)getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);

            // Initialize the GroupViewHolder defined at the bottom of this document
            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.tv_header);

            groupViewHolder.mGroupCheckBox = (CheckBox) convertView
                    .findViewById(R.id.chk_group);

            groupViewHolder.mGroupLinearLayout = (LinearLayout) convertView.findViewById(R.id.ll_chk_group);

            convertView.setTag(groupViewHolder);
        } else {

            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);

        groupViewHolder.mGroupCheckBox.setOnCheckedChangeListener(null);

        if (mGroupCheckStates.containsKey(groupPosition)) {
			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
			 * the value of the parent view (group) of this child (aka, the key),
			 * then retrive the boolean array getChecked[]
			*/
            boolean getChecked = mGroupCheckStates.get(groupPosition);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            groupViewHolder.mGroupCheckBox.setChecked(getChecked);

            if(getChecked)
                setColorToGroupCheckbox(groupPosition,  groupViewHolder.mGroupLinearLayout);
            else
                groupViewHolder.mGroupLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        } else {

			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
			 * contain the value of the parent view (group) of this child (aka, the key),
			 * (aka, the key), then initialize getChecked[] as a new boolean array
			 *  and set it's size to the total number of children associated with
			 *  the parent group
			*/
            boolean getChecked = false;

            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
            mGroupCheckStates.put(groupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            groupViewHolder.mGroupCheckBox.setChecked(false);
        }


        groupViewHolder.mGroupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = new boolean[mListDataGroup.get(groupPosition).length()];
                    for(int i=0; i<mListDataGroup.get(groupPosition).length(); i++) {
                        getChecked[i] = isChecked;
                        mChildCheckStates.put(groupPosition, getChecked);
                    }
                    mGroupCheckStates.put(groupPosition,isChecked);

                    notifyDataSetChanged();
                    Toast.makeText(mContext, "group " + groupPosition, Toast.LENGTH_SHORT).show();

                } else {

                    boolean getChecked[] = new boolean[mListDataGroup.get(groupPosition).length()];
                    for(int i=0; i<mListDataGroup.get(groupPosition).length(); i++) {
                        getChecked[i] = isChecked;
                        mChildCheckStates.put(groupPosition, getChecked);
                    }
                    mGroupCheckStates.put(groupPosition,isChecked);
                    notifyDataSetChanged();

                }
            }
        });

        return convertView;
    }

    public HashMap<Integer, boolean[]> getmChildCheckStates() {
        return mChildCheckStates;
    }

    public void setmChildCheckStates(HashMap<Integer, boolean[]> mChildCheckStates) {
        this.mChildCheckStates = mChildCheckStates;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
    }

    /*
     * This defaults to "public object getChild" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListChildItems".
        //  Here is where I call the getter to get that text
        childText = (String) getChild(mGroupPosition, mChildPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);

            childViewHolder = new ChildViewHolder();

            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.tv_item);

            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.chk_item);

            convertView.setTag(R.layout.list_item, childViewHolder);

        } else {

            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.list_item);
        }

        childViewHolder.mChildText.setText(childText);

		/*
		 * You have to set the onCheckChangedListener to null
		 * before restoring check states because each call to
		 * "setChecked" is accompanied by a call to the
		 * onCheckChangedListener
		*/
        childViewHolder.mCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
			 * the value of the parent view (group) of this child (aka, the key),
			 * then retrive the boolean array getChecked[]
			*/
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);

            if(getChecked[mChildPosition])
                setDrawableToCheckboxButton(mGroupPosition, childViewHolder.mCheckBox);
            else
                childViewHolder.mCheckBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_light_grey_24dp));

        } else {

			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
			 * contain the value of the parent view (group) of this child (aka, the key),
			 * (aka, the key), then initialize getChecked[] as a new boolean array
			 *  and set it's size to the total number of children associated with
			 *  the parent group
			*/
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
            mChildCheckStates.put(mGroupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.mCheckBox.setChecked(false);
            childViewHolder.mCheckBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_light_grey_24dp));

        }

        childViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    Toast.makeText(mContext, "tem " + mChildPosition + " from group " + mGroupPosition, Toast.LENGTH_SHORT).show();
                    setDrawableToCheckboxButton(mGroupPosition, childViewHolder.mCheckBox);

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    childViewHolder.mCheckBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_light_grey_24dp));
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void setColorToGroupCheckbox(int position, LinearLayout llChkGroup){
        switch (position){
            case 0:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.light_orange));
                break;
            case 1:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.dark_blue));
                break;
            case 2:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
                break;
            case 3:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.light_green));
                break;
            case 4:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
                break;
            case 5:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.turquoise));
                break;
            case 6:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
                break;
            case 7:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                break;
            case 8:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
                break;
            case 9:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                break;
            case 10:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.pink));
                break;
            case 11:
                llChkGroup.setBackgroundColor(mContext.getResources().getColor(R.color.dark_red));
                break;

            default:
                break;
        }
    }

    private void setDrawableToCheckboxButton(int mGroupPosition, CheckBox checkBox){

        switch (mGroupPosition){
            case 0:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_orange_24dp));
                break;
            case 1:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_dark_blue_24dp));
                break;
            case 2:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_light_blue_24dp));
                break;
            case 3:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_light_green_24dp));
                break;
            case 4:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_purple_24dp));
                break;
            case 5:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_turquoise_24dp));
                break;
            case 6:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_light_orange_24dp));
                break;
            case 7:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_green_24dp));
                break;
            case 8:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_yellow_24dp));
                break;
            case 9:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_blue_24dp));
                break;
            case 10:
                checkBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_pink_24dp));
                break;
            default:
                break;

        }



    }


    public final class GroupViewHolder {

        TextView mGroupText;
        CheckBox mGroupCheckBox;
        LinearLayout mGroupLinearLayout;
    }

    public final class ChildViewHolder {

        TextView mChildText;
        CheckBox mCheckBox;
    }
}