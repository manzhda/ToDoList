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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
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
import com.agileengine.leadandroidtesttask.todolist.framework.fragment.base.RecycleCursorFragment;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.OnStartDragListener;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.SimpleItemTouchHelperCallback;

import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;


public class ToDoListFragment extends RecycleCursorFragment {

    private static final int TODO_ITEMS_LOADER_ID = 0;
    private ToDoItemAdapter mToDoItemAdapter;

    private static final int REQUEST_CODE = 1000;
    private SearchView mSearchView;
    private String mQuery;
    private ItemTouchHelper mItemTouchHelper;

    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

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
        mToDoItemAdapter = new ToDoItemAdapter(getActivity(), new ToDoItemAdapter.ViewHolder.ClickListener() {
            @Override
            public void onItemClicked(int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                } else {
                    AddEditToDoItemActivity.goThereForResult(getActivity(), mToDoItemAdapter.getItem(position), REQUEST_CODE);
                }
            }

            @Override
            public boolean onItemLongClicked(int position) {
//                if (actionMode == null) {
//                    actionMode = getActivity().startActionMode(actionModeCallback);
//                    getToolbar().setVisibility(View.GONE);
//                }
//
//                toggleSelection(position);
//
                return true;
            }
        }, null);

        mToDoItemAdapter.setOnStartDragListener(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                if(actionMode != null){
                    actionMode.finish();
                }
                mItemTouchHelper.startDrag(viewHolder);
            }

            @Override
            public void onLongPress(int position) {
                if (actionMode == null) {
                    actionMode = getActivity().startActionMode(actionModeCallback);
                    getToolbar().setVisibility(View.GONE);
                }

                toggleSelection(position);

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setItemAnimator(new FlipInLeftYAnimator());
        mRecyclerView.setAdapter(mToDoItemAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mToDoItemAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);


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

    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        mToDoItemAdapter.toggleSelection(position);
        int count = mToDoItemAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.menu_todo_context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    // TODO: actually remove items
                    Log.d(TAG, "menu_remove");
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mToDoItemAdapter.clearSelection();
            getToolbar().setVisibility(View.VISIBLE);
            actionMode = null;
        }
    }

}
