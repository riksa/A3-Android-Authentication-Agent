/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.riksa.a3.R;

/**
 * User: riksa
 * Date: 17.10.2012
 * Time: 22:29
 */
public class CreateKeyPairActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_keypair);
    }
}