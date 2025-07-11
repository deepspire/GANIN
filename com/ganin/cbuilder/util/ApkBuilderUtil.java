package com.ganin.cbuilder.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import com.android.tools.build.bundletool.commands.BuildBundleCommand;
import com.android.tools.build.bundletool.flags.FlagParser;
import com.bigzhao.xml2axml.Encoder;
import com.naef.jnlua.LuaState;
import java.io.File;
import java.io.IOException;
import java.security.Security;
import kellinwood.security.zipsigner.ZipSigner;
import kellinwood.security.zipsigner.optional.CustomKeySigner;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import org.apache.commons.io.FileUtils;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import ru.maximoff.zipalign.ZipAligner;

//
public class ApkBuilderUtil {
    private final String aapt2Path;
    private final String aaptLibPath;
    private final String aaptPath;
    private final String appDataDir;
    private final Context context;
    private final String filesDir;
    private final String script2Path;
    private final String script3Path;
    private final String scriptPath;
    private final String zipalignPath;

    public ApkBuilderUtil(Context context, String str, String str2) {
        this.context = context;
        this.filesDir = str;
        this.appDataDir = str2;
        this.scriptPath = context.getApplicationInfo().nativeLibraryDir + "/libbuild.so";
        this.script2Path = context.getApplicationInfo().nativeLibraryDir + "/libbuild2.so";
        this.script3Path = context.getApplicationInfo().nativeLibraryDir + "/libbuild3.so";
        this.aaptLibPath = context.getApplicationInfo().nativeLibraryDir + "/libaapt.so";
        this.aapt2Path = context.getApplicationInfo().nativeLibraryDir + "/libaapt2.so";
        this.aaptPath = context.getApplicationInfo().nativeLibraryDir + "/libaaptexec.so";
        this.zipalignPath = context.getApplicationInfo().nativeLibraryDir + "/libzipalign.so";
    }

