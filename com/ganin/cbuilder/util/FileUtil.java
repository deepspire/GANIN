package com.ganin.cbuilder.util;

import android.content.res.AssetManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//
public class FileUtil {
    private final AssetManager assetManager;
    private final String filesDir;

    public FileUtil(AssetManager assetManager, String str) {
        this.assetManager = assetManager;
        this.filesDir = str;
    }

    public void copyAsset(String str, String str2) {
        try {
            InputStream open = this.assetManager.open(str);
            new File(str2).createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            copyFile2(open, fileOutputStream);
            open.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile2(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return;
            } else {
                outputStream.write(bArr, 0, read);
            }
        }
    }

    public void copyFileWithPath(String str, String str2) throws IOException {
        copyFile2(new FileInputStream(str), new FileOutputStream(str2));
    }

    public void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                deleteRecursive(file2);
            }
        }
        file.delete();
    }

    public void copyFile(String str, String str2) {
        String str3;
        try {
            InputStream open = this.assetManager.open(str);
            if (str2 != null) {
                str3 = this.filesDir + "/" + str2;
            } else {
                str3 = this.filesDir + "/" + str;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(str3);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    open.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readAssetsFile(String str) {
        try {
            InputStream open = this.assetManager.open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            return new String(bArr);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void saveFile(String str, String str2) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            fileOutputStream.write(str2.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
