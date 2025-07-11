package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CCustom implements NamedJavaFunction {
    public String getName() {
        return "new";
    }

    public int invoke(LuaState luaState) {
        luaState.pushBoolean(true);
        return 1;
    }
}
