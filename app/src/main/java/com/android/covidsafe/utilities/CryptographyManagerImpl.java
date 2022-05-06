package com.android.covidsafe.utilities;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CryptographyManagerImpl implements CryptographyManager {

    private final Integer KEY_SIZE = 256;
    private final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private final String ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM;
    private final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE;
    private final String ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;

    @Override
    public Cipher getInitializedCipherForEncryption(String keyName) {
        Cipher cipher = getCipher();
        SecretKey secretKey = getOrCreateSecretKey(keyName);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    @Override
    public Cipher getInitializedCipherForDecryption(String keyName, byte[] initializationVector) {
        Cipher cipher = getCipher();
        SecretKey secretKey = getOrCreateSecretKey(keyName);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, initializationVector));
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    @Override
    public CiphertextWrapper encryptData(String plaintext, Cipher cipher) {
        byte[] ciphertext = null;
        try {
            ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new CiphertextWrapper(ciphertext, cipher.getIV());
    }

    @Override
    public String decryptData(byte[] ciphertext, Cipher cipher) {
        byte[] plaintext = new byte[0];
        try {
            plaintext = cipher.doFinal(ciphertext);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new String(plaintext, StandardCharsets.UTF_8);
    }

    @Override
    public void persistCiphertextWrapperToSharedPrefs(
            CiphertextWrapper ciphertextWrapper,
            Context context,
            String filename,
            int mode,
            String prefKey
    ) {
        String json = new Gson().toJson(ciphertextWrapper);
        context.getSharedPreferences(filename, mode).edit().putString(prefKey, json).apply();
    }

    @Override
    public CiphertextWrapper getCiphertextWrapperFromSharedPrefs(
            Context context,
            String filename,
            int mode,
            String prefKey
    ) {
        String json = context.getSharedPreferences(filename, mode).getString(prefKey, null);
        return new Gson().fromJson(json, CiphertextWrapper.class);
    }

    private Cipher getCipher() {
        Cipher cipher = null;
        String transformation = this.ENCRYPTION_ALGORITHM + '/' + this.ENCRYPTION_BLOCK_MODE + '/' + this.ENCRYPTION_PADDING;
        try {
            cipher = Cipher.getInstance(transformation);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    private SecretKey getOrCreateSecretKey(String keyName) {
        try {
            // If Secretkey was previously created for that keyName, then grab and return it.
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null); // Keystore must be loaded before it can be accessed
            Key key = keyStore.getKey(keyName, null);
            if (key != null) {
                return (SecretKey) key;
            } else {
                // if you reach here, then a new SecretKey must be generated for that keyName
                KeyGenParameterSpec.Builder paramsBuilder = new KeyGenParameterSpec.Builder(keyName,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
                paramsBuilder.setBlockModes(ENCRYPTION_BLOCK_MODE);
                paramsBuilder.setEncryptionPaddings(ENCRYPTION_PADDING);
                paramsBuilder.setKeySize(KEY_SIZE);
                paramsBuilder.setUserAuthenticationRequired(true);

                KeyGenParameterSpec keyGenParams = paramsBuilder.build();
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                        ANDROID_KEYSTORE);
                keyGenerator.init(keyGenParams);

                return keyGenerator.generateKey();
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
