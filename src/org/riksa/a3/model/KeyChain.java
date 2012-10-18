/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.model;

import android.os.AsyncTask;
import android.widget.Toast;
import org.riksa.a3.R;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.lang.ref.WeakReference;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: riksa
 * Date: 18.10.2012
 * Time: 18:33
 */
public class KeyChain {
    private static final Logger log = LoggerFactory.getLogger(KeyChain.class);
    private List<A3Key> keys;
    private static KeyChain instance;
    private Collection<KeyChainListener> listeners = new HashSet<KeyChainListener>();
    private ExecutorService executorService;

    private KeyChain() {
        keys = new ArrayList<A3Key>();
    }

    public static KeyChain getInstance() {
        if (instance == null) {
            instance = new KeyChain();
        }
        return instance;
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    private class KeyDefinition {
        String keyName;
        String keyType;
        int keyBits;

        private KeyDefinition(String keyName, String keyType, int keyBits) {
            this.keyName = keyName;
            this.keyType = keyType;
            this.keyBits = keyBits;
        }
    }

    private class KeyGeneratorTask extends AsyncTask<KeyDefinition, Void, Void> {

        @Override
        protected Void doInBackground(KeyDefinition... keyDefinitions) {
            for (KeyDefinition keyDefinition : keyDefinitions) {
                A3Key key = new A3Key(keyDefinition.keyName, null);
                addKey(key);
                try {

                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyDefinition.keyType);
                    keyPairGenerator.initialize(keyDefinition.keyBits, new SecureRandom());
                    KeyPair keyPair = keyPairGenerator.genKeyPair();
                    key.setKeyPair(keyPair);
                    log.debug("Public key format {}", keyPair.getPublic().getFormat());
                    log.debug("Private key format {}", keyPair.getPrivate().getFormat());

                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getMessage(), e);
                    removeKey(key);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            notifyKeysChanged();
        }
    }

    public void generateKeyAsync(final String keyName, final String keyType, final int keyBits) {
        ExecutorService executorService = getExecutorService();
        final A3Key key = new A3Key(keyName, null);
        addKey(key);

        executorService.execute(new Runnable() {
            public void run() {
                log.debug("Generating key {}", keyName);
                try {
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);
                    keyPairGenerator.initialize(keyBits, new SecureRandom());
                    KeyPair keyPair = keyPairGenerator.genKeyPair();
                    key.setKeyPair(keyPair);
                    log.debug("Public key format {}", keyPair.getPublic().getFormat());
                    log.debug("Private key format {}", keyPair.getPrivate().getFormat());
                    notifyKeysChanged();
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getMessage(), e);
                    removeKey(key);
                }
                log.debug("Generation of key {} finished", keyName);
            }
        });

    }

    private void addKey(A3Key key) {
        synchronized (keys) {
            if (!keys.contains(key)) {
                keys.add(key);
                notifyKeysChanged();
            }
        }
    }

    private void removeKey(A3Key key) {
        synchronized (keys) {
            if (keys.contains(key)) {
                keys.remove(key);
                notifyKeysChanged();
            }
        }
    }

    public void addListener(KeyChainListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    public void removeListener(KeyChainListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void notifyKeysChanged() {
        log.debug("notifyKeysChanged");
        for (KeyChainListener keyChainListener : listeners) {
            keyChainListener.keyChainChanged();
        }

    }

    public List<A3Key> getUnmodifiableKeys() {
        return Collections.unmodifiableList(keys);
    }

    public interface KeyChainListener {
        void keyChainChanged();
    }

}
