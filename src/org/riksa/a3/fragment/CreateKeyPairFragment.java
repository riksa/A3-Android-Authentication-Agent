/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.riksa.a3.R;
import org.riksa.a3.activity.CreateKeyPairActivity;
import org.riksa.a3.exception.InvalidInputException;
import org.riksa.a3.exception.ViewNotFoundException;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * User: riksa
 * Date: 17.10.2012
 * Time: 22:35
 */
public class CreateKeyPairFragment extends Fragment {
    private static final Logger log = LoggerFactory.getLogger(CreateKeyPairFragment.class);
    private static final String EXTRA_KEY_NAME = "extra_key_name";
    private static final String EXTRA_KEY_KEYPAIR = "extra_key_keypair";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_keypair, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            findView(Button.class, R.id.pk_generate_button).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    onGenerateClicked(view);
                }
            });
        } catch (ViewNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void onGenerateClicked(View view) {
        try {
            log.debug("Key name {}", getKeyName());
            log.debug("Key type {}", getKeyType());
            log.debug("Key bits {}", getKeyBits());

            // TODO: AsyncTask
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(getKeyType());
            keyPairGenerator.initialize(getKeyBits(), new SecureRandom()); // TODO: securerandom generation?
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            log.debug("Public key format {}", keyPair.getPublic().getFormat());
            log.debug("Private key format {}", keyPair.getPrivate().getFormat());

            Intent data = new Intent();
            data.putExtra(EXTRA_KEY_NAME, getKeyName());
            data.putExtra(EXTRA_KEY_KEYPAIR, keyPair);

            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish(); // this has to be changed if we are using single activity at some point
        } catch (InvalidInputException e) {
            log.warn("TODO: handle specific cases");
            Toast.makeText(getActivity(), R.string.pk_invalid_input, Toast.LENGTH_SHORT).show();
        } catch (ViewNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            Toast.makeText(getActivity(), R.string.pk_invalid_pk_alg, Toast.LENGTH_SHORT).show();
        }
    }

    private String getKeyName() throws InvalidInputException, ViewNotFoundException {
        log.debug("TODO: validate");
        EditText textView = findView(EditText.class, R.id.pk_name);
        return textView.getText().toString();
    }

    private String getKeyType() throws ViewNotFoundException {
        RadioButton rsaButton = findView(RadioButton.class, R.id.pk_type_rsa);
        if (rsaButton.isChecked()) {
            return "RSA";
        }
        return "DSA";
    }

    private int getKeyBits() throws ViewNotFoundException {
        Spinner spinner = findView(Spinner.class, R.id.pk_strength);
        String selectedString = (String) spinner.getSelectedItem();
        return Integer.parseInt(selectedString);
    }

    private <T extends View> T findView(Class<? extends View> clazz, int id) throws ViewNotFoundException {
        View view = getActivity().findViewById(id);
        if (view != null && view.getClass().isAssignableFrom(clazz)) {
            return (T) view;
        }
        log.error("Cannot find view of {} with id {}", clazz.toString(), id);
        throw new ViewNotFoundException();
    }

}
