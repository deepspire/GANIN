package plugin.ganin;

import android.util.Log;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CNumber implements NamedJavaFunction {
    public String getName() {
        return "number";
    }

    public int invoke(LuaState luaState) {
        double parseDouble = Double.parseDouble(String.format("%.10f", Double.valueOf(luaState.toNumber(1))));
        Log.d("TAG", "" + parseDouble);
        luaState.pushNumber(parseDouble);
        return 1;
    }
}
