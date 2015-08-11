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

package com.agileengine.leadandroidtesttask.todolist.framework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.OnBackButtonListener;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.ToolbarGetter;

import butterknife.ButterKnife;


public abstract class ContentActivitySupport<T extends Fragment> extends AppCompatActivity implements ToolbarGetter {

    protected Toolbar mToolBar;

    protected T mContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.inject(this);
        if (savedInstanceState == null) {
            mContentFragment = onCreateContentFragment();
            if(mContentFragment!=null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mContentFragment)
                        .commit();
            }
        } else {
            mContentFragment = (T) getSupportFragmentManager().findFragmentById(R.id.container);
        }
        mToolBar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if(mToolBar != null) {
            mToolBar.setTitle(getTitle());
            setSupportActionBar(mToolBar);
            checkIfBackNeeded(mContentFragment);
            onInitToolBar(mToolBar);
        }
    }

    protected void onInitToolBar(Toolbar toolBar) {
        // If ancestor what customize toolbar they need override this method.
    }

    protected int getLayoutId() {
        return R.layout.activity_tool_container;
    }

    protected abstract T onCreateContentFragment();

    protected void checkIfBackNeeded(Fragment frg) {
        if (frg instanceof OnBackButtonListener) {
            if (((OnBackButtonListener) frg).isBackButtonNeeded()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        T frg = getContentFragment();
        if (!(frg instanceof OnBackButtonListener) || !((OnBackButtonListener) frg).onBackButtonPressed()) {
            super.onBackPressed();
        }
    }

    public T getContentFragment() {
        return mContentFragment;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    protected static void onAppear(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    };

    protected static void onDisappear(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    };
}
