package com.ganin.cbuilder.util;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

//
public class GifDecoder {
    private static final int DISPOSAL_BACKGROUND = 2;
    private static final int DISPOSAL_NONE = 1;
    private static final int DISPOSAL_PREVIOUS = 3;
    private static final int DISPOSAL_UNSPECIFIED = 0;
    protected static final int MAX_STACK_SIZE = 4096;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    private static final String TAG = "GifDecoder";
    protected int[] act;
    protected int bgColor;
    protected int bgIndex;
    protected int[] copyScratch;
    protected GifFrame currentFrame;
    protected Bitmap currentImage;
    protected int frameCount;
    protected int framePointer;
    protected ArrayList<GifFrame> frames;
    protected int[] gct;
    protected boolean gctFlag;
    protected int gctSize;
    protected int height;
    protected boolean lctFlag;
    protected int lctSize;
    protected byte[] mainPixels;
    protected int[] mainScratch;
    protected int pixelAspect;
    protected byte[] pixelStack;
    protected short[] prefix;
    protected Bitmap previousImage;
    protected ByteBuffer rawData;
    protected Bitmap renderImage;
    protected int status;
    protected byte[] suffix;
    protected int width;
    protected int loopCount = 1;
    protected byte[] block = new byte[256];
    protected int blockSize = 0;

    private static class GifFrame {
        public int bufferFrameStart;
        public int delay;
        public int dispose;
        public int ih;
        public boolean interlace;
        public int iw;
        public int ix;
        public int iy;
        public int[] lct;
        public int transIndex;
        public boolean transparency;

