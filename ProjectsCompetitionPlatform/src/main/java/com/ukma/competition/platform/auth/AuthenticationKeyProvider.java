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
public class AuthenticationKeyProvider {

    final PrivateKey privateKey;
    final PublicKey publicKey;
    final KeyFactory keyFactory;
    final ResourceLoader resourceLoader;

    public AuthenticationKeyProvider(ResourceLoader resourceLoader) throws Exception {
        this.resourceLoader = resourceLoader;
        this.keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = loadPrivateKey();
        this.publicKey = loadPublicKey();
    }

    private PrivateKey loadPrivateKey() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/keys/private_key.pem");
        String key = new String(resource.getContentAsByteArray())
            .replaceAll("-----\\w+ PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        return keyFactory.generatePrivate(spec);
    }

    private PublicKey loadPublicKey() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:/keys/public_key.pem");
        String key = new String(resource.getContentAsByteArray())
            .replaceAll("-----\\w+ PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        return keyFactory.generatePublic(spec);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}