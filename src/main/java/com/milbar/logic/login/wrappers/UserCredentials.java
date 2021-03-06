package com.milbar.logic.login.wrappers;

import com.milbar.logic.encryption.factories.RSACipherFactory;
import com.milbar.logic.encryption.factories.RSAFactory;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class UserCredentials implements Serializable {
    
    private String username;
    
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    public UserCredentials(String username) {
        this.username = username;
        RSACipherFactory cipherFactory = new RSACipherFactory(new RSAFactory());
        KeyPair keyPair = cipherFactory.getKeyPairDefault();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
    
    public String getUsername() {
        return username;
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
}
