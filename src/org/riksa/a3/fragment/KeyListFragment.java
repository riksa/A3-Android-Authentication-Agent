/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.riksa.a3.R;
import org.riksa.a3.activity.CreateKeyPairActivity;
import org.riksa.a3.activity.ImportKeyPairActivity;
import org.riksa.a3.model.A3Key;
import org.riksa.a3.model.KeyChain;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.security.KeyStoreException;
import java.util.List;

/**
 * User: riksa
 * Date: 17.10.2012
 * Time: 18:23
 */
public class KeyListFragment extends ListFragment {
    private static final Logger log = LoggerFactory.getLogger(KeyListFragment.class);
    private static final int CREATE_KEY_INTENT = 1;
    private static final int IMPORT_KEY_INTENT = 2;

    Runnable setKeysRunnable = new Runnable() {
        public void run() {
            setKeys(keyChain.getUnmodifiableKeys());
        }
    };

    KeyChain.KeyChainListener keyChainListener = new KeyChain.KeyChainListener() {
        public void keyChainChanged() {
            log.debug("keyChainChanged, keyCount={}", keyChain.getUnmodifiableKeys().size());
            getActivity().runOnUiThread(setKeysRunnable);
        }
    };
    private KeyChain keyChain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keylist, container, false);
        try {
            keyChain = KeyChain.getInstance(getActivity());
            keyChain.addListener(keyChainListener);
        } catch (KeyStoreException e) {
            log.error(e.getMessage(), e);
            Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_LONG ).show();
            getActivity().finish();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        keyChain.removeListener(keyChainListener);
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setKeys(keyChain.getUnmodifiableKeys());
    }

    private void setKeys(List<A3Key> keys) {
        log.debug("setKeys, size={}", keys.size());
        setListAdapter(KeyListSimpleAdapter.create(getActivity(), keys));
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
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
        KeyListSimpleAdapter adapter = (KeyListSimpleAdapter) getListAdapter();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String id = adapter.getKeyIdAtPosition(menuInfo.position);
        A3Key key = keyChain.getKeyWithId(id);

        switch (item.getItemId()) {
            case R.id.menu_delete:
                keyChain.removeKey(key);
                break;
            case R.id.menu_copy_public_key:
                if (key != null) {
                    log.debug("Public key={}", key.getPublicKey());
                }
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
                startKeyImport();
                break;
            default:
                log.warn("Unhandled menu item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    private void startKeyCreation() {
        // for now, just a separate activity. fragments later (maybe?)
        Intent intent = new Intent(getActivity(), CreateKeyPairActivity.class);
        startActivityForResult(intent, CREATE_KEY_INTENT);

    }

    private void startKeyImport() {
        // for now, just a separate activity. fragments later (maybe?)
        Intent intent = new Intent(getActivity(), ImportKeyPairActivity.class);
        startActivityForResult(intent, IMPORT_KEY_INTENT);

    }

    void showDetails(int index) {
        log.debug("TODO: showDetails {} ", index);
    }
}
