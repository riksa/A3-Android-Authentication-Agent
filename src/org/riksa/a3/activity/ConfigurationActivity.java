/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.riksa.a3.R;

public class ConfigurationActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
    }
}
