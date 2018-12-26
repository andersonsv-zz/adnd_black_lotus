package br.com.andersonsv.blacklotus.application;

import android.app.Application;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import br.com.andersonsv.blacklotus.R;

public class BlackLotusApplication extends Application {
    private static final String KEY_STORE_TYPE = "BKS";
    private static final String SSL_PROTOCOL = "TLS";

    @Override
    public void onCreate() {
        super.onCreate();

        InputStream stream;
        char [] password;
        try {
            stream = getApplicationContext().getResources().openRawResource(R.raw.blacklotus);
            password = ("blacklotus").toCharArray();
            KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
            ks.load(stream, password);
            stream.close();

            SSLContext sc = SSLContext.getInstance(SSL_PROTOCOL);
            String strKmf = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(strKmf);

            String strTmf = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(strTmf);

            kmf.init(ks, password);
            tmf.init(ks);

            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLSocketFactory sf = sc.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
