package plugin.ganin;

import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CPath implements NamedJavaFunction {
    public String getName() {
        return "path";
    }

    public int invoke(LuaState luaState) {
        luaState.pushString(CoronaEnvironment.getApplicationContext().getApplicationInfo().dataDir);
        return 1;
    }
}
