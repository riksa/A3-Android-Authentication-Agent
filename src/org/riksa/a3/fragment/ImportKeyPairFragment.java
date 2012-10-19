/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.riksa.a3.R;
import org.riksa.a3.exception.ViewNotFoundException;
import org.riksa.a3.util.A3Utils;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 19.10.2012
 * Time: 20:15
 */
public class ImportKeyPairFragment extends Fragment {
    private static final Logger log = LoggerFactory.getLogger(ImportKeyPairFragment.class);
    private static final int BROWSE_PRIVATE = 1;
    private static final int BROWSE_PUBLIC = 2;
    public static final String ORG_OPENINTENTS_ACTION_PICK_FILE = "org.openintents.action.PICK_FILE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import_keypair, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            A3Utils.findView(getActivity(), Button.class, R.id.pk_btn_browse_private).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    onBrowsePrivateClicked(view);
                }
            });
            A3Utils.findView(getActivity(), Button.class, R.id.pk_btn_browse_public).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    onBrowsePublicClicked(view);
                }
            });
        } catch (ViewNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void onBrowsePrivateClicked(View view) {
        Intent intent = new Intent(ORG_OPENINTENTS_ACTION_PICK_FILE);
        startActivityForResult(intent, BROWSE_PRIVATE);
    }

    private void onBrowsePublicClicked(View view) {
        Intent intent = new Intent(ORG_OPENINTENTS_ACTION_PICK_FILE);
        startActivityForResult(intent, BROWSE_PUBLIC);
    }

}
