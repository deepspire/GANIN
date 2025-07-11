package plugin.ganin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;
import com.ganin.server.AcceptThread;
import com.ganin.server.ConnectThread;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

//
public class CBluetooth implements NamedJavaFunction {
    private CoronaRuntimeTaskDispatcher dispatcher;
    private int fListener;
    private final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private AcceptThread acceptThread = null;
    private ConnectThread connectThread = null;

    public String getName() {
        return "bluetooth";
    }

    public boolean checkPerms(String str) {
        return ActivityCompat.checkSelfPermission(CoronaEnvironment.getApplicationContext(), str) == 0;
    }

    public String getDevice(CoronaActivity coronaActivity) {
        StringBuilder sb = new StringBuilder();
        BluetoothAdapter bluetoothAdapter = this.bluetooth;
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                sb.append("{\"name\":\"");
                sb.append(this.bluetooth.getName());
                sb.append("\", \"address\": \"");
                sb.append(this.bluetooth.getAddress());
                sb.append("\"}");
            } else if (checkPerms("android.permission.BLUETOOTH_CONNECT") || checkPerms("android.permission.BLUETOOTH_ADMIN")) {
                coronaActivity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
            }
        }
        return sb.toString();
    }

    public String getDevices(CoronaActivity coronaActivity) {
        StringBuilder sb = new StringBuilder();
        BluetoothAdapter bluetoothAdapter = this.bluetooth;
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = this.bluetooth.getBondedDevices();
                if (bondedDevices.size() > 0) {
                    for (BluetoothDevice bluetoothDevice : bondedDevices) {
                        sb.append("{\"name\":\"");
                        sb.append(bluetoothDevice.getName());
                        sb.append("\", \"address\": \"");
                        sb.append(bluetoothDevice.getAddress());
                        sb.append("\"},");
                    }
                }
            } else if (checkPerms("android.permission.BLUETOOTH_CONNECT") || checkPerms("android.permission.BLUETOOTH_ADMIN")) {
                coronaActivity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
            }
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return "[" + ((Object) sb) + "]";
    }

    public BluetoothDevice getBluetoothDevice(CoronaActivity coronaActivity, String str) {
        BluetoothAdapter bluetoothAdapter = this.bluetooth;
        BluetoothDevice bluetoothDevice = null;
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = this.bluetooth.getBondedDevices();
                if (bondedDevices.size() > 0) {
                    for (BluetoothDevice bluetoothDevice2 : bondedDevices) {
                        if (Objects.equals(str, bluetoothDevice2.getAddress())) {
                            bluetoothDevice = bluetoothDevice2;
                        }
                    }
                }
            } else if (checkPerms("android.permission.BLUETOOTH_CONNECT") || checkPerms("android.permission.BLUETOOTH_ADMIN")) {
                coronaActivity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
            }
        }
        return bluetoothDevice;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int invoke(LuaState luaState) {
        CoronaActivity coronaActivity;
        String device;
        coronaActivity = CoronaEnvironment.getCoronaActivity();
        device = getDevice(coronaActivity);
        String luaState2 = luaState.toString(1);
        luaState2.hashCode();
        switch (luaState2) {
            case "device":
                luaState.pushString(device);
                return 1;
            case "host":
                AcceptThread acceptThread = this.acceptThread;
                if (acceptThread != null) {
                    acceptThread.cancel();
                }
                this.fListener = CoronaLua.newRef(luaState, 2);
                this.dispatcher = new CoronaRuntimeTaskDispatcher(luaState);
                AcceptThread acceptThread2 = new AcceptThread(UUID.fromString("b31f9f8f-216c-4cd2-9ef7-d5b361c8fdc1"), new Handler(Looper.getMainLooper()) { // from class: plugin.ganin.CBluetooth.2
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        CBluetooth.this.dispatcher.send(new ThreadTask(CBluetooth.this.fListener, new String((byte[]) message.obj, StandardCharsets.UTF_8)));
                    }
                });
                this.acceptThread = acceptThread2;
                acceptThread2.start();
                return 1;
            case "send":
                AcceptThread acceptThread3 = this.acceptThread;
                if (acceptThread3 != null) {
                    acceptThread3.write(luaState.toString(2));
                    luaState.pushBoolean(true);
                } else {
                    ConnectThread connectThread = this.connectThread;
                    if (connectThread != null) {
                        connectThread.write(luaState.toString(2));
                        luaState.pushBoolean(true);
                    } else {
                        luaState.pushBoolean(false);
                    }
                }
                return 1;
            case "connect":
                ConnectThread connectThread2 = this.connectThread;
                if (connectThread2 != null) {
                    connectThread2.cancel();
                }
                this.fListener = CoronaLua.newRef(luaState, 3);
                this.dispatcher = new CoronaRuntimeTaskDispatcher(luaState);
                BluetoothDevice bluetoothDevice = getBluetoothDevice(coronaActivity, luaState.toString(2));
                if (bluetoothDevice == null) {
                    return 0;
                }
                ConnectThread connectThread3 = new ConnectThread(bluetoothDevice, UUID.fromString("b31f9f8f-216c-4cd2-9ef7-d5b361c8fdc1"), new Handler(Looper.getMainLooper()) { // from class: plugin.ganin.CBluetooth.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        CBluetooth.this.dispatcher.send(new ThreadTask(CBluetooth.this.fListener, new String((byte[]) message.obj, StandardCharsets.UTF_8)));
                    }
                });
                this.connectThread = connectThread3;
                connectThread3.start();
                return 1;
            case "devices":
                luaState.pushString(getDevices(coronaActivity));
                return 1;
            default:
                return 1;
        }
    }

    private static class ThreadTask implements CoronaRuntimeTask {
        private final int fLuaListenerRegistryId;
        private final String result;

        public ThreadTask(int i, String str) {
            this.fLuaListenerRegistryId = i;
            this.result = str;
        }

        public void executeUsing(CoronaRuntime coronaRuntime) {
            LuaState luaState = coronaRuntime.getLuaState();
            try {
                CoronaLua.newEvent(luaState, "BluetoothEvent");
                luaState.pushString(this.result);
                luaState.setField(-2, "result");
                CoronaLua.dispatchEvent(luaState, this.fLuaListenerRegistryId, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
