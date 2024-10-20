package com.ukma.competition.platform.auth;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyProvider {

    final PrivateKey PRIVATE_KEY;
    final PublicKey PUBLIC_KEY;
    final KeyFactory KEY_FACTORY;
    ResourceLoader resourceLoader;

    public KeyProvider(ResourceLoader resourceLoader) throws Exception {
        this.resourceLoader = resourceLoader;
        this.KEY_FACTORY = KeyFactory.getInstance("RSA");
        this.PRIVATE_KEY = loadPrivateKey();
        this.PUBLIC_KEY = loadPublicKey();
    }

    private PrivateKey loadPrivateKey() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/keys/private_key.pem");
        String key = new String(resource.getContentAsByteArray())
            .replaceAll("-----\\w+ PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        return KEY_FACTORY.generatePrivate(spec);
    }

    private PublicKey loadPublicKey() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/keys/public_key.pem");
        String key = new String(resource.getContentAsByteArray())
            .replaceAll("-----\\w+ PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        return KEY_FACTORY.generatePublic(spec);
    }

    public PrivateKey getPrivateKey() {
        return PRIVATE_KEY;
    }

    public PublicKey getPublicKey() {
        return PUBLIC_KEY;
    }
}