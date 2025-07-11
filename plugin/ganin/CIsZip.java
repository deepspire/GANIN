package plugin.ganin;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.jose4j.jwk.RsaJsonWebKey;

//
public class CIsZip implements NamedJavaFunction {
    public String getName() {
        return "isZip";
    }

    public int invoke(LuaState luaState) {
        luaState.pushBoolean(isArchive(new File(luaState.toString(1))));
        return 1;
    }

    private static boolean isArchive(File file) {
        int i;
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
        } catch (IOException unused) {
            i = 0;
        }
        try {
            i = randomAccessFile.readInt();
            try {
                randomAccessFile.close();
            } catch (IOException unused2) {
            }
            return i == 1347093252 || i == 1347093766 || i == 1347094280;
        } catch (Throwable th) {
            try {
                randomAccessFile.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
