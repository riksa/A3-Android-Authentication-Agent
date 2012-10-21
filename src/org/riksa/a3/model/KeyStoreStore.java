/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.model;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * User: riksa
 * Date: 21.10.2012
 * Time: 18:50
 * interface for storing/retrieving keys
 */
public interface KeyStoreStore {
    /**
     * Opens a keystore
     */
    void open() throws KeyStoreException;

    /**
     * Closes the keystore
     */
    void close() throws KeyStoreException;

    /**
     * Returns a key for specified keyId
     * @param id
     * @return
     */
    A3Key getKeyWithId(String id);

    /**
     * Add a new key to store
     * @param key
     * @return true if key was added (false if it already existed)
     */
    boolean addKey(A3Key key);

    /**
     * Remove given key from store
     * @param key
     * @return true if key was removed (false if key was not in the store)
     */
    boolean removeKey(A3Key key);

    /**
     * Get an unmodifiable list of keys
     * @return
     */
    List<A3Key> getUnmodifiableKeys();
}
