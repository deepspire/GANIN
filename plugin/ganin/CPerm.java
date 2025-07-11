package plugin.ganin;

import android.os.Build;
import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.util.ArrayList;

//
public class CPerm implements NamedJavaFunction {
    public String getName() {
        return "perm";
    }

    public int invoke(LuaState luaState) {
        requestPermission(CoronaEnvironment.getCoronaActivity());
        luaState.pushBoolean(true);
        return 1;
    }

    public void requestPermission(CoronaActivity coronaActivity) {
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 33) {
            arrayList.add("android.permission.READ_MEDIA_IMAGES");
            arrayList.add("android.permission.READ_MEDIA_AUDIO");
            arrayList.add("android.permission.READ_MEDIA_VIDEO");
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            coronaActivity.requestPermissions((String[]) arrayList.toArray(new String[0]), 1);
        }
    }
}
