package com.android.covidsafe.utilities;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
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

public interface CryptographyManager {

    /**
     * This method first gets or generates an instance of SecretKey and then initializes the Cipher
     * with the key. The secret key uses [ENCRYPT_MODE][Cipher.ENCRYPT_MODE] is used.
     */
    Cipher getInitializedCipherForEncryption(String keyName);

    /**
     * This method first gets or generates an instance of SecretKey and then initializes the Cipher
     * with the key. The secret key uses [DECRYPT_MODE][Cipher.DECRYPT_MODE] is used.
     */
    Cipher getInitializedCipherForDecryption(String keyName, byte[] initializationVector);

    /**
     * The Cipher created with [getInitializedCipherForEncryption] is used here
     */
    CiphertextWrapper encryptData(String plaintext, Cipher cipher);

    /**
     * The Cipher created with [getInitializedCipherForDecryption] is used here
     */
    String decryptData(byte[] ciphertext, Cipher cipher);

    void persistCiphertextWrapperToSharedPrefs(
            CiphertextWrapper ciphertextWrapper,
            Context context,
            String filename,
            int mode,
            String prefKey
    );

    CiphertextWrapper getCiphertextWrapperFromSharedPrefs(
            Context context,
            String filename,
            int mode,
            String prefKey
    );
}

