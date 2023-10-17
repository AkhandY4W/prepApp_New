package com.youth4work.prepapp.PayTM;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PaytmSSLSocketFactory extends SSLSocketFactory {
    private volatile SSLContext mSSLContext;
    private static final String PKCS12 = "pkcs12";
    private static final String X509 = "X509";
    private static final String TLS = "TLS";
    private static final String RAW = "raw";

    protected PaytmSSLSocketFactory(Context inCtxt, PaytmClientCertificate inCertificate) {
        X509TrustManager TM = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] inChain, String inAuthType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] inChain, String inAuthType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        try {
            if(inCertificate != null && inCertificate.mFileName != null) {
                paytm.debugLog("Reading the certificate " + inCertificate.mFileName + ".p12");
                KeyStore inEx = KeyStore.getInstance("pkcs12");
                inEx.load(inCtxt.getResources().openRawResource(inCtxt.getResources().getIdentifier(inCertificate.mFileName, "raw", inCtxt.getPackageName())), inCertificate.mPassword.toCharArray());
                Enumeration inEx1 = inEx.aliases();

                while(inEx1.hasMoreElements()) {
                    paytm.debugLog(inEx.getCertificate(((String)inEx1.nextElement()).toString()).toString());
                }

                KeyManagerFactory KMF = KeyManagerFactory.getInstance("X509");
                KMF.init(inEx, inCertificate.mPassword.toCharArray());
                this.mSSLContext = SSLContext.getInstance("TLS");
                this.mSSLContext.init(KMF.getKeyManagers(), new TrustManager[]{TM}, (SecureRandom)null);
                paytm.debugLog("Client certificate attached.");
            } else {
                paytm.debugLog("Client certificate is not found");
                paytm.debugLog("so, setting only the trust manager");
                this.mSSLContext = SSLContext.getInstance("TLS");
                this.mSSLContext.init((KeyManager[])null, new TrustManager[]{TM}, (SecureRandom)null);
                paytm.debugLog("set trust manager");
            }
        } catch (Exception var8) {
            paytm.debugLog("Exception while attaching Client certificate.");
            paytm.printStackTrace(var8);

            try {
                paytm.debugLog("so, setting only the trust manager");
                this.mSSLContext = SSLContext.getInstance("TLS");
                this.mSSLContext.init((KeyManager[])null, new TrustManager[]{TM}, (SecureRandom)null);
                paytm.debugLog("set trust manager");
            } catch (Exception var7) {
                paytm.debugLog("Exception while setting the trust manager");
                paytm.printStackTrace(var7);
            }
        }

    }

    public synchronized Socket createSocket(Socket inS, String inHost, int iniPort, boolean inbAutoClose) throws IOException {
        return this.mSSLContext != null?this.mSSLContext.getSocketFactory().createSocket(inS, inHost, iniPort, inbAutoClose):getDefault().createSocket(inHost, iniPort);
    }

    public synchronized String[] getDefaultCipherSuites() {
        return null;
    }

    public synchronized String[] getSupportedCipherSuites() {
        return null;
    }

    public synchronized Socket createSocket(String inHost, int iniPort) throws IOException, UnknownHostException {
        return this.mSSLContext != null?this.mSSLContext.getSocketFactory().createSocket(inHost, iniPort):getDefault().createSocket(inHost, iniPort);
    }

    public synchronized Socket createSocket(InetAddress inHost, int iniPort) throws IOException {
        return this.mSSLContext != null?this.mSSLContext.getSocketFactory().createSocket(inHost, iniPort):getDefault().createSocket(inHost, iniPort);
    }

    public synchronized Socket createSocket(String inHost, int iniPort, InetAddress inLocalHost, int iniLocalPort) throws IOException, UnknownHostException {
        return this.mSSLContext != null?this.mSSLContext.getSocketFactory().createSocket(inHost, iniPort, inLocalHost, iniLocalPort):getDefault().createSocket(inHost, iniPort);
    }

    public synchronized Socket createSocket(InetAddress inAddress, int iniPort, InetAddress inLocalAddress, int iniLocalPort) throws IOException {
        return this.mSSLContext != null?this.mSSLContext.getSocketFactory().createSocket(inAddress, iniPort, inLocalAddress, iniLocalPort):getDefault().createSocket(inAddress, iniPort, inLocalAddress, iniLocalPort);
    }
}

