package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.File;
import kellinwood.security.zipsigner.ZipSigner;
import kellinwood.security.zipsigner.optional.CertCreator;
import kellinwood.security.zipsigner.optional.DistinguishedNameValues;

//
public class CKeystore implements NamedJavaFunction {
    public String getName() {
        return "keystore";
    }

    public int invoke(LuaState luaState) {
        String luaState2 = luaState.toString(1);
        String luaState3 = luaState.toString(2);
        char[] charArray = luaState.toString(3).toCharArray();
        try {
            new ZipSigner().issueLoadingCertAndKeysProgressEvent();
            DistinguishedNameValues distinguishedNameValues = new DistinguishedNameValues();
            distinguishedNameValues.setCommonName(luaState3);
            new File(luaState2 + "/cbuilder.jks").delete();
            CertCreator.createKeystoreAndKey(luaState2 + "/cbuilder.jks", charArray, luaState3, distinguishedNameValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        luaState.pushBoolean(true);
        return 1;
    }
}
