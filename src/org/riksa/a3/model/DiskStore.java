/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.model;

import android.content.Context;
import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

/**
 * User: riksa
 * Date: 21.10.2012
 * Time: 18:50
 */
public class DiskStore extends AbstractKeyStoreStore implements KeyStoreStore {
    private static final String JKS_TYPE = "BOUNCYCASTLE";
    private static final String KEYSTORE_FILE = "keystore.bks";
    private static final Logger log = LoggerFactory.getLogger(DiskStore.class);
    private KeyStore keystore;
    private PromptPasswordCallback promptLoadPassword;
    private PromptPasswordCallback promptSavePassword;
    private Context context;
    private File keystoreFile;

    static {
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    public DiskStore(Context context, PromptPasswordCallback promptLoadPassword, PromptPasswordCallback promptSavePassword) {
        super();
        this.promptLoadPassword = promptLoadPassword;
        this.promptSavePassword = promptSavePassword;
        this.context = context;
        keystoreFile = new File(context.getFilesDir(), KEYSTORE_FILE);
    }

    public void open() throws KeyStoreException {
        String password = promptLoadPassword.getPassword();
        final KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection( password.toCharArray() );

        if (keystoreFile.exists()) {
            log.debug("Keystore {} exists", keystoreFile.getAbsolutePath());
            KeyStore.Builder builder = KeyStore.Builder.newInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider(), keystoreFile, pp);
            keystore = builder.getKeyStore();
        } else {
            log.debug("Keystore {} does not exist", keystoreFile.getAbsolutePath());
            KeyStore.Builder builder = KeyStore.Builder.newInstance(JKS_TYPE, new org.spongycastle.jce.provider.BouncyCastleProvider(), pp);
            keystore = builder.getKeyStore();
        }
    }

    public void close() throws KeyStoreException {
        try {
            KeyStore.LoadStoreParameter pp = new KeyStore.LoadStoreParameter() {
                public KeyStore.ProtectionParameter getProtectionParameter() {
                    String password = promptSavePassword.getPassword();
                    final KeyStore.PasswordProtection saveStoreParameter = new KeyStore.PasswordProtection( password.toCharArray() );
                    return saveStoreParameter;
                }
            };
            keystore.store( pp );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        } catch (CertificateException e) {
            log.error(e.getMessage(), e);
            throw new KeyStoreException(e.getMessage());
        }
    }

}
