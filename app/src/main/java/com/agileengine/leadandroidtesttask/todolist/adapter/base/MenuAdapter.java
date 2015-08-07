package com.agileengine.leadandroidtesttask.todolist.adapter.base;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.framework.utils.ResourceUtils;

public class MenuAdapter extends BaseAdapter {

    private final String[] mItems;
    private int[] mIconIds;
    private Context mCtx;
    private int mListItemLayoutId;

    public MenuAdapter(Context context, int layoutId, int titlesId, int iconsId) {
        mCtx = context;
        mListItemLayoutId = layoutId;
        Resources resources = context.getResources();
        mItems = resources.getStringArray(titlesId);
        mIconIds = ResourceUtils.getIds(context, iconsId);
    }

    public MenuAdapter(Context context, int layoutId, int titlesId) {
        mCtx = context;
        mListItemLayoutId = layoutId;
        Resources resources = context.getResources();
        mItems = resources.getStringArray(titlesId);
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public String getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mCtx, mListItemLayoutId, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.item_text);
        if (item == null) {
            throw new IllegalArgumentException("Layout should have TextView child with @id/item_text");
        }

        item.setText(mItems[position]);
        if (mIconIds != null) {
            item.setCompoundDrawablesWithIntrinsicBounds(mIconIds[position], 0, 0, 0);
        }

        return convertView;
    }

    public boolean isMultipleChoiceMode() {
        return false;
    }
}
