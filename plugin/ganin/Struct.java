package plugin.ganin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//
public class Struct {
    public static byte[] pack(String str, Object... objArr) {
        ByteBuffer allocate = ByteBuffer.allocate(str.length() * 4);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        int i = 0;
        for (char c : str.toCharArray()) {
            if (c == 'b') {
                allocate.put(((Byte) objArr[i]).byteValue());
            } else if (c == 'd') {
                allocate.putDouble(((Double) objArr[i]).doubleValue());
            } else if (c == 'f') {
                allocate.putFloat(((Float) objArr[i]).floatValue());
            } else if (c == 'l') {
                allocate.putLong(((Long) objArr[i]).longValue());
            } else if (c == 'h') {
                allocate.putShort(((Short) objArr[i]).shortValue());
            } else if (c == 'i') {
                allocate.putInt(((Integer) objArr[i]).intValue());
            } else {
                throw new IllegalArgumentException("Invalid format character: " + c);
            }
            i++;
        }
        return allocate.array();
    }
}
