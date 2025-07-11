package plugin.ganin;

import android.content.Context;
import android.util.Log;
import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;
import com.ganin.cbuilder.util.FileUtil;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//
public class CThread implements NamedJavaFunction {
    private int fListener;
    private String result;

    public String getName() {
        return "thread";
    }

    public int invoke(LuaState luaState) {
        String luaState2 = luaState.toString(1);
        String luaState3 = luaState.toString(2);
        Context applicationContext = CoronaEnvironment.getApplicationContext();
        FileUtil fileUtil = new FileUtil(applicationContext.getAssets(), luaState2);
        String str = applicationContext.getApplicationInfo().nativeLibraryDir + "/libscript.so";
        String str2 = applicationContext.getApplicationInfo().nativeLibraryDir + "/liblua5.so";
        fileUtil.saveFile(luaState2 + "/file.lua", luaState3);
        CoronaActivity coronaActivity = CoronaEnvironment.getCoronaActivity() != null ? CoronaEnvironment.getCoronaActivity() : null;
        this.fListener = CoronaLua.newRef(luaState, 3);
        CThread$$ExternalSyntheticLambda0 cThread$$ExternalSyntheticLambda0 = new Runnable(str, str2, luaState2, new CoronaRuntimeTaskDispatcher(luaState)) { // from class: plugin.ganin.CThread$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ CoronaRuntimeTaskDispatcher f$4;

            public /* synthetic */ CThread$$ExternalSyntheticLambda0(String str3, String str22, String luaState22, CoronaRuntimeTaskDispatcher coronaRuntimeTaskDispatcher) {
                this.f$1 = str3;
                this.f$2 = str22;
                this.f$3 = luaState22;
                this.f$4 = coronaRuntimeTaskDispatcher;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CThread.this.m298lambda$invoke$0$pluginganinCThread(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        };
        if (coronaActivity == null) {
            return 0;
        }
        new Thread(cThread$$ExternalSyntheticLambda0).start();
        return 0;
    }

    /* renamed from: lambda$invoke$0$plugin-ganin-CThread */
    /* synthetic */ void m298lambda$invoke$0$pluginganinCThread(String str, String str2, String str3, CoronaRuntimeTaskDispatcher coronaRuntimeTaskDispatcher) {
        Log.d("TAG", "run");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ProcessBuilder(str, str2, str3).start().getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append(",");
            }
            int length = sb.length();
            sb.delete(length - 1, length);
            this.result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        coronaRuntimeTaskDispatcher.send(new ThreadTask(this.fListener, this.result));
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
                CoronaLua.newEvent(luaState, "ThreadEvent");
                luaState.pushString(this.result);
                luaState.setField(-2, "result");
                CoronaLua.dispatchEvent(luaState, this.fLuaListenerRegistryId, 0);
                CoronaLua.deleteRef(luaState, this.fLuaListenerRegistryId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
