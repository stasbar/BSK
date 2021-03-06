package com.milbar.logic.encryption.wrappers.data;


import com.milbar.logic.encryption.factories.CipherFactory;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public abstract class EncryptedObject<E extends Serializable> implements Serializable {
    
    
    private byte[] serializedObject;
    CipherFactory cipherFactory;
    private boolean isEncrypted;
    
    public EncryptedObject(byte[] serializedObject, CipherFactory cipherFactory, boolean isEncrypted) {
        this.serializedObject = serializedObject;
        this.cipherFactory = cipherFactory;
        this.isEncrypted = isEncrypted;
    }
    
    public E getOriginalObject(byte[] data) {
        return (E)SerializationUtils.deserialize(data);
    }
    
    public byte[] getSerializedObject() {
        return serializedObject;
    }
    
    public boolean isEncrypted() {
        return isEncrypted;
    }
    
    public abstract CipherFactory getCipherFactory();
    
    
}
