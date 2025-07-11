package plugin.ganin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CPasteboardPaste implements NamedJavaFunction {
    private CoronaRuntimeTaskDispatcher dispatcher;
    private int fListener;

    public String getName() {
        return "pasteboardPaste";
    }

    private static class ThreadTask implements CoronaRuntimeTask {
        private final int fLuaListenerRegistryId;
        private final String resultString;

        public ThreadTask(int i, String str) {
            this.fLuaListenerRegistryId = i;
            this.resultString = str;
        }

        public void executeUsing(CoronaRuntime coronaRuntime) {
            LuaState luaState = coronaRuntime.getLuaState();
            try {
                CoronaLua.newEvent(luaState, "PasteboardEvent");
                luaState.pushString(this.resultString);
                luaState.setField(-2, "string");
                CoronaLua.dispatchEvent(luaState, this.fLuaListenerRegistryId, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int invoke(LuaState luaState) {
        this.fListener = CoronaLua.newRef(luaState, 1);
        this.dispatcher = new CoronaRuntimeTaskDispatcher(luaState);
        String lastClipboardText = getLastClipboardText(CoronaEnvironment.getApplicationContext());
        if (lastClipboardText != null) {
            this.dispatcher.send(new ThreadTask(this.fListener, lastClipboardText));
        }
        luaState.pushBoolean(true);
        return 1;
    }

    public String getLastClipboardText(Context context) {
        ClipData primaryClip;
        CharSequence text;
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService("clipboard");
        if (clipboardManager == null || !clipboardManager.hasPrimaryClip() || (primaryClip = clipboardManager.getPrimaryClip()) == null || primaryClip.getItemCount() <= 0 || (text = primaryClip.getItemAt(0).getText()) == null) {
            return null;
        }
        return text.toString();
    }
}