    public void apkModify(FileUtil fileUtil, ApkSignerUtil apkSignerUtil, LuaState luaState, String str, boolean z, String str2, String str3, String str4, String str5) {
        String str6;
        String str7;
        String str8;
        String str9;
        int i;
        AndrolibException androlibException;
        LuaState luaState2;
        fileUtil.copyFile(apkSignerUtil.getCerPath(), null);
        fileUtil.copyFile(apkSignerUtil.getKeyPath(), null);
        fileUtil.copyFile("CGame", "CGame.apk");
        if (str4.equals("testkey")) {
            try {
                new File(this.appDataDir + "/cbuilder.jks").delete();
                fileUtil.copyFile("testkey.jks", "cbuilder.jks");
                fileUtil.copyFileWithPath(this.filesDir + "/cbuilder.jks", this.appDataDir + "/cbuilder.jks");
                new File(this.filesDir + "/cbuilder.jks").delete();
                str6 = "\ntestkey ok";
            } catch (IOException e) {
                e.printStackTrace();
                str6 = "\ntestkey false";
            }
        } else {
            str6 = "";
        }
        String str10 = "pcall(function() EXPORT.export({path = MY_PATH .. '/Sign.apk', name = '" + str + ".apk', listener = function(event) pcall(function() OS_REMOVE(DOC_DIR .. '/game.cc') OS_REMOVE(MY_PATH, true) require('Core.Share.build').reset() end) pcall(function() ADS.showAd() end) end}) end) pcall(function() WINDOW.remove() end)";
        String str11 = "pcall(function() EXPORT.export({path = MY_PATH .. '/Sign.aab', name = '" + str + ".aab', listener = function(event) pcall(function() OS_REMOVE(DOC_DIR .. '/game.cc') OS_REMOVE(MY_PATH, true) require('Core.Share.build').reset() end) pcall(function() ADS.showAd() end) end}) end) pcall(function() WINDOW.remove() end)";
        String encode = encode(this.filesDir + "/AndroidManifest1.xml", this.filesDir + "/AndroidManifest.xml", str6);
        if (z) {
            String str12 = this.filesDir + "/res.zip";
            String str13 = this.filesDir + "/resources";
            fileUtil.copyAsset("res", str12);
            try {
                new ZipFile(str12).extractAll(str13);
                new File(str12).delete();
            } catch (ZipException e2) {
                e2.printStackTrace();
            }
        }
        try {
            String str14 = encode + "\nprocess builder start";
            new ProcessBuilder(this.scriptPath, this.aaptPath, this.filesDir, this.aaptLibPath, this.zipalignPath).start().waitFor();
            if (fileExists(this.filesDir + "/CGame.apk")) {
                str7 = str14 + "\nprocess builder okay";
            } else {
                str7 = str14 + "\nprocess builder false";
            }
            String str15 = str7 + "\nprocess zipalign start";
            ZipAligner.align(this.filesDir + "/CGame.apk", this.filesDir + "/Align.apk", 4, true);
            if (fileExists(this.filesDir + "/Align.apk")) {
                str8 = str15 + "\nprocess zipalign okay";
            } else {
                str8 = str15 + "\nprocess zipalign false";
            }
            String str16 = str8 + "\nprocess signer start";
            apkSignerUtil.sign(this.appDataDir + "/cbuilder.jks", str4, str5.toCharArray());
            if (fileExists(this.filesDir + "/Sign.apk")) {
                str9 = str16 + "\nprocess signer okay";
            } else {
                str9 = str16 + "\nprocess signer false";
            }
            ((ClipboardManager) this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", str9));
            if (z) {
                try {
                    ApkDecoder apkDecoder = new ApkDecoder();
                    apkDecoder.setForceDelete(true);
                    apkDecoder.setDecodeSources((short) 0);
                    apkDecoder.setDecodeResources((short) 256);
                    apkDecoder.setOutDir(new File(this.filesDir + "/CGame"));
                    apkDecoder.setApkFile(new File(this.filesDir + "/Sign.apk"));
                    try {
                        try {
                            apkDecoder.decode();
                        } catch (AndrolibException e3) {
                            androlibException = e3;
                            i = 1;
                            androlibException.printStackTrace();
                            luaState2 = luaState;
                            luaState2.load(str11, "CBuilder code");
                            luaState2.pushValue(i);
                            luaState2.call(i, i);
                        }
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    new ProcessBuilder(this.script2Path, this.aapt2Path, this.filesDir, str2, str3).start().waitFor();
                    String str17 = this.filesDir + "/base.zip";
                    String str18 = this.filesDir + "/base";
                    try {
                        new ZipFile(str17).extractAll(str18);
                        new File(str17).delete();
                    } catch (ZipException e5) {
                        e5.printStackTrace();
                    }
                    String[] strArr = new String[2];
                    strArr[0] = this.script3Path;
                    i = 1;
                    try {
                        strArr[1] = this.filesDir;
                        new ProcessBuilder(strArr).start().waitFor();
                        try {
                            ZipParameters zipParameters = new ZipParameters();
                            zipParameters.setCompressionLevel(CompressionLevel.ULTRA);
                            ZipFile zipFile = new ZipFile(str17);
                            zipFile.addFolder(new File(str18 + "/manifest"), zipParameters);
                            zipFile.addFolder(new File(str18 + "/dex"), zipParameters);
                            zipFile.addFolder(new File(str18 + "/res"), zipParameters);
                            zipFile.addFolder(new File(str18 + "/assets"), zipParameters);
                            zipFile.addFolder(new File(str18 + "/lib"), zipParameters);
                            zipFile.addFolder(new File(str18 + "/root"), zipParameters);
                            zipFile.addFile(new File(str18 + "/resources.pb"), zipParameters);
                        } catch (ZipException e6) {
                            e6.printStackTrace();
                        }
                        try {
                            if (Build.VERSION.SDK_INT >= 24) {
                                BuildBundleCommand.fromFlags(new FlagParser().parse("--modules=" + this.filesDir + "/base.zip", "--output=" + this.filesDir + "/CGame.aab")).execute();
                                Security.addProvider(new BouncyCastleProvider());
                                i = 1;
                                try {
                                    CustomKeySigner.signZip(new ZipSigner(), this.appDataDir + "/cbuilder.jks", str5.toCharArray(), str4, str5.toCharArray(), "SHA256withRSA", this.filesDir + "/CGame.aab", this.filesDir + "/Sign.aab");
                                } catch (Exception e7) {
                                    e = e7;
                                    e.printStackTrace();
                                    luaState2 = luaState;
                                    luaState2.load(str11, "CBuilder code");
                                    luaState2.pushValue(i);
                                    luaState2.call(i, i);
                                }
                            } else {
                                i = 1;
                            }
                        } catch (Exception e8) {
                            e = e8;
                            i = 1;
                        }
                    } catch (AndrolibException e9) {
                        e = e9;
                        androlibException = e;
                        androlibException.printStackTrace();
                        luaState2 = luaState;
                        luaState2.load(str11, "CBuilder code");
                        luaState2.pushValue(i);
                        luaState2.call(i, i);
                    }
                } catch (AndrolibException e10) {
                    e = e10;
                    i = 1;
                }
                luaState2 = luaState;
                luaState2.load(str11, "CBuilder code");
            } else {
                luaState2 = luaState;
                i = 1;
                luaState2.load(str10, "CBuilder code");
            }
            luaState2.pushValue(i);
            luaState2.call(i, i);
        } catch (Exception e11) {
            e11.printStackTrace();
        }
    }

    public String encode(String str, String str2, String str3) {
        try {
            FileUtils.writeByteArrayToFile(new File(str2), new Encoder().encodeFile(this.context, str));
            return str3 + "\nencode manifest okay";
        } catch (Exception e) {
            String str4 = str3 + "\nencode manifest false";
            e.printStackTrace();
            return str4;
        }
    }

    public boolean fileExists(String str) {
        File file = new File(str);
        return file.exists() && file.isFile();
    }
}
