package com.agileengine.leadandroidtesttask.todolist.framework.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListAdapter;
import android.widget.ListView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ViewHelper {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Return measured width
     */

    public static int setListViewWidthBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int maxWidth = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            int measuredWidth = listItem.getMeasuredWidth();
            if (maxWidth < measuredWidth) {
                maxWidth = measuredWidth;
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.width = maxWidth + listView.getPaddingLeft() + listView.getPaddingRight();
        listView.setLayoutParams(params);
        listView.requestLayout();
        return params.width;
    }

    public static void showView(final View view, Animation animation) {

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(VISIBLE);
                view.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    public static void hideView(final View view, Animation animation) {

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(INVISIBLE);
                view.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    public static void show(View view, boolean show) {
        if (show) {
            if (view.getVisibility() != VISIBLE) {
                view.setVisibility(VISIBLE);
            }
        } else {
            view.setVisibility(GONE);
        }
    }

    public static final void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static final void gone(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * Enables/Disables all child views in a view group.
     *
     * @param viewGroup the view group
     * @param enable    <code>true</code> to enable, <code>false</code> to disable
     *                  the views.
     */
    private static void enable(ViewGroup viewGroup, boolean enable) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                enable((ViewGroup) child, enable);
            } else {
                child.setEnabled(enable);
            }
        }
    }

    public static void enable(View view, boolean enable) {
        if (view instanceof ViewGroup) {
            enable((ViewGroup) view, enable);
        } else {
            view.setEnabled(enable);
        }
    }
}