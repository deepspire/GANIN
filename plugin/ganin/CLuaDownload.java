package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

//
public class CLuaDownload implements NamedJavaFunction {
    public String getName() {
        return "lua";
    }

    public int invoke(LuaState luaState) {
        String luaState2 = luaState.toString(1);
        try {
            new ZipFile(luaState2).extractAll(luaState.toString(2));
        } catch (ZipException e) {
            e.printStackTrace();
        }
        luaState.pushBoolean(true);
        return 1;
    }
}
