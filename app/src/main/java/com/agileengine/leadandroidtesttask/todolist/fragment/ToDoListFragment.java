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

package com.agileengine.leadandroidtesttask.todolist.fragment;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.agileengine.leadandroidtesttask.todolist.App;
import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.activity.AddEditToDoItemActivity;
import com.agileengine.leadandroidtesttask.todolist.adapter.ToDoItemAdapter;
import com.agileengine.leadandroidtesttask.todolist.adapter.base.CursorRecyclerViewAdapter;
import com.agileengine.leadandroidtesttask.todolist.db.table.ToDoItemTable;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.OnItemClickListener;
import com.agileengine.leadandroidtesttask.todolist.framework.fragment.base.RecycleCursorFragment;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;


public class ToDoListFragment extends RecycleCursorFragment {

    private static final int TODO_ITEMS_LOADER_ID = 0;
    private ToDoItemAdapter mToDoItemAdapter;

    private static final int REQUEST_CODE = 1000;
    private SearchView mSearchView;
    private String mQuery;

    @Override
    protected int getTargetLayout() {
        return R.layout.fragment_to_do_list;
    }

    @Override
    protected CursorLoader getCursorLoader(int id, Bundle args) {
        if(mQuery != null){

            String s = "%" + mQuery + "%";
            String[] selectionArg = {s, s};

            String selection = ToDoItemTable.Cols.TITLE + " LIKE ? OR " + ToDoItemTable.Cols.TAGS + " LIKE ?";

            return App.getToDoApi().getToDoItems(selection, selectionArg, ToDoItemTable.Cols.URGENT + " DESC");
        } else {
            return App.getToDoApi().getToDoItems(null, null, ToDoItemTable.Cols.URGENT + " DESC");
        }
    }

    @Override
    protected CursorRecyclerViewAdapter getAdapter() {
        return mToDoItemAdapter;
    }

    @Override
    protected void initComponents(LayoutInflater inflater, View root, Bundle savedInstanceState) {
        mToDoItemAdapter = new ToDoItemAdapter(getActivity(), null);

        mToDoItemAdapter.setToDoItemClickListener(new OnItemClickListener<ToDoItem>() {
            @Override
            public void onItemClicked(View view, ToDoItem item, int position) {
                AddEditToDoItemActivity.goThereForResult(getActivity(), item, REQUEST_CODE);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mToDoItemAdapter);


        FloatingActionButton createNew = (FloatingActionButton)  root.findViewById(R.id.fb_create_new_todo_item);
        createNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewToDoItem();
            }
        });

        getActivity().getLoaderManager().initLoader(TODO_ITEMS_LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_to_do_list, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            mSearchView = (SearchView) searchItem.getActionView();
            if (mSearchView == null) {
                Log.w(TAG, "Could not set up search view, view is null.");
            } else {
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        // Don't care about this.
//                        mSearchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        mQuery = !TextUtils.isEmpty(s) ? s : null;
                        getLoaderManager().restartLoader(TODO_ITEMS_LOADER_ID, null, ToDoListFragment.this);
                        return true;
                    }
                });

                mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackButtonPressed() {
        if(!mSearchView.isIconified()){
            mSearchView.setIconified(true);
            return true;
        } else {
            return super.onBackButtonPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_CODE == requestCode){
            if(Activity.RESULT_OK == resultCode){
                // TODO Update items on the screen.
            } else {
                // Do nothing
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createNewToDoItem() {
        AddEditToDoItemActivity.goThereForResult(getActivity(), null, REQUEST_CODE);
    }
}
