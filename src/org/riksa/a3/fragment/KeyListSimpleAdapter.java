/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher;
import org.riksa.a3.R;
import org.riksa.a3.model.A3Key;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: riksa
 * Date: 10/18/12
 * Time: 2:59 PM
 */
public class KeyListSimpleAdapter extends SimpleAdapter {
    private static final Logger log = LoggerFactory.getLogger(SimpleAdapter.class);
    private static final String KEYID = "id";
    private static final String KEYNAME = "name";
    private static final String KEYTYPE = "type";
    private static final String KEYSTRENGTH = "strength";
    private static final String[] from = new String[]{KEYNAME, KEYTYPE, KEYSTRENGTH};
    private static final int[] to = new int[]{R.id.col_keyname, R.id.col_keytype, R.id.col_keystrength};

    public static ListAdapter create(Context context, List<A3Key> a3Keys) {
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        for (A3Key key : a3Keys) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put(KEYID, key.getKeyId());
            entry.put(KEYNAME, key.getKeyName());
            entry.put(KEYTYPE, key.getKeyType().toString());
            entry.put(KEYSTRENGTH, Integer.toString(key.getKeyStrength()));
            data.add(entry);
        }
        return new KeyListSimpleAdapter(context, data, R.layout.row_keylist, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Map<String, Object> entry = (Map<String, Object>) getItem(position);
        if (A3Key.Type.UNKNOWN.toString().equals(entry.get(KEYTYPE))) {
            switchToProgress(view, true);
        } else {
            switchToProgress(view, false);
        }
        return view;
    }

    private void switchToProgress(View view, boolean showProgress) {
        log.debug("switchToProgress {}", showProgress);
        ViewSwitcher viewSwitcher = (ViewSwitcher) view.findViewById(R.id.col_iconswitcher);
        View progress = view.findViewById(R.id.col_progress);
        if (viewSwitcher != null && progress != null) {
            log.debug("Got views");
            if (!progress.isShown() ^ showProgress) {
                log.debug("Flipping");
                viewSwitcher.showPrevious();
            }
        }
    }

    public String getKeyIdAtPosition( int position ) {
        Map<String, Object> entry = (Map<String, Object>) getItem( position );
        return (String) entry.get(KEYID);
    }


    private KeyListSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

}
