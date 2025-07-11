package plugin.ganin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.ganin.cbuilder.util.GifDecoder;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//
public class CConvert implements NamedJavaFunction {
    public String getName() {
        return "convert";
    }

    public int invoke(LuaState luaState) {
        int i;
        int i2;
        int i3;
        String luaState2 = luaState.toString(1);
        String luaState3 = luaState.toString(2);
        Bitmap bitmap = getBitmap(luaState2);
        int i4 = 0;
        try {
            i3 = bitmap.getWidth();
            try {
                i2 = bitmap.getHeight();
                try {
                    FileInputStream fileInputStream = new FileInputStream(luaState2);
                    byte[] bArr = new byte[fileInputStream.available()];
                    fileInputStream.read(bArr);
                    GifDecoder gifDecoder = new GifDecoder();
                    gifDecoder.read(bArr);
                    i = gifDecoder.getFrameCount();
                    try {
                        Bitmap[] bitmapArr = new Bitmap[i];
                        for (int i5 = 0; i5 < i; i5++) {
                            gifDecoder.advance();
                            bitmapArr[i5] = Bitmap.createBitmap(gifDecoder.getNextFrame());
                        }
                        Bitmap createBitmap = Bitmap.createBitmap(i3 * i, i2, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(createBitmap);
                        Paint paint = new Paint();
                        while (i4 < i) {
                            canvas.drawBitmap(bitmapArr[i4], r9.getWidth() * i4, 0.0f, paint);
                            i4++;
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(luaState3);
                        createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e = e;
                        i4 = i3;
                        e.printStackTrace();
                        i3 = i4;
                        luaState.pushInteger(i3);
                        luaState.pushInteger(i2);
                        luaState.pushInteger(i);
                        return 3;
                    }
                } catch (Exception e2) {
                    e = e2;
                    i4 = i3;
                    i = 0;
                }
            } catch (Exception e3) {
                e = e3;
                i4 = i3;
                i = 0;
                i2 = 0;
                e.printStackTrace();
                i3 = i4;
                luaState.pushInteger(i3);
                luaState.pushInteger(i2);
                luaState.pushInteger(i);
                return 3;
            }
        } catch (Exception e4) {
            e = e4;
        }
        luaState.pushInteger(i3);
        luaState.pushInteger(i2);
        luaState.pushInteger(i);
        return 3;
    }

    public Bitmap getBitmap(String str) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileInputStream = null;
        }
        return BitmapFactory.decodeStream(fileInputStream);
    }
}
