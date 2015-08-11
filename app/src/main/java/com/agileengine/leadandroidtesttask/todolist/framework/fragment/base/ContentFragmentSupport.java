/*
 * Copyright (c) 2015. Dmitriy Manzhosov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agileengine.leadandroidtesttask.todolist.framework.fragment.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agileengine.leadandroidtesttask.todolist.framework.callback.OnActionReceived;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.OnBackButtonListener;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.ToolbarGetter;

import java.util.HashMap;

import butterknife.ButterKnife;

public abstract class ContentFragmentSupport extends Fragment implements OnBackButtonListener {

    public static final String TAG = ContentFragmentSupport.class.getSimpleName();

    private LocalBroadcastManager mLocalBroadcastManager;

    private BroadcastReceiver mBroadcastReceiver;

    private IntentFilter mIntentFilter;

    private HashMap<String, OnActionReceived> mActionMap = new HashMap<String, OnActionReceived>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getTargetLayout(), container, false);
        ButterKnife.inject(this, root);
        initComponents(inflater, root, savedInstanceState);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(activity);
        super.onAttach(activity);
        initReceiver();
    }

    protected void initReceiver() {
        mBroadcastReceiver = getBroadcastReceiver();
    }

    protected void listenToAction(String action, OnActionReceived callback) {
        mActionMap.put(action, callback);
        updateBroadcastActionList();
    }

    protected void stopListenToAction(String action) {
        mActionMap.remove(action);
        updateBroadcastActionList();
    }

    public void updateBroadcastActionList() {
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        mIntentFilter = new IntentFilter();
        for (String commandName : mActionMap.keySet()) {
            mIntentFilter.addAction(commandName);
        }
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null) {
                    OnActionReceived onActionReceived = mActionMap.get(action);
                    if (onActionReceived != null) {
                        onActionReceived.onReceive(action, intent);
                    }
                }
            }
        };
    }

    @Override
    public void onResume() {
        if (mBroadcastReceiver != null && mActionMap.size() > 0) {
            mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (mBroadcastReceiver != null && mActionMap.size() > 0) {
            mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        }
        super.onStop();
    }

    @LayoutRes
    protected abstract int getTargetLayout();

    protected abstract void initComponents(LayoutInflater inflater, View root, Bundle savedInstanceState);

    @Override
    public boolean onBackButtonPressed() {
        return false;
    }

    @Override
    public boolean isBackButtonNeeded() {
        return false;
    }

    public void setTitle(String title) {
        if (getActivity() instanceof ToolbarGetter) {
            ((ToolbarGetter) getActivity()).getToolBar().setTitle(title);
        }
    }

    public Toolbar getToolbar() {
        if (getActivity() instanceof ToolbarGetter) {
            return ((ToolbarGetter) getActivity()).getToolBar();
        }
        return null;
    }
}
