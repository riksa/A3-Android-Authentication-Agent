/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.util;

import android.app.Activity;
import android.view.View;
import org.riksa.a3.exception.ViewNotFoundException;
import org.slf4j.Logger;

/**
 * User: riksa
 * Date: 19.10.2012
 * Time: 20:48
 */
public class A3Utils {
    private static final Logger log = LoggerFactory.getLogger(A3Utils.class);

    public static <T extends View> T findView(Activity activity, Class<? extends View> clazz, int id) throws ViewNotFoundException {
        View view = activity.findViewById(id);
        if (view != null && view.getClass().isAssignableFrom(clazz)) {
            return (T) view;
        }
        log.error("Cannot find view of {} with id {}", clazz.toString(), id);
        throw new ViewNotFoundException();
    }
}
