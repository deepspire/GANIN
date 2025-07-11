package com.ganin.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

//
public class AcceptThread extends Thread {
    private final BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private final BluetoothServerSocket mmServerSocket;
    private ConnectedThread thread;

    public AcceptThread(UUID uuid, Handler handler) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        BluetoothServerSocket bluetoothServerSocket = null;
        this.thread = null;
        this.mHandler = handler;
        try {
            bluetoothServerSocket = defaultAdapter.listenUsingRfcommWithServiceRecord("CCode", uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mmServerSocket = bluetoothServerSocket;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        BluetoothSocket accept;
        do {
            try {
                accept = this.mmServerSocket.accept();
            } catch (IOException unused) {
                return;
            }
        } while (accept == null);
        ConnectedThread connectedThread = new ConnectedThread(accept, this.mHandler);
        this.thread = connectedThread;
        connectedThread.start();
        this.thread.write("connected".getBytes(StandardCharsets.UTF_8));
        cancel();
    }

    public void write(String str) {
        ConnectedThread connectedThread = this.thread;
        if (connectedThread != null) {
            connectedThread.write(str.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void cancel() {
        try {
            this.mmServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
