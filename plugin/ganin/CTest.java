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
import java.io.IOException;

//
public class CTest implements NamedJavaFunction {
    private int fListener;
    private String result;

    public String getName() {
        return "test";
    }

    public int invoke(LuaState luaState) {
        String luaState2 = luaState.toString(1);
        Context applicationContext = CoronaEnvironment.getApplicationContext();
        FileUtil fileUtil = new FileUtil(applicationContext.getAssets(), luaState2);
        String str = applicationContext.getApplicationInfo().nativeLibraryDir + "/libscript2.so";
        String str2 = applicationContext.getApplicationInfo().nativeLibraryDir + "/libjavac.so";
        fileUtil.copyFile("libz.so.1", "libz.so.1");
        CoronaActivity coronaActivity = CoronaEnvironment.getCoronaActivity() != null ? CoronaEnvironment.getCoronaActivity() : null;
        this.fListener = CoronaLua.newRef(luaState, 2);
        CTest$$ExternalSyntheticLambda0 cTest$$ExternalSyntheticLambda0 = new Runnable(str, str2, luaState2, new CoronaRuntimeTaskDispatcher(luaState)) { // from class: plugin.ganin.CTest$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ CoronaRuntimeTaskDispatcher f$4;

            public /* synthetic */ CTest$$ExternalSyntheticLambda0(String str3, String str22, String luaState22, CoronaRuntimeTaskDispatcher coronaRuntimeTaskDispatcher) {
                this.f$1 = str3;
                this.f$2 = str22;
                this.f$3 = luaState22;
                this.f$4 = coronaRuntimeTaskDispatcher;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CTest.this.m297lambda$invoke$0$pluginganinCTest(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        };
        if (coronaActivity == null) {
            return 0;
        }
        new Thread(cTest$$ExternalSyntheticLambda0).start();
        return 0;
    }

    /* renamed from: lambda$invoke$0$plugin-ganin-CTest */
    /* synthetic */ void m297lambda$invoke$0$pluginganinCTest(String str, String str2, String str3, CoronaRuntimeTaskDispatcher coronaRuntimeTaskDispatcher) {
        Log.d("TAG", "run");
        try {
            new ProcessBuilder(str, str2, str3).start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        coronaRuntimeTaskDispatcher.send(new ThreadTask(this.fListener, ""));
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
                CoronaLua.newEvent(luaState, "TestEvent");
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
