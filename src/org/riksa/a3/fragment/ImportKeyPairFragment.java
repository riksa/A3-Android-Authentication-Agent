/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

            EditText privateKeyEditText = A3Utils.findView(getActivity(), EditText.class, R.id.pk_private_key_path);
            privateKeyEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    validateKeys();
                }

                public void afterTextChanged(Editable editable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

            EditText publicKeyEditText = A3Utils.findView(getActivity(), EditText.class, R.id.pk_public_key_path);
            publicKeyEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    validateKeys();
                }

                public void afterTextChanged(Editable editable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

        } catch (ViewNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    void validateKeys() {
        try {
            Button importButton = A3Utils.findView(getActivity(), Button.class, R.id.pk_btn_import);
            importButton.setEnabled(false);
            EditText privateEdit = A3Utils.findView(getActivity(), EditText.class, R.id.pk_private_key_path);
            EditText publicEdit = A3Utils.findView(getActivity(), EditText.class, R.id.pk_public_key_path);
            if (validatePrivateKey(privateEdit.getText().toString()) && validatePublicKey(publicEdit.getText().toString())) {
                importButton.setEnabled(true);
            }
        } catch (ViewNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean validatePrivateKey(String privateKey) throws ViewNotFoundException {
        ImageView imageView = A3Utils.findView(getActivity(), ImageView.class, R.id.pk_icon_private_key_valid);
        TextView textView = A3Utils.findView(getActivity(), TextView.class, R.id.pk_text_private_key_valid);
        textView.setText(R.string.pk_text_private_key_invalid);
        imageView.setImageResource(R.drawable.btn_check_buttonless_off);
        if (privateKey == null || privateKey.length() == 0) {
            return false;
        }

        log.debug("privateKey={}", privateKey);

        String type = "RSA";
        int bits = 1024;
        textView.setText(getString(R.string.pk_text_private_key_valid, type, bits));
        imageView.setImageResource(R.drawable.btn_check_buttonless_on);
        return true;
    }

    private boolean validatePublicKey(String publicKey) throws ViewNotFoundException {
        ImageView imageView = A3Utils.findView(getActivity(), ImageView.class, R.id.pk_icon_public_key_valid);
        TextView textView = A3Utils.findView(getActivity(), TextView.class, R.id.pk_text_public_key_valid);
        textView.setText(R.string.pk_text_public_key_invalid);
        imageView.setImageResource(R.drawable.btn_check_buttonless_off);
        if (publicKey == null || publicKey.length() == 0) {
            return false;
        }

        log.debug("publicKey={}", publicKey);

        String type = "RSA";
        int bits = 1024;
        textView.setText(getString(R.string.pk_text_public_key_valid, type, bits));
        imageView.setImageResource(R.drawable.btn_check_buttonless_on);
        return true;
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
