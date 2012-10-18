/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.model;

import org.riksa.a3.util.LoggerFactory;
import org.slf4j.Logger;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.UUID;

/**
 * User: riksa
 * Date: 10/18/12
 * Time: 2:55 PM
 */
public class A3Key {
    private static final Logger log = LoggerFactory.getLogger(A3Key.class);
    private String keyId;
    public enum Type {RSA, DSA, UNKNOWN};
    private String keyName;
    private KeyPair keyPair;

    public A3Key(String keyName, KeyPair keyPair) {
        this.keyId = UUID.randomUUID().toString();
        this.keyName = keyName;
        this.keyPair = keyPair;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getKeyName() {
        return keyName;
    }

    public Type getKeyType() {
        if( keyPair == null ) {
            return Type.UNKNOWN;
        }
        return Type.RSA;
    }

    public int getKeyStrength() {
        if( keyPair == null ) {
            return -1;
        }
        return 666;
    }
}