        private GifFrame() {
        }
    }

    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.frameCount;
    }

    public int getDelay(int i) {
        if (i < 0 || i >= this.frameCount) {
            return -1;
        }
        return this.frames.get(i).delay;
    }

    public int getNextDelay() {
        int i;
        if (this.frameCount <= 0 || (i = this.framePointer) < 0) {
            return -1;
        }
        return getDelay(i);
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    public Bitmap getNextFrame() {
        int i;
        if (this.frameCount <= 0 || (i = this.framePointer) < 0 || this.currentImage == null) {
            return null;
        }
        GifFrame gifFrame = this.frames.get(i);
        int i2 = 0;
        if (gifFrame.lct == null) {
            this.act = this.gct;
        } else {
            this.act = gifFrame.lct;
            if (this.bgIndex == gifFrame.transIndex) {
                this.bgColor = 0;
            }
        }
        if (gifFrame.transparency) {
            int i3 = this.act[gifFrame.transIndex];
            this.act[gifFrame.transIndex] = 0;
            i2 = i3;
        }
        if (this.act == null) {
            Log.w(TAG, "No Valid Color Table");
            this.status = 1;
            return null;
        }
        setPixels(this.framePointer);
        if (gifFrame.transparency) {
            this.act[gifFrame.transIndex] = i2;
        }
        return this.currentImage;
    }

    public int read(InputStream inputStream, int i) {
        System.currentTimeMillis();
        if (inputStream != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i > 0 ? 4096 + i : 4096);
                byte[] bArr = new byte[16384];
                while (true) {
                    int read = inputStream.read(bArr, 0, 16384);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byteArrayOutputStream.flush();
                read(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                Log.w(TAG, "Error reading data from stream", e);
            }
        } else {
            this.status = 2;
        }
        try {
            inputStream.close();
        } catch (Exception e2) {
            Log.w(TAG, "Error closing stream", e2);
        }
        return this.status;
    }

    public int read(byte[] bArr) {
        init();
        if (bArr != null) {
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            this.rawData = wrap;
            wrap.rewind();
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        return this.status;
    }

    protected void setPixels(int i) {
        int i2;
        Bitmap bitmap;
        Bitmap bitmap2;
        GifFrame gifFrame = this.frames.get(i);
        int i3 = i - 1;
        GifFrame gifFrame2 = i3 >= 0 ? this.frames.get(i3) : null;
        int[] iArr = this.mainScratch;
        int i4 = 0;
        if (gifFrame2 != null && gifFrame2.dispose > 0) {
            if (gifFrame2.dispose == 1 && (bitmap2 = this.currentImage) != null) {
                int i5 = this.width;
                bitmap2.getPixels(iArr, 0, i5, 0, 0, i5, this.height);
            }
            if (gifFrame2.dispose == 2) {
                int i6 = !gifFrame.transparency ? this.bgColor : 0;
                for (int i7 = 0; i7 < gifFrame2.ih; i7++) {
                    int i8 = ((gifFrame2.iy + i7) * this.width) + gifFrame2.ix;
                    int i9 = gifFrame2.iw + i8;
                    while (i8 < i9) {
                        iArr[i8] = i6;
                        i8++;
                    }
                }
            }
            if (gifFrame2.dispose == 3 && (bitmap = this.previousImage) != null) {
                int i10 = this.width;
                bitmap.getPixels(iArr, 0, i10, 0, 0, i10, this.height);
            }
        }
        decodeBitmapData(gifFrame, this.mainPixels);
        int i11 = 8;
        int i12 = 0;
        int i13 = 1;
        while (i4 < gifFrame.ih) {
            if (gifFrame.interlace) {
                if (i12 >= gifFrame.ih) {
                    i13++;
                    if (i13 == 2) {
                        i12 = 4;
                    } else if (i13 == 3) {
                        i11 = 4;
                        i12 = 2;
                    } else if (i13 == 4) {
                        i11 = 2;
                        i12 = 1;
                    }
                }
                i2 = i12 + i11;
            } else {
                i2 = i12;
                i12 = i4;
            }
            int i14 = i12 + gifFrame.iy;
            if (i14 < this.height) {
                int i15 = i14 * this.width;
                int i16 = gifFrame.ix + i15;
                int i17 = gifFrame.iw + i16;
                int i18 = this.width;
                if (i15 + i18 < i17) {
                    i17 = i15 + i18;
                }
                int i19 = gifFrame.iw * i4;
                while (i16 < i17) {
                    int i20 = i19 + 1;
                    int i21 = this.act[this.mainPixels[i19] & 255];
                    if (i21 != 0) {
                        iArr[i16] = i21;
                    }
                    i16++;
                    i19 = i20;
                }
            }
            i4++;
            i12 = i2;
        }
        Bitmap bitmap3 = this.currentImage;
        int[] iArr2 = this.copyScratch;
        int i22 = this.width;
        bitmap3.getPixels(iArr2, 0, i22, 0, 0, i22, this.height);
        Bitmap bitmap4 = this.previousImage;
        int[] iArr3 = this.copyScratch;
        int i23 = this.width;
        bitmap4.setPixels(iArr3, 0, i23, 0, 0, i23, this.height);
        Bitmap bitmap5 = this.currentImage;
        int i24 = this.width;
        bitmap5.setPixels(iArr, 0, i24, 0, 0, i24, this.height);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v23, types: [short] */
    /* JADX WARN: Type inference failed for: r3v25 */
    protected void decodeBitmapData(GifFrame gifFrame, byte[] bArr) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        short s;
        byte[] bArr2 = bArr;
        System.currentTimeMillis();
        if (gifFrame != null) {
            this.rawData.position(gifFrame.bufferFrameStart);
        }
        int i6 = gifFrame == null ? this.width * this.height : gifFrame.ih * gifFrame.iw;
        if (bArr2 == null || bArr2.length < i6) {
            bArr2 = new byte[i6];
        }
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }
        int read = read();
        int i7 = 1 << read;
        int i8 = i7 + 1;
        int i9 = i7 + 2;
        int i10 = read + 1;
        int i11 = (1 << i10) - 1;
        for (int i12 = 0; i12 < i7; i12++) {
            this.prefix[i12] = 0;
            this.suffix[i12] = (byte) i12;
        }
        int i13 = i10;
        int i14 = i9;
        int i15 = i11;
        int i16 = -1;
        int i17 = 0;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        while (i17 < i6) {
            if (i18 != 0) {
                i = i10;
                i2 = i8;
                int i25 = i23;
                i3 = i7;
                i4 = i25;
            } else if (i19 >= i13) {
                int i26 = i20 & i15;
                i20 >>= i13;
                i19 -= i13;
                if (i26 > i14 || i26 == i8) {
                    break;
                }
                if (i26 == i7) {
                    i13 = i10;
                    i14 = i9;
                    i15 = i11;
                    i16 = -1;
                } else if (i16 == -1) {
                    this.pixelStack[i18] = this.suffix[i26];
                    i16 = i26;
                    i23 = i16;
                    i18++;
                    i10 = i10;
                } else {
                    i = i10;
                    if (i26 == i14) {
                        i5 = i26;
                        this.pixelStack[i18] = (byte) i23;
                        s = i16;
                        i18++;
                    } else {
                        i5 = i26;
                        s = i5;
                    }
                    while (s > i7) {
                        this.pixelStack[i18] = this.suffix[s];
                        s = this.prefix[s];
                        i18++;
                        i7 = i7;
                    }
                    i3 = i7;
                    byte[] bArr3 = this.suffix;
                    i4 = bArr3[s] & 255;
                    if (i14 >= 4096) {
                        break;
                    }
                    int i27 = i18 + 1;
                    i2 = i8;
                    byte b = (byte) i4;
                    this.pixelStack[i18] = b;
                    this.prefix[i14] = (short) i16;
                    bArr3[i14] = b;
                    i14++;
                    if ((i14 & i15) == 0 && i14 < 4096) {
                        i13++;
                        i15 += i14;
                    }
                    i18 = i27;
                    i16 = i5;
                }
            } else {
                if (i21 == 0) {
                    i21 = readBlock();
                    if (i21 <= 0) {
                        break;
                    } else {
                        i22 = 0;
                    }
                }
                i20 += (this.block[i22] & 255) << i19;
                i19 += 8;
                i22++;
                i21--;
            }
            i18--;
            bArr2[i24] = this.pixelStack[i18];
            i17++;
            i24++;
            i7 = i3;
            i8 = i2;
            i23 = i4;
            i10 = i;
        }
        for (int i28 = i24; i28 < i6; i28++) {
            bArr2[i28] = 0;
        }
    }

    protected boolean err() {
        return this.status != 0;
    }

    protected void init() {
        this.status = 0;
        this.frameCount = 0;
        this.framePointer = -1;
        this.frames = new ArrayList<>();
        this.gct = null;
    }

    protected int read() {
        try {
            return this.rawData.get() & 255;
        } catch (Exception unused) {
            this.status = 1;
            return 0;
        }
    }

    protected int readBlock() {
        int read = read();
        this.blockSize = read;
        int i = 0;
        if (read > 0) {
            while (true) {
                try {
                    int i2 = this.blockSize;
                    if (i >= i2) {
                        break;
                    }
                    int i3 = i2 - i;
                    this.rawData.get(this.block, i, i3);
                    i += i3;
                } catch (Exception e) {
                    Log.w(TAG, "Error Reading Block", e);
                    this.status = 1;
                }
            }
        }
        return i;
    }

    protected int[] readColorTable(int i) {
        byte[] bArr = new byte[i * 3];
        int[] iArr = null;
        try {
            this.rawData.get(bArr);
            iArr = new int[256];
            int i2 = 0;
            int i3 = 0;
            while (i2 < i) {
                int i4 = i3 + 1;
                int i5 = i4 + 1;
                int i6 = i5 + 1;
                int i7 = i2 + 1;
                iArr[i2] = ((bArr[i3] & 255) << 16) | (-16777216) | ((bArr[i4] & 255) << 8) | (bArr[i5] & 255);
                i3 = i6;
                i2 = i7;
            }
        } catch (BufferUnderflowException e) {
            Log.w(TAG, "Format Error Reading Color Table", e);
            this.status = 1;
        }
        return iArr;
    }

    protected void readContents() {
        boolean z = false;
        while (!z && !err()) {
            int read = read();
            if (read == 33) {
                int read2 = read();
                if (read2 == 1) {
                    skip();
                } else if (read2 == 249) {
                    this.currentFrame = new GifFrame();
                    readGraphicControlExt();
                } else if (read2 == 254) {
                    skip();
                } else if (read2 == 255) {
                    readBlock();
                    String str = "";
                    for (int i = 0; i < 11; i++) {
                        str = str + ((char) this.block[i]);
                    }
                    if (str.equals("NETSCAPE2.0")) {
                        readNetscapeExt();
                    } else {
                        skip();
                    }
                } else {
                    skip();
                }
            } else if (read == 44) {
                readBitmap();
            } else if (read != 59) {
                this.status = 1;
            } else {
                z = true;
            }
        }
    }

    protected void readHeader() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            str = str + ((char) read());
        }
        if (!str.startsWith("GIF")) {
            this.status = 1;
            return;
        }
        readLSD();
        if (!this.gctFlag || err()) {
            return;
        }
        int[] readColorTable = readColorTable(this.gctSize);
        this.gct = readColorTable;
        this.bgColor = readColorTable[this.bgIndex];
    }

    protected void readGraphicControlExt() {
        read();
        int read = read();
        this.currentFrame.dispose = (read & 28) >> 2;
        if (this.currentFrame.dispose == 0) {
            this.currentFrame.dispose = 1;
        }
        this.currentFrame.transparency = (read & 1) != 0;
        this.currentFrame.delay = readShort() * 10;
        this.currentFrame.transIndex = read();
        read();
    }

    protected void readBitmap() {
        this.currentFrame.ix = readShort();
        this.currentFrame.iy = readShort();
        this.currentFrame.iw = readShort();
        this.currentFrame.ih = readShort();
        int read = read();
        this.lctFlag = (read & 128) != 0;
        this.lctSize = (int) Math.pow(2.0d, (read & 7) + 1);
        this.currentFrame.interlace = (read & 64) != 0;
        if (this.lctFlag) {
            this.currentFrame.lct = readColorTable(this.lctSize);
        } else {
            this.currentFrame.lct = null;
        }
        this.currentFrame.bufferFrameStart = this.rawData.position();
        decodeBitmapData(null, this.mainPixels);
        skip();
        if (err()) {
            return;
        }
        this.frameCount++;
        this.frames.add(this.currentFrame);
    }

    protected void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int read = read();
        this.gctFlag = (read & 128) != 0;
        this.gctSize = 2 << (read & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
        int i = this.width;
        int i2 = this.height;
        this.mainPixels = new byte[i * i2];
        this.mainScratch = new int[i * i2];
        this.copyScratch = new int[i * i2];
        this.previousImage = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
        this.currentImage = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565);
    }

    protected void readNetscapeExt() {
        do {
            readBlock();
            byte[] bArr = this.block;
            if (bArr[0] == 1) {
                this.loopCount = ((bArr[2] & 255) << 8) | (bArr[1] & 255);
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    protected int readShort() {
        return this.rawData.getShort();
    }

    protected void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
