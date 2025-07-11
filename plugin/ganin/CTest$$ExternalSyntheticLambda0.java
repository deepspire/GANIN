package plugin.ganin;

import com.ansca.corona.CoronaRuntimeTaskDispatcher;

/* compiled from: D8$$SyntheticClass */
//
public final /* synthetic */ class CTest$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ CoronaRuntimeTaskDispatcher f$4;

    public /* synthetic */ CTest$$ExternalSyntheticLambda0(String str, String str2, String str3, CoronaRuntimeTaskDispatcher coronaRuntimeTaskDispatcher) {
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = str3;
        this.f$4 = coronaRuntimeTaskDispatcher;
    }

    @Override // java.lang.Runnable
    public final void run() {
        CTest.this.m297lambda$invoke$0$pluginganinCTest(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
