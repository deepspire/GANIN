package plugin.ganin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CNotificationSchedule implements NamedJavaFunction {
    public String getName() {
        return "scheduleNotification";
    }

    public int invoke(LuaState luaState) {
        luaState.pushBoolean(true);
        return 1;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int i = applicationInfo.labelRes;
        return i == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(i);
    }
}
