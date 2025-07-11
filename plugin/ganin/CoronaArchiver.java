package plugin.ganin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//
public class CoronaArchiver {
    private String _input_dir;
    private File[] _input_files;
    private RandomAccessFile stream;
    private final byte[] _MAGIC_NUMBER_HEADER = {114, 97, 99, 1};
    private final byte[] _MAGIC_NUMBER_END = {-1, -1, -1, -1};
    private final int _MAGIC_NUMBER_INDEX = 1;
    private Map<String, Integer> metadata = new HashMap();
    private Map<String, Integer> indexes = new HashMap();

    public void pack(String str, String str2) {
        String str3;
        this._input_dir = str;
        try {
            this.stream = new RandomAccessFile(str2, "rw");
            File[] listFiles = new File(str).listFiles();
            this._input_files = listFiles;
            this.metadata.put("length", Integer.valueOf(listFiles.length));
            this.stream.write(this._MAGIC_NUMBER_HEADER);
            this.stream.write(Struct.pack("i", 1));
            this.stream.write(Struct.pack("i", 0));
            this.stream.write(Struct.pack("i", this.metadata.get("length")));
            File[] fileArr = this._input_files;
            int length = fileArr.length;
            int i = 0;
            while (true) {
                str3 = "iii";
                if (i >= length) {
                    break;
                }
                File file = fileArr[i];
                int _padding_length = _padding_length(file.getName().length(), "index");
                this.stream.write(Struct.pack("iii", 1, 488, Integer.valueOf(file.getName().length())));
                this.stream.write(file.getName().getBytes(StandardCharsets.UTF_8));
                _write_padding(_padding_length);
                i++;
            }
            long filePointer = this.stream.getFilePointer();
            long filePointer2 = this.stream.getFilePointer() - 12;
            this.stream.seek(8L);
            this.stream.write(Struct.pack("i", Integer.valueOf((int) filePointer2)));
            this.stream.seek(filePointer);
            for (File file2 : this._input_files) {
                FileInputStream fileInputStream = new FileInputStream(file2);
                int length2 = (int) file2.length();
                int _padding_length2 = _padding_length(length2, "data");
                byte[] bArr = new byte[length2];
                fileInputStream.read(bArr);
                this.indexes.put(file2.getName(), Integer.valueOf((int) this.stream.getFilePointer()));
                str3 = str3;
                this.stream.write(Struct.pack(str3, 2, Integer.valueOf(length2 + 4 + _padding_length2), Integer.valueOf(length2)));
                this.stream.write(bArr);
                _write_padding(_padding_length2);
            }
            this.stream.write(this._MAGIC_NUMBER_END);
            this.stream.write(Struct.pack("i", 0));
            _write_finalize();
            this.stream.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void _write_finalize() throws IOException {
        this.stream.seek(16L);
        for (File file : this._input_files) {
            int _padding_length = _padding_length(file.getName().length(), "index");
            this.stream.readInt();
            RandomAccessFile randomAccessFile = this.stream;
            randomAccessFile.seek(randomAccessFile.getFilePointer());
            this.stream.write(Struct.pack("i", this.indexes.get(file.getName())));
            RandomAccessFile randomAccessFile2 = this.stream;
            randomAccessFile2.seek(randomAccessFile2.getFilePointer());
            this.stream.read(new byte[this.stream.readInt() + _padding_length]);
        }
    }

    private void _write_padding(int i) throws IOException {
        for (int i2 = 0; i2 < i; i2++) {
            this.stream.write(0);
        }
    }

    private int _padding_length(int i, String str) {
        int i2 = ((4 - (i % 4)) + i) - i;
        if (!str.equals("data") || i2 < 4) {
            return i2;
        }
        return 0;
    }
}
