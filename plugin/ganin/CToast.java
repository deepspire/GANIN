package plugin.ganin;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CToast implements NamedJavaFunction {
    public String getName() {
        return "toast";
    }

    public int invoke(LuaState luaState) {
        new Handler(Looper.getMainLooper()).post(new Runnable(luaState.toString(1), luaState.toInteger(2)) { // from class: plugin.ganin.CToast$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$0;
            public final /* synthetic */ int f$1;

            public /* synthetic */ CToast$$ExternalSyntheticLambda0(String str, int i) {
                this.f$0 = str;
                this.f$1 = i;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CToast.lambda$invoke$0(this.f$0, this.f$1);
            }
        });
        luaState.pushBoolean(true);
        return 1;
    }

    static /* synthetic */ void lambda$invoke$0(String str, int i) {
        Toast.makeText(CoronaEnvironment.getApplicationContext(), str, i).show();
    }
}
