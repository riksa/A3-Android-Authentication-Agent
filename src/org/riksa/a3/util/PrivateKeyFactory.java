/*
 * This file is part of A3 - Android Authentication Agent
 * Copyright (c) 2012. riku salkia <riksa@iki.fi>
 * TODO: License ;)
 */

package org.riksa.a3.util;

import android.util.Base64;
import org.slf4j.Logger;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * User: riksa
 * Date: 19.10.2012
 * Time: 23:04
 */
public class PrivateKeyFactory {
    private static final Logger log = LoggerFactory.getLogger(PrivateKeyFactory.class);
    private static final Collection<PrivateKeyParser> parsers;

    static {
        parsers = new HashSet<PrivateKeyParser>();
        // RAS
        registerParser(new PrivateKeyParser() {

            // OpenSSH file
            public PrivateKey parse(byte[] bytes) {
                log.info("Trying to parse RSA/OpenSSH");
                byte[] spec = stripShit(bytes);
                log.info("Trying to parse DSA/OpenSSH");
                PrivateKey pk = parse("DSA", spec);
                if (pk != null) {
                    return pk;
                }
                log.info("Trying to parse RSA/OpenSSH");
                pk = parse("RSA", spec);
                if (pk != null) {
                    return pk;
                }
                return null;
            }

            private PrivateKey parse(String algo, byte[] spec) {
                try {
                    KeyFactory kf = KeyFactory.getInstance(algo);
                    KeySpec keySpec = new PKCS8EncodedKeySpec(spec);
                    PrivateKey pk = kf.generatePrivate(keySpec);
                    return pk;
                } catch (NoSuchAlgorithmException e) {
                    return null;
                } catch (InvalidKeySpecException e) {
                    return null;
                }
            }

            private byte[] stripShit(byte[] bytes) {
                String spec = new String(bytes);
                spec = spec.replaceAll("-----[^-]*-----", "");
                log.debug("Spec={}", spec);
                return Base64.decode(spec, 0);
            }

            public Integer getBits(PrivateKey privateKey) {
                if (privateKey instanceof RSAPrivateKey) {
                    RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
                    log.debug("Algo={}, format={}", privateKey.getAlgorithm(), privateKey.getFormat());
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public static PrivateKey createPrivateKey(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        for (PrivateKeyParser parser : parsers) {
            PrivateKey key = parser.parse(bytes);
            if (key != null) {
                return key;
            }
        }

        return null;
    }

    public static void registerParser(PrivateKeyParser parser) {
        parsers.add(parser);
    }

    public static int getBits(PrivateKey privateKey) {
        if (privateKey != null) {
            for (PrivateKeyParser parser : parsers) {
                Integer bits = parser.getBits(privateKey);
                if (bits != null) {
                    return bits;
                }
            }
        }
        return 0;
    }
}
