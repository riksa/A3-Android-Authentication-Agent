/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.model;

import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: riksa
 * Date: 21.10.2012
 * Time: 19:03
 */
public abstract class AbstractKeyStoreStore implements KeyStoreStore {
    private static final Logger log = LoggerFactory.getLogger(AbstractKeyStoreStore.class);
    protected List<A3Key> keys;

    protected AbstractKeyStoreStore() {
        keys = new ArrayList<A3Key>();
    }

    public A3Key getKeyWithId(String id) {
        if (id != null) {
            for (A3Key key : keys) {
                if (id.equals(key.getKeyId())) {
                    return key;
                }
            }
        }
        return null;
    }

    public boolean addKey(A3Key key) {
        synchronized (keys) {
            if (!keys.contains(key)) {
                keys.add(key);
                return true;
            }
        }
        return false;
    }

    public boolean removeKey(A3Key key) {
        synchronized (keys) {
            if (keys.contains(key)) {
                keys.remove(key);
                return true;
            }
        }
        return false;
    }

    public List<A3Key> getUnmodifiableKeys() {
        return Collections.unmodifiableList(keys);

    }

}
