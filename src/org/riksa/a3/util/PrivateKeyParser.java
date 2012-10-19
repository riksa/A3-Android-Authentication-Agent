/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.util;

import java.security.PrivateKey;

/**
 * User: riksa
 * Date: 19.10.2012
 * Time: 23:10
 */
public interface PrivateKeyParser {
    public PrivateKey parse(byte[] bytes);
    Integer getBits(PrivateKey privateKey);
}
