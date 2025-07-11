package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CWidgetBlocks implements NamedJavaFunction {
    public String getName() {
        return "widget";
    }

    public int invoke(LuaState luaState) {
        String luaState2 = luaState.toString(1);
        luaState2.hashCode();
        if (!luaState2.equals("edittext")) {
            luaState.pushNil();
        }
        return 1;
    }
}
