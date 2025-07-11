package plugin.ganin;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.File;

//
public class CUpdate implements NamedJavaFunction {
    public String getName() {
        return "update";
    }

    void installAPK(String str, Context context) {
        if (new File(str).exists()) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriFromFile(context, new File(str)), "application/vnd.android.package-archive");
            intent.addFlags(268435456);
            intent.addFlags(1);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.d("TAG", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                return FileProvider.getUriForFile(context, "com.ganin.ccode.provider", file);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return Uri.fromFile(file);
    }

    public int invoke(LuaState luaState) {
        installAPK(luaState.toString(1), CoronaEnvironment.getApplicationContext());
        luaState.pushBoolean(true);
        return 1;
    }
}
