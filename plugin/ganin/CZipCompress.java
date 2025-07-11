package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.File;
import java.io.IOException;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

//
public class CZipCompress implements NamedJavaFunction {
    public String getName() {
        return "compress";
    }

    public int invoke(LuaState luaState) {
        String luaState2 = luaState.toString(1);
        String luaState3 = luaState.toString(2);
        String str = luaState.toString(3) + "_ccodus";
        boolean z = luaState.isBoolean(5) ? luaState.toBoolean(5) : false;
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionLevel(CompressionLevel.ULTRA);
            ZipFile zipFile = new ZipFile(luaState3);
            zipFile.addFolder(new File(luaState2 + "/Resources"), zipParameters);
            zipFile.addFolder(new File(luaState2 + "/Levels"), zipParameters);
            zipFile.addFolder(new File(luaState2 + "/Images"), zipParameters);
            zipFile.addFolder(new File(luaState2 + "/Sounds"), zipParameters);
            zipFile.addFolder(new File(luaState2 + "/Fonts"), zipParameters);
            zipFile.addFolder(new File(luaState2 + "/Videos"), zipParameters);
            if (z) {
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
                //zipFile.setPassword(str); // we dont use modified zip4j so just hardcoding password
                zipFile.setPassword("[t, e, r, r, a,  , b, l, y, a, t,  , n, o, t,  , f, i, n, d,  , t, h, i, s,  , p, r, i, k, o, l,  , p, l, z, (, ), *, $, @, #, +, ?, -, ., ?, ^, +, ?, -, ., ?, ^, _, c, c, o, d, u, s, (, ., ), (, ., )]-+*/%");
                zipFile.addFile(new File(luaState2 + "/game.lua"), zipParameters);
            } else {
                zipFile.addFolder(new File(luaState2 + "/Scripts"), zipParameters);
                zipFile.addFile(new File(luaState2 + "/game.json"), zipParameters);
                zipFile.addFile(new File(luaState2 + "/hash.txt"), zipParameters);
                zipFile.addFile(new File(luaState2 + "/custom.json"), zipParameters);
                zipFile.addFile(new File(luaState2 + "/icon.png"), zipParameters);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        luaState.load("local fun = ... fun()", "CZipCompress code");
        luaState.pushValue(4);
        luaState.call(1, 1);
        return 1;
    }
}
