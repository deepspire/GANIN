package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.jose4j.jwx.HeaderParameterNames;

//
public class CZipBlocks implements NamedJavaFunction {
    public String getName() {
        return HeaderParameterNames.ZIP;
    }

    public int invoke(LuaState luaState) {
        ZipFile zipFile;
        String luaState2 = luaState.toString(1);
        try {
            if (luaState2.equals("compress")) {
                String luaState3 = luaState.toString(2);
                String luaState4 = luaState.toString(3);
                String luaState5 = luaState.toString(4);
                int integer = luaState.toInteger(5);
                String luaState6 = luaState.isString(6) ? luaState.toString(6) : null;
                ZipParameters zipParameters = new ZipParameters();
                ZipFile zipFile2 = new ZipFile(luaState4);
                switch (integer) {
                    case 0:
                        zipParameters.setCompressionLevel(CompressionLevel.NO_COMPRESSION);
                    case 1:
                        zipParameters.setCompressionLevel(CompressionLevel.FASTEST);
                    case 2:
                        zipParameters.setCompressionLevel(CompressionLevel.FASTER);
                    case 3:
                        zipParameters.setCompressionLevel(CompressionLevel.FAST);
                    case 4:
                        zipParameters.setCompressionLevel(CompressionLevel.MEDIUM_FAST);
                    case 5:
                        zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
                    case 6:
                        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
                    case 7:
                        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
                    case 8:
                        zipParameters.setCompressionLevel(CompressionLevel.PRE_ULTRA);
                        break;
                }
                zipParameters.setCompressionLevel(CompressionLevel.ULTRA);
                if (luaState6 != null) {
                    zipParameters.setEncryptFiles(true);
                    zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
                    //zipFile.setPassword(str); // we dont use modified zip4j so just hardcoding password
                    zipFile.setPassword("[t, e, r, r, a,  , b, l, y, a, t,  , n, o, t,  , f, i, n, d,  , t, h, i, s,  , p, r, i, k, o, l,  , p, l, z, (, ), *, $, @, #, +, ?, -, ., ?, ^, +, ?, -, ., ?, ^, _, c, c, o, d, u, s, (, ., ), (, ., )]-+*/%");
                }
                if (luaState3.equals("folder")) {
                    zipFile2.addFolder(new File(luaState5), zipParameters);
                } else {
                    zipFile2.addFile(new File(luaState5), zipParameters);
                }
            } else if (luaState2.equals("uncompress")) {
                String luaState7 = luaState.toString(2);
                String luaState8 = luaState.toString(3);
                if (luaState.isString(4)) {
                    zipFile = new ZipFile(luaState7, luaState.toString(4).toCharArray());
                } else {
                    zipFile = new ZipFile(luaState7);
                }
                zipFile.extractAll(luaState8);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        luaState.pushBoolean(true);
        return 1;
    }
}
