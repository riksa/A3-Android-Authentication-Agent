/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.app.Activity;
import android.content.Context;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import org.riksa.a3.R;
import org.riksa.a3.model.A3Key;

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
    private static final String KEYNAME = "name";
    private static final String KEYTYPE = "type";
    private static final String KEYSTRENGTH = "strength";
    private static final String[] from = new String[]{KEYNAME, KEYTYPE, KEYSTRENGTH};
    private static final int[] to = new int[]{R.id.col_keyname, R.id.col_keytype, R.id.col_keystrength};

    public static ListAdapter create(Context context, List<A3Key> a3Keys) {
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
        for (A3Key key : a3Keys) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put(KEYNAME, key.getKeyName());
            entry.put(KEYTYPE, key.getKeyType().toString());
            entry.put(KEYSTRENGTH, Integer.toString(key.getKeyStrength()));
            data.add(entry);
        }
        return new KeyListSimpleAdapter(context, data, R.layout.row_keylist, from, to);
    }

    private KeyListSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

}
