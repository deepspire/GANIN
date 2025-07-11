package plugin.ganin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CPaint implements NamedJavaFunction {
    private static final int CREATE_IMAGE_REQUEST = 1001;
    private CoronaRuntimeTaskDispatcher dispatcher;
    private int fListener;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public String getName() {
        return "cpaint";
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
                CoronaLua.newEvent(luaState, "PaintEvent");
                luaState.pushString(this.result);
                luaState.setField(-2, "result");
                CoronaLua.dispatchEvent(luaState, this.fLuaListenerRegistryId, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int invoke(LuaState luaState) {
        CoronaEnvironment.getApplicationContext();
        CoronaEnvironment.getCoronaActivity();
        luaState.toString(1);
        this.fListener = CoronaLua.newRef(luaState, 2);
        this.dispatcher = new CoronaRuntimeTaskDispatcher(luaState);
        Log.d("CPaint", "Starting paint operation");
        luaState.pushBoolean(true);
        return 1;
    }

    private void openPlayStoreForDrawingApps(CoronaActivity coronaActivity) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("market://search?q=drawing app"));
            coronaActivity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(Uri.parse("https://play.google.com/store/search?q=drawing app"));
            coronaActivity.startActivity(intent2);
        }
    }
}
