package net.paulweider;

import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.PKIXParameters;

import static org.junit.jupiter.api.Assertions.*;

class MainTest
{

    @Test
    void getKeyStore_ifNoTrustCertFound()
    {
        KeyStore keyStore = Main.getKeyStore();

        Exception actual = assertThrows(InvalidAlgorithmParameterException.class, () -> new PKIXParameters(keyStore));
        assertEquals(actual.getMessage(), "the trustAnchors parameter must be non-empty");
    }
}