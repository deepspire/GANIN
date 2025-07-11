package plugin.ganin;

import android.util.Log;
import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CWidgets implements NamedJavaFunction {
    public String getName() {
        return "widgets";
    }

    public int invoke(LuaState luaState) {
        try {
            CoronaActivity coronaActivity = CoronaEnvironment.getCoronaActivity();
            byte[] byteArray = coronaActivity.getPackageManager().getPackageInfo(coronaActivity.getPackageName(), 64).signatures[0].toByteArray();
            StringBuilder sb = new StringBuilder();
            for (byte b : byteArray) {
                sb.append(String.format("%02x", Byte.valueOf(b)));
            }
            Log.d("HEX", sb.toString());
            luaState.pushString(sb.toString());
        } catch (Exception e) {
            luaState.pushString("");
            e.printStackTrace();
        }
        return 1;
    }
}
