package plugin.ganin;

import android.content.Context;
import com.ansca.corona.CoronaEnvironment;
import com.ganin.cbuilder.util.ApkBuilderUtil;
import com.ganin.cbuilder.util.ApkSignerUtil;
import com.ganin.cbuilder.util.FileUtil;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.File;

//
public class CBuilder implements NamedJavaFunction {
    public String getName() {
        return "build";
    }

    public int invoke(LuaState luaState) {
        if (!luaState.isString(1) || !luaState.isString(2) || !luaState.isString(3) || !luaState.isNumber(4) || !luaState.isString(5)) {
            luaState.pushNil();
            luaState.pushString("Arguments should be string! Argument 4 should be number!");
            return 2;
        }
        String luaState2 = luaState.toString(1);
        String luaState3 = luaState.toString(7);
        Context applicationContext = CoronaEnvironment.getApplicationContext();
        FileUtil fileUtil = new FileUtil(applicationContext.getAssets(), luaState2);
        String replace = fileUtil.readAssetsFile("AndroidManifest").replace("com.ganin.cgame", luaState.toString(2)).replace("label=\"CGame\"", "label=\"" + luaState.toString(3) + "\"").replace("versionCode=\"1\"", "versionCode=\"" + luaState.toString(4) + "\"").replace("versionName=\"1.0\"", "versionName=\"" + luaState.toString(5) + "\"");
        String replace2 = fileUtil.readAssetsFile("AndroidManifest2").replace("com.ganin.cgame", luaState.toString(2)).replace("label=\"CGame\"", "label=\"" + luaState.toString(3) + "\"");
        fileUtil.saveFile(luaState2 + "/AndroidManifest1.xml", replace);
        fileUtil.saveFile(luaState2 + "/AndroidManifest2.xml", replace2);
        ApkSignerUtil apkSignerUtil = new ApkSignerUtil("testkey.pk8", "testkey.x509.pem", luaState2, new File(luaState2 + "/Align.apk"), new File(luaState2 + "/Sign.apk"));
        if (luaState.toBoolean(6)) {
            fileUtil.copyAsset("android", luaState2 + "/android.jar");
        }
        new ApkBuilderUtil(applicationContext, luaState2, luaState3).apkModify(fileUtil, apkSignerUtil, luaState, luaState.toString(3), luaState.toBoolean(6), luaState.toString(4), luaState.toString(5), luaState.toString(8), luaState.toString(9));
        luaState.pushBoolean(true);
        return 1;
    }
}
