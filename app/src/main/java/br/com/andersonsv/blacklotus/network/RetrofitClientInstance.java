package br.com.andersonsv.blacklotus.network;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import br.com.andersonsv.blacklotus.R;
import okhttp3.OkHttpClient;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static final String BASE_URL = "https://api.magicthegathering.io/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = trustManagerForCertificates(trustedCertificatesInputStream(), context);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { trustManager }, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static X509TrustManager trustManagerForCertificates(InputStream in, Context context)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "blacklotus".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password, context);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }
    private static KeyStore newEmptyKeyStore(char[] password, Context context) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null;
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.
        String comodoRsaCertificationAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n" +
                "MIIF2DCCA8CgAwIBAgIQTKr5yttjb+Af907YWwOGnTANBgkqhkiG9w0BAQwFADCB\n" +
                "hTELMAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4G\n" +
                "A1UEBxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxKzApBgNV\n" +
                "BAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTAwMTE5\n" +
                "MDAwMDAwWhcNMzgwMTE4MjM1OTU5WjCBhTELMAkGA1UEBhMCR0IxGzAZBgNVBAgT\n" +
                "EkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UEBxMHU2FsZm9yZDEaMBgGA1UEChMR\n" +
                "Q09NT0RPIENBIExpbWl0ZWQxKzApBgNVBAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNh\n" +
                "dGlvbiBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCR\n" +
                "6FSS0gpWsawNJN3Fz0RndJkrN6N9I3AAcbxT38T6KhKPS38QVr2fcHK3YX/JSw8X\n" +
                "pz3jsARh7v8Rl8f0hj4K+j5c+ZPmNHrZFGvnnLOFoIJ6dq9xkNfs/Q36nGz637CC\n" +
                "9BR++b7Epi9Pf5l/tfxnQ3K9DADWietrLNPtj5gcFKt+5eNu/Nio5JIk2kNrYrhV\n" +
                "/erBvGy2i/MOjZrkm2xpmfh4SDBF1a3hDTxFYPwyllEnvGfDyi62a+pGx8cgoLEf\n" +
                "Zd5ICLqkTqnyg0Y3hOvozIFIQ2dOciqbXL1MGyiKXCJ7tKuY2e7gUYPDCUZObT6Z\n" +
                "+pUX2nwzV0E8jVHtC7ZcryxjGt9XyD+86V3Em69FmeKjWiS0uqlWPc9vqv9JWL7w\n" +
                "qP/0uK3pN/u6uPQLOvnoQ0IeidiEyxPx2bvhiWC4jChWrBQdnArncevPDt09qZah\n" +
                "SL0896+1DSJMwBGB7FY79tOi4lu3sgQiUpWAk2nojkxl8ZEDLXB0AuqLZxUpaVIC\n" +
                "u9ffUGpVRr+goyhhf3DQw6KqLCGqR84onAZFdr+CGCe01a60y1Dma/RMhnEw6abf\n" +
                "Fobg2P9A3fvQQoh/ozM6LlweQRGBY84YcWsr7KaKtzFcOmpH4MN5WdYgGq/yapiq\n" +
                "crxXStJLnbsQ/LBMQeXtHT1eKJ2czL+zUdqnR+WEUwIDAQABo0IwQDAdBgNVHQ4E\n" +
                "FgQUu69+Aj36pvE8hI6t7jiY7NkyMtQwDgYDVR0PAQH/BAQDAgEGMA8GA1UdEwEB\n" +
                "/wQFMAMBAf8wDQYJKoZIhvcNAQEMBQADggIBAArx1UaEt65Ru2yyTUEUAJNMnMvl\n" +
                "wFTPoCWOAvn9sKIN9SCYPBMtrFaisNZ+EZLpLrqeLppysb0ZRGxhNaKatBYSaVqM\n" +
                "4dc+pBroLwP0rmEdEBsqpIt6xf4FpuHA1sj+nq6PK7o9mfjYcwlYRm6mnPTXJ9OV\n" +
                "2jeDchzTc+CiR5kDOF3VSXkAKRzH7JsgHAckaVd4sjn8OoSgtZx8jb8uk2Intzna\n" +
                "FxiuvTwJaP+EmzzV1gsD41eeFPfR60/IvYcjt7ZJQ3mFXLrrkguhxuhoqEwWsRqZ\n" +
                "CuhTLJK7oQkYdQxlqHvLI7cawiiFwxv/0Cti76R7CZGYZ4wUAc1oBmpjIXUDgIiK\n" +
                "boHGhfKppC3n9KUkEEeDys30jXlYsQab5xoq2Z0B15R97QNKyvDb6KkBPvVWmcke\n" +
                "jkk9u+UJueBPSZI9FoJAzMxZxuY67RIuaTxslbH9qh17f4a+Hg4yRvv7E491f0yL\n" +
                "S0Zj/gA0QHDBw7mh3aZw4gSzQbzpgJHqZJx64SIDqZxubw5lT2yHh17zbqD5daWb\n" +
                "QOhTsiedSrnAdyGN/4fy3ryM7xfft0kL0fJuMAsaDk527RH89elWsn2/x20Kk4yl\n" +
                "0MC2Hb46TpSi125sC8KKfPog88Tk5c0NqMuRkrF8hey1FGlmDoLnzc7ILaZRfyHB\n" +
                "NVOFBkpdn627G190\n" +
                "-----END CERTIFICATE-----\n";
        String entrustRootCertificateAuthority = ""
              + "-----BEGIN CERTIFICATE-----\n" +
                      "MIILJzCCCs2gAwIBAgIQbJFfI4mAstqNVPB6QX4h+TAKBggqhkjOPQQDAjCBkjEL\n" +
                      "MAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UE\n" +
                      "BxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxODA2BgNVBAMT\n" +
                      "L0NPTU9ETyBFQ0MgRG9tYWluIFZhbGlkYXRpb24gU2VjdXJlIFNlcnZlciBDQSAy\n" +
                      "MB4XDTE4MTIyMDAwMDAwMFoXDTE5MDYyODIzNTk1OVowazEhMB8GA1UECxMYRG9t\n" +
                      "YWluIENvbnRyb2wgVmFsaWRhdGVkMSEwHwYDVQQLExhQb3NpdGl2ZVNTTCBNdWx0\n" +
                      "aS1Eb21haW4xIzAhBgNVBAMTGnNuaTM4NjA3LmNsb3VkZmxhcmVzc2wuY29tMFkw\n" +
                      "EwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEertVKtM+rnNDsD9WnRXJ1GbDr6FEEajD\n" +
                      "FixWFcOVN924THAb+mn/WLz7G7TB3Tx+jWaz3nHKha7o5a9E9Wd3MaOCCSkwggkl\n" +
                      "MB8GA1UdIwQYMBaAFEAJYWfwvINxT94SCCxv1NQrdj2WMB0GA1UdDgQWBBQyAY4e\n" +
                      "mxAGgTwFGKnb0FrOMEQigDAOBgNVHQ8BAf8EBAMCB4AwDAYDVR0TAQH/BAIwADAd\n" +
                      "BgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwTwYDVR0gBEgwRjA6BgsrBgEE\n" +
                      "AbIxAQICBzArMCkGCCsGAQUFBwIBFh1odHRwczovL3NlY3VyZS5jb21vZG8uY29t\n" +
                      "L0NQUzAIBgZngQwBAgEwVgYDVR0fBE8wTTBLoEmgR4ZFaHR0cDovL2NybC5jb21v\n" +
                      "ZG9jYTQuY29tL0NPTU9ET0VDQ0RvbWFpblZhbGlkYXRpb25TZWN1cmVTZXJ2ZXJD\n" +
                      "QTIuY3JsMIGIBggrBgEFBQcBAQR8MHowUQYIKwYBBQUHMAKGRWh0dHA6Ly9jcnQu\n" +
                      "Y29tb2RvY2E0LmNvbS9DT01PRE9FQ0NEb21haW5WYWxpZGF0aW9uU2VjdXJlU2Vy\n" +
                      "dmVyQ0EyLmNydDAlBggrBgEFBQcwAYYZaHR0cDovL29jc3AuY29tb2RvY2E0LmNv\n" +
                      "bTCCBmcGA1UdEQSCBl4wggZaghpzbmkzODYwNy5jbG91ZGZsYXJlc3NsLmNvbYIV\n" +
                      "Ki5hZmF2b3JkZWxvbWVqb3Iub3Jngg0qLmFsYXNuZXQub3JnghUqLmFuZHJlc3Zl\n" +
                      "bGF6cXVlei5jb22CEiouYW5kcmV3YmFja2VzLmNvbYILKi5hbnRhZC5iaXqCDCou\n" +
                      "Y2FyLW5ldC5jYYISKi5jbGluaXNvbnJpc2EuY29tghYqLmNvbmVqaWxsb2RlaW5k\n" +
                      "aWFzLm14ghIqLmRhcmtwcm9zcGVjdHMudXOCECouZGVzYWZpb2IyMS5vcmeCDSou\n" +
                      "ZGlyZWN0by5jb22CDSouZG9uZGVkYXIubXiCECouZW5kb2dhbWluZy5uZXSCGCou\n" +
                      "Zmxlc2hlcnRvbm1pbm9yYmFsbC5jYYIfKi5nb2xwb3JtZXhpY290ZWxsZXZhYXJ1\n" +
                      "c2lhLmNvbYIWKi5pbXByZXNvc2xhY2FuYWRhLmNvbYILKi5rZW50bGwuY2GCDiou\n" +
                      "a29vbWNoYS5ibG9ngg0qLmtvb21jaGEuY29tghIqLmxlZ2Fsc3Rhci5jb20ubXiC\n" +
                      "FCoubG9uZG9uanJrbmlnaHRzLmNhghYqLm1hZ2ljdGhlZ2F0aGVyaW5nLmlvghIq\n" +
                      "Lm1hcHJpcXVpbS5jb20ubXiCEioubWluZHlsYW5kZXJzLmNvbYIKKi5ubXNhLm5l\n" +
                      "dIIWKi5ub3Jmb2xraGVyaWNhbmVzLmNvbYIeKi5ub3RpZmljYWNpb25lc211bHRp\n" +
                      "dmEuY29tLm14ggoqLm85OWcuY29tghwqLm9yYW5nZXZpbGxlbWlub3Job2NrZXku\n" +
                      "Y29tgg4qLnBiYm9hcmQuaW5mb4IUKi5waWNrZXJpbmdob2NrZXkuY2GCDyoucG9r\n" +
                      "ZW1vbnRjZy5pb4IYKi5wb3B1cHBob3RvYm9vdGguY29tLmF1ghUqLnBvcnRtaW5v\n" +
                      "cmhvY2tleS5jb22CESouc2FsemFjYXRlY2FzLm14ghcqLnNodXRpbmJyZWFraW5n\n" +
                      "b3V0LmNvbYITKi5zaW1jb2ViYXNlYmFsbC5jYYILKi50YWN0aWMubXiCDyoudGFj\n" +
                      "dGljdGVsLmNvbYISKi50ZW5nb2Zsb2plcmEuY29tghkqLnR1ZnVuZGFjaW9udGVs\n" +
                      "ZXZpc2Eub3JnggwqLndvYWEub24uY2GCE2FmYXZvcmRlbG9tZWpvci5vcmeCC2Fs\n" +
                      "YXNuZXQub3JnghNhbmRyZXN2ZWxhenF1ZXouY29tghBhbmRyZXdiYWNrZXMuY29t\n" +
                      "gglhbnRhZC5iaXqCCmNhci1uZXQuY2GCEGNsaW5pc29ucmlzYS5jb22CFGNvbmVq\n" +
                      "aWxsb2RlaW5kaWFzLm14ghBkYXJrcHJvc3BlY3RzLnVzgg5kZXNhZmlvYjIxLm9y\n" +
                      "Z4ILZGlyZWN0by5jb22CC2RvbmRlZGFyLm14gg5lbmRvZ2FtaW5nLm5ldIIWZmxl\n" +
                      "c2hlcnRvbm1pbm9yYmFsbC5jYYIdZ29scG9ybWV4aWNvdGVsbGV2YWFydXNpYS5j\n" +
                      "b22CFGltcHJlc29zbGFjYW5hZGEuY29tgglrZW50bGwuY2GCDGtvb21jaGEuYmxv\n" +
                      "Z4ILa29vbWNoYS5jb22CEGxlZ2Fsc3Rhci5jb20ubXiCEmxvbmRvbmpya25pZ2h0\n" +
                      "cy5jYYIUbWFnaWN0aGVnYXRoZXJpbmcuaW+CEG1hcHJpcXVpbS5jb20ubXiCEG1p\n" +
                      "bmR5bGFuZGVycy5jb22CCG5tc2EubmV0ghRub3Jmb2xraGVyaWNhbmVzLmNvbYIc\n" +
                      "bm90aWZpY2FjaW9uZXNtdWx0aXZhLmNvbS5teIIIbzk5Zy5jb22CGm9yYW5nZXZp\n" +
                      "bGxlbWlub3Job2NrZXkuY29tggxwYmJvYXJkLmluZm+CEnBpY2tlcmluZ2hvY2tl\n" +
                      "eS5jYYINcG9rZW1vbnRjZy5pb4IWcG9wdXBwaG90b2Jvb3RoLmNvbS5hdYITcG9y\n" +
                      "dG1pbm9yaG9ja2V5LmNvbYIPc2FsemFjYXRlY2FzLm14ghVzaHV0aW5icmVha2lu\n" +
                      "Z291dC5jb22CEXNpbWNvZWJhc2ViYWxsLmNhggl0YWN0aWMubXiCDXRhY3RpY3Rl\n" +
                      "bC5jb22CEHRlbmdvZmxvamVyYS5jb22CF3R1ZnVuZGFjaW9udGVsZXZpc2Eub3Jn\n" +
                      "ggp3b2FhLm9uLmNhMIIBBQYKKwYBBAHWeQIEAgSB9gSB8wDxAHYAu9nfvB+KcbWT\n" +
                      "lCOXqpJ7RzhXlQqrUugakJZkNo4e0YUAAAFny5sW+wAABAMARzBFAiEAzncHnY8x\n" +
                      "jSI03vCE4vQaN52iZl51Q2YCmiovYoYaat8CIA6PG61JaRTxX8Pt8SZLLsdxmT+m\n" +
                      "fAbGP7uOUQMr27I3AHcAdH7agzGtMxCRIZzOJU9CcMK//V5CIAjGNzV55hB7zFYA\n" +
                      "AAFny5sXcAAABAMASDBGAiEA46Re07SPlLkOkLdYHdm/ZR8kPkyfaFdRKflzRKdn\n" +
                      "SC4CIQCJrBcnI6vZ5BJmyk5NPaR1dw8z3qoHgGt4IpZkSA/55DAKBggqhkjOPQQD\n" +
                      "AgNIADBFAiAa9PglQseSJC4Uewi5WSHo+dGy81eUFbfkdXPDLh5CHAIhAPPZDDlH\n" +
                      "3TkMvE5qKla7QidMW+zD6L6vYunNJiuxqM7d\n" +
                      "-----END CERTIFICATE-----\n";
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .writeUtf8(entrustRootCertificateAuthority)
                .inputStream();
    }



}
