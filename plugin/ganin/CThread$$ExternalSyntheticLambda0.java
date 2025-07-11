package plugin.ganin;

import com.ansca.corona.CoronaRuntimeTaskDispatcher;

/* compiled from: D8$$SyntheticClass */
//
public final /* synthetic */ class CThread$$ExternalSyntheticLambda0 implements Runnable {
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
}
