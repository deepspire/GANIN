package com.ganin.server;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//
public class ConnectedThread extends Thread {
    private final Handler mHandler;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final BluetoothSocket mmSocket;

    public ConnectedThread(BluetoothSocket bluetoothSocket, Handler handler) {
        InputStream inputStream;
        this.mmSocket = bluetoothSocket;
        this.mHandler = handler;
        OutputStream outputStream = null;
        try {
            inputStream = bluetoothSocket.getInputStream();
            try {
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e = e;
                e.printStackTrace();
                this.mmInStream = inputStream;
                this.mmOutStream = outputStream;
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
        }
        this.mmInStream = inputStream;
        this.mmOutStream = outputStream;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                byte[] bArr = new byte[8192];
                this.mHandler.obtainMessage(0, this.mmInStream.read(bArr), -1, bArr).sendToTarget();
            } catch (IOException unused) {
                return;
            }
        }
    }

    public void write(byte[] bArr) {
        try {
            this.mmOutStream.flush();
            this.mmOutStream.write(bArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            this.mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
