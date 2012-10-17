/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.util;

import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 17.10.2012
 * Time: 18:34
 */
public class LoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger("A3_" + clazz.getSimpleName());
    }
}
