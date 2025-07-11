package plugin.ganin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CPasteboardCopy implements NamedJavaFunction {
    public String getName() {
        return "pasteboardCopy";
    }

    public int invoke(LuaState luaState) {
        copyToClipboard(CoronaEnvironment.getApplicationContext(), luaState.toString(1));
        luaState.pushBoolean(true);
        return 1;
    }

    public void copyToClipboard(Context context, String str) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", str));
    }
}
