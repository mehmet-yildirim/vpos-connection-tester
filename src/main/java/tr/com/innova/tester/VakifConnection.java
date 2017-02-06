package tr.com.innova.tester;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mehmet on 2.02.2017.
 */
@Slf4j
public class VakifConnection {

    private final String url;

    public VakifConnection(String url) {
        this.url = url;
    }

    public String execute(String data) throws Exception {
        StringBuilder responseXMLBuilder = null;
        OutputStreamWriter out = null;
        BufferedReader in = null;

        try {
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URL connURL = new URL(this.url);

            log.info("Specified URL: {}", connURL);


            URLConnection conn = connURL.openConnection();

            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            out = new OutputStreamWriter(
                    conn.getOutputStream());

            out.write("prmstr=");
            out.write(data);
            out.flush();
            out.close();

            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            responseXMLBuilder = new StringBuilder();

            String line;

            while ((line = in.readLine()) != null)
                responseXMLBuilder.append(line);
            responseXMLBuilder.append("\n");
            in.close();

            return responseXMLBuilder.toString();
        } finally {
            if ( in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("exception occured when closing stream.");
                }
            }

            if ( out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("exception occured when closing stream.");
                }
            }
        }
    }

    private void trustAllHttpsCertificates() throws Exception {
        // Create a trust manager that does not validate certificate chains
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
}
