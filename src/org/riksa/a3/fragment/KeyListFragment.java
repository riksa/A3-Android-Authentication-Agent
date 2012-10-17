/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.riksa.a3.R;
import org.riksa.a3.activity.CreateKeyPairActivity;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 17.10.2012
 * Time: 18:23
 */
public class KeyListFragment extends ListFragment {
    private static final Logger log = LoggerFactory.getLogger(KeyListFragment.class);
    private static final int CREATE_KEY_INTENT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keylist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, new String[]{"Key 1", "Key 2"}));
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_key_list, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_key_list_context, menu);
    }

    private <T extends View> T findView(Class<? extends View> clazz, int id) {
        View view = getActivity().findViewById(id);
        if (view != null && view.getClass().isAssignableFrom(clazz)) {
            return (T) view;
        }
        log.error("Cannot find view of class {} with id {}", clazz.toString(), id);
        return null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                log.debug("TODO: delete key");
                break;
            case R.id.menu_copy_public_key:
                log.debug("TODO: copy public key");
                break;
            default:
                log.warn("Unhandled menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                startKeyCreation();
                break;
            case R.id.menu_import:
                log.debug("TODO: import key");
                break;
            default:
                log.warn("Unhandled menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    private void startKeyCreation() {
        // for now, just a separate activity. fragments later (maybe?)
        Intent intent = new Intent( getActivity(), CreateKeyPairActivity.class );
        startActivityForResult( intent, CREATE_KEY_INTENT );

    }

    void showDetails(int index) {
        log.debug("TODO: showDetails {} ", index);
    }
}
