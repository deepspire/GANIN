package plugin.ganin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CPasteboardClear implements NamedJavaFunction {
    public String getName() {
        return "pasteboardClear";
    }

    public int invoke(LuaState luaState) {
        clearClipboard(CoronaEnvironment.getApplicationContext());
        luaState.pushBoolean(true);
        return 1;
    }

    public void clearClipboard(Context context) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("", ""));
    }
}
