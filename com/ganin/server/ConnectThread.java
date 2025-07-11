package com.ganin.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

//
public class ConnectThread extends Thread {
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Handler mHandler;
    private final BluetoothSocket mmSocket;
    private ConnectedThread thread;

    public ConnectThread(BluetoothDevice bluetoothDevice, UUID uuid, Handler handler) {
        BluetoothSocket bluetoothSocket;
        this.mHandler = handler;
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
            bluetoothSocket = null;
        }
        this.mmSocket = bluetoothSocket;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.mBluetoothAdapter.cancelDiscovery();
        try {
            try {
                this.mmSocket.connect();
                ConnectedThread connectedThread = new ConnectedThread(this.mmSocket, this.mHandler);
                this.thread = connectedThread;
                connectedThread.start();
                this.thread.write("connected".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException unused) {
            this.mmSocket.close();
        }
    }

    public void write(String str) {
        this.thread.write(str.getBytes(StandardCharsets.UTF_8));
    }

    public void cancel() {
        try {
            this.mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
