package com.oblador.keychain.cipherStorage;

import android.app.Activity;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.oblador.keychain.exceptions.CryptoFailedException;
import com.oblador.keychain.exceptions.KeyStoreAccessException;

public interface CipherStorage {
    abstract class CipherResult<T> {
        public final T username;
        public final T password;

        public CipherResult(T username, T password) {
            this.username = username;
            this.password = password;
        }
    }

    class EncryptionResult extends CipherResult<byte[]> {
        public CipherStorage cipherStorage;

        public EncryptionResult(byte[] username, byte[] password, CipherStorage cipherStorage) {
            super(username, password);
            this.cipherStorage = cipherStorage;
        }
    }

    class DecryptionResult extends CipherResult<String> {
        public DecryptionResult(String username, String password) {
            super(username, password);
        }
    }

    interface DecryptionResultHandler {
        public void onDecrypt(DecryptionResult decryptionResult, String error);
    }

    class AuthenticationPrompt {
        public final @NonNull String title;
        public final @Nullable String subTitle;
        public final @Nullable String description;
        public final @NonNull String cancel;

        public AuthenticationPrompt(@NonNull String title, @Nullable String subTitle, @Nullable String description, @NonNull String cancel) {
            this.title = title;
            this.subTitle = subTitle;
            this.description = description;
            this.cancel = cancel;
        }
    }

    EncryptionResult encrypt(@NonNull String service, @NonNull String username, @NonNull String password) throws CryptoFailedException;

    void decrypt(@NonNull DecryptionResultHandler decryptionResultHandler, @NonNull String service, @NonNull byte[] username, @NonNull byte[] password) throws CryptoFailedException, KeyPermanentlyInvalidatedException;

    void removeKey(@NonNull String service) throws KeyStoreAccessException;

    String getCipherStorageName();

    boolean getCipherBiometrySupported();

    int getMinSupportedApiLevel();

    boolean getRequiresCurrentActivity();
    void setCurrentActivity(Activity activity);

    void setAuthenticationPrompt(AuthenticationPrompt authenticationPrompt);
}
