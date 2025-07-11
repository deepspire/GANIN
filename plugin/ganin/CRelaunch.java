package plugin.ganin;

import com.ansca.corona.CoronaEnvironment;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CRelaunch implements NamedJavaFunction {
    public String getName() {
        return "relaunch";
    }

    public int invoke(LuaState luaState) {
        ProcessPhoenix.triggerRebirth(CoronaEnvironment.getApplicationContext());
        return 1;
    }
}
