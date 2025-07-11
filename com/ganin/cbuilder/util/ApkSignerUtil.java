package com.ganin.cbuilder.util;

import com.android.apksig.ApkSigner;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import kellinwood.security.zipsigner.optional.KeyStoreFileManager;
import org.apache.commons.io.FileUtils;

//
public class ApkSignerUtil {
    private final String cerPath;
    private final String filesDir;
    private final File inputApk;
    private final String keyPath;
    private final File outputApk;

    public ApkSignerUtil(String str, String str2, String str3, File file, File file2) {
        this.keyPath = str;
        this.cerPath = str2;
        this.filesDir = str3;
        this.inputApk = file;
        this.outputApk = file2;
    }

    public PrivateKey getKey() throws Exception {
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(FileUtils.readFileToByteArray(new File(this.filesDir + "/" + this.keyPath))));
    }

    public void sign(String str, String str2, char[] cArr) {
        PrivateKey key;
        ArrayList arrayList = new ArrayList();
        try {
            if (str2.equals("testkey")) {
                key = getKey();
                arrayList.add((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(this.filesDir + "/" + this.cerPath)));
            } else if (new File(str).exists()) {
                KeyStore loadKeyStore = KeyStoreFileManager.loadKeyStore(str, cArr);
                Certificate certificate = loadKeyStore.getCertificate(str2);
                Key key2 = loadKeyStore.getKey(str2, cArr);
                arrayList.add((X509Certificate) certificate);
                key = (PrivateKey) key2;
            } else {
                key = getKey();
                arrayList.add((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(this.filesDir + "/" + this.cerPath)));
            }
            ApkSigner.SignerConfig signerConfig = new ApkSigner.SignerConfig("CERT", key, arrayList, true);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(signerConfig);
            ApkSigner.Builder builder = new ApkSigner.Builder(arrayList2);
            builder.setInputApk(this.inputApk);
            builder.setMinSdkVersion(21);
            builder.setV4SigningEnabled(false);
            builder.setCreatedBy("CCode");
            builder.setOutputApk(this.outputApk);
            builder.build().sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getKeyPath() {
        return this.keyPath;
    }

    public String getCerPath() {
        return this.cerPath;
    }
}
