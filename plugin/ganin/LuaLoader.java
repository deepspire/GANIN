package plugin.ganin;

import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;
import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeListener;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.permissions.PermissionState;
import com.ansca.corona.permissions.PermissionsServices;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class LuaLoader implements JavaFunction, CoronaRuntimeListener {
    private static final String EVENT_NAME = "pluginLibraryEvent";
    private int fListener = -1;

    public void onLoaded(CoronaRuntime coronaRuntime) {
    }

    public void onResumed(CoronaRuntime coronaRuntime) {
    }

    public void onStarted(CoronaRuntime coronaRuntime) {
    }

    public void onSuspended(CoronaRuntime coronaRuntime) {
    }

    static /* synthetic */ int access$200(LuaLoader luaLoader) {
        return luaLoader.fListener;
    }

    public LuaLoader() {
        CoronaEnvironment.addRuntimeListener(this);
    }

    public int invoke(LuaState luaState) {
        luaState.register(luaState.toString(1), new NamedJavaFunction[]{new InitWrapper(this, null), new ShowWrapper(this, null), new CZipCompress(), new CPasteboardClear(), new CPasteboardCopy(), new CPasteboardPaste(), new CPaint(), new CZipUncompress(), new CFileParameters(), new CWidgetBlocks(), new CLuaDownload(), new CZipBlocks(), new CBluetooth(), new CKeystore(), new CRelaunch(), new CConvert(), new CCustom(), new CThread(), new CIsZip(), new CWidgets(), new CUpdate(), new CScroll2(), new CScroll(), new CToast(), new CNumber(), new CPath(), new CPerm(), new CPermit(), new CSize(), new CTest(), new CBuilder()});
        return 1;
    }

    public void onExiting(CoronaRuntime coronaRuntime) {
        CoronaLua.deleteRef(coronaRuntime.getLuaState(), this.fListener);
        this.fListener = -1;
    }

    /* renamed from: plugin.ganin.LuaLoader$1 */
    class AnonymousClass1 implements CoronaRuntimeTask {
        final /* synthetic */ String val$message;

        AnonymousClass1(String str) {
            this.val$message = str;
        }

        public void executeUsing(CoronaRuntime coronaRuntime) {
            LuaState luaState = coronaRuntime.getLuaState();
            CoronaLua.newEvent(luaState, LuaLoader.EVENT_NAME);
            luaState.pushString(this.val$message);
            luaState.setField(-2, "message");
            try {
                CoronaLua.dispatchEvent(luaState, LuaLoader.access$200(LuaLoader.this), 0);
            } catch (Exception unused) {
            }
        }
    }

    public void dispatchEvent(String str) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask(str) { // from class: plugin.ganin.LuaLoader.1
            final /* synthetic */ String val$message;

            AnonymousClass1(String str2) {
                this.val$message = str2;
            }

            public void executeUsing(CoronaRuntime coronaRuntime) {
                LuaState luaState = coronaRuntime.getLuaState();
                CoronaLua.newEvent(luaState, LuaLoader.EVENT_NAME);
                luaState.pushString(this.val$message);
                luaState.setField(-2, "message");
                try {
                    CoronaLua.dispatchEvent(luaState, LuaLoader.access$200(LuaLoader.this), 0);
                } catch (Exception unused) {
                }
            }
        });
    }

    public int init(LuaState luaState) {
        if (!CoronaLua.isListener(luaState, 1, EVENT_NAME)) {
            return 0;
        }
        this.fListener = CoronaLua.newRef(luaState, 1);
        return 0;
    }

    public int show(LuaState luaState) {
        CoronaActivity coronaActivity = CoronaEnvironment.getCoronaActivity();
        if (coronaActivity == null) {
            return 0;
        }
        String checkString = luaState.checkString(1);
        if (checkString == null) {
            checkString = "corona";
        }
        coronaActivity.runOnUiThread(new Runnable("https://dictionary.reference.com/browse/" + checkString) { // from class: plugin.ganin.LuaLoader.2
            final /* synthetic */ String val$url;

            AnonymousClass2(String str) {
                this.val$url = str;
            }

            @Override // java.lang.Runnable
            public void run() {
                CoronaActivity coronaActivity2 = CoronaEnvironment.getCoronaActivity();
                if (coronaActivity2 == null) {
                    return;
                }
                int i = AnonymousClass4.$SwitchMap$com$ansca$corona$permissions$PermissionState[new PermissionsServices(coronaActivity2.getApplicationContext()).getPermissionStateFor("android.permission.INTERNET").ordinal()];
                if (i == 1) {
                    Toast.makeText((Context) coronaActivity2, (CharSequence) "Internet permission is not set up", 1).show();
                } else {
                    if (i == 2) {
                        Toast.makeText((Context) coronaActivity2, (CharSequence) "Internet permission is denied", 1).show();
                        return;
                    }
                    WebView webView = new WebView(coronaActivity2);
                    coronaActivity2.getOverlayView().addView(webView);
                    webView.loadUrl(this.val$url);
                }
            }
        });
        new Thread() { // from class: plugin.ganin.LuaLoader.3
            AnonymousClass3() {
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException unused) {
                }
                LuaLoader.this.dispatchEvent("Hello!");
            }
        }.start();
        return 0;
    }

    /* renamed from: plugin.ganin.LuaLoader$2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ String val$url;

        AnonymousClass2(String str) {
            this.val$url = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            CoronaActivity coronaActivity2 = CoronaEnvironment.getCoronaActivity();
            if (coronaActivity2 == null) {
                return;
            }
            int i = AnonymousClass4.$SwitchMap$com$ansca$corona$permissions$PermissionState[new PermissionsServices(coronaActivity2.getApplicationContext()).getPermissionStateFor("android.permission.INTERNET").ordinal()];
            if (i == 1) {
                Toast.makeText((Context) coronaActivity2, (CharSequence) "Internet permission is not set up", 1).show();
            } else {
                if (i == 2) {
                    Toast.makeText((Context) coronaActivity2, (CharSequence) "Internet permission is denied", 1).show();
                    return;
                }
                WebView webView = new WebView(coronaActivity2);
                coronaActivity2.getOverlayView().addView(webView);
                webView.loadUrl(this.val$url);
            }
        }
    }

    /* renamed from: plugin.ganin.LuaLoader$4 */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$ansca$corona$permissions$PermissionState;

        static {
            int[] iArr = new int[PermissionState.values().length];
            $SwitchMap$com$ansca$corona$permissions$PermissionState = iArr;
            try {
                iArr[PermissionState.MISSING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$ansca$corona$permissions$PermissionState[PermissionState.DENIED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$ansca$corona$permissions$PermissionState[PermissionState.GRANTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* renamed from: plugin.ganin.LuaLoader$3 */
    class AnonymousClass3 extends Thread {
        AnonymousClass3() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException unused) {
            }
            LuaLoader.this.dispatchEvent("Hello!");
        }
    }

    private class InitWrapper implements NamedJavaFunction {
        public String getName() {
            return "init";
        }

        private InitWrapper() {
        }

        /* synthetic */ InitWrapper(LuaLoader luaLoader, AnonymousClass1 anonymousClass1) {
            this();
        }

        public int invoke(LuaState luaState) {
            return LuaLoader.this.init(luaState);
        }
    }

    private class ShowWrapper implements NamedJavaFunction {
        public String getName() {
            return "show";
        }

        private ShowWrapper() {
        }

        /* synthetic */ ShowWrapper(LuaLoader luaLoader, AnonymousClass1 anonymousClass1) {
            this();
        }

        public int invoke(LuaState luaState) {
            return LuaLoader.this.show(luaState);
        }
    }
}
