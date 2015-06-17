package Shootan.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ByteUtils {

    public static String bytesToString(List<Byte> data, IndexWrapper index) {
        int length=twoBytesToUInt(data, index);
        byte[] source=new byte[length];
        for (int i=0; i<length; i++) {
            if (index.value<data.size()) {
                source[i] = data.get(index.value++);
            }
        }
        return new String(source);
    }

    public static ArrayList<Byte> stringToBytes(String s) {
        byte[] b=s.getBytes();
        ArrayList<Byte> result=new ArrayList<>(b.length+2);
        result.addAll(uIntToBytes(b.length));
        for (byte t: b) {
            result.add(t);
        }
        return result;
    }

    @Test
    public void stringToBytesTest() {
        String [] words=("Компания Microsoft показала на Е3 игровые возможности HoloLens, продемонстрировав, как гарнитура дополненной реальности может использоваться для игры в Minecraft.\n" +
                "\n" +
                "Это устройство позволит играть на любой поверхности. В ходе демонстрации пользователь сначала поиграл на стене, а потом переместил виртуальный мир на стол.\n" +
                "\n" +
                "Сколько будет стоить версия Minecraft для HoloLens, пока не сообщается, также как и дата начала продаж. Кроме возможности игры в трехмерном режиме, гарнитура дополненной реальности позволит использовать для управления жесты и голосовые команды.\n" +
                "\n" +
                "HoloLens будет работать с Windows 10, о поддержке других платформ пока не сообщается.").split("а");
        for (String word: words) {
            ArrayList<Byte> ser=stringToBytes(word);

            String des=bytesToString(ser, new IndexWrapper());
            assertEquals(word, des);
        }
    }

    public static ArrayList<Byte> normalisedFloatToBytes(float normalised) {
        if (normalised<0) normalised=0;
        int unnormed= (int) (normalised*65000);
        ArrayList<Byte> res=new ArrayList<>(2);
        res.add((byte) (unnormed/256));
        res.add((byte) (unnormed%256));
        return res;
    }

    public static float twoBytesToNormalisedFloat(List<Byte> data, IndexWrapper index) {
        return twoBytesToNormalisedFloat(data.get(index.value++), data.get(index.value++));
    }


    private static float twoBytesToNormalisedFloat(byte first, byte second) {
        return (float) (twoBytesToUInt(first, second)/65000.0);
    }


    @Test
    public void testTwoBytesToNormalisedFloat() {
        for (float i=0; i<1; i+=0.001) {
            ArrayList<Byte> transformed=normalisedFloatToBytes(i);
            float angle=twoBytesToNormalisedFloat(transformed.get(0), transformed.get(1));
            assertTrue(Math.abs(angle - i) < 0.05);
        }
    }

    public static ArrayList<Byte> coordToBytes(float coord) {
        int intPart= (int) Math.floor(coord);
        ArrayList<Byte> res=new ArrayList<>(2);
        res.addAll(uIntToBytes(intPart));
        res.addAll(normalisedFloatToBytes(coord - intPart));
        return res;
    }

    public static float fourBytesToCoord(List<Byte> data, IndexWrapper index) {
        return fourBytesToCoord(
                data.get(index.value++),
                data.get(index.value++),
                data.get(index.value++),
                data.get(index.value++)
                );
    }

    private static float fourBytesToCoord(byte first, byte second, byte third, byte fourth) {
        return twoBytesToUInt(first, second)+twoBytesToNormalisedFloat(third, fourth);
    }


    @Test
    public void testFourBytesToCoord() {
        for (float i=0; i<50000; i+=0.05) {
            ArrayList<Byte> transformed=coordToBytes(i);
            float coord=fourBytesToCoord(transformed.get(0), transformed.get(1), transformed.get(2), transformed.get(3));
            assertTrue(Math.abs(coord - i) < 0.1);
        }
    }

    public static ArrayList<Byte> angleToBytes(float angle) {
        if (angle<0) angle+=Math.PI*2;
        int viewAngle= (int) (angle*65000/Math.PI/2);
        ArrayList<Byte> res=new ArrayList<>(2);
        res.add((byte) (viewAngle/256));
        res.add((byte) (viewAngle%256));
        return res;
    }

    public static float twoBytesToAngle(List<Byte> data, IndexWrapper index) {
        return twoBytesToAngle(data.get(index.value++), data.get(index.value++));
    }

    private static float twoBytesToAngle(byte first, byte second) {
        return (float) (twoBytesToUInt(first, second)*Math.PI*2/65000.0);
    }


    @Test
    public void testTwoBytesToAngle() {
        for (float i=0; i<Math.PI*2; i+=0.001) {
            ArrayList<Byte> transformed=angleToBytes(i);
            float angle=twoBytesToAngle(transformed.get(0), transformed.get(1));
            assertTrue(Math.abs(angle - i) < 0.05);
        }
    }

    public static boolean byteToBoolean(List<Byte> data, IndexWrapper index) {
        return byteToBoolean(data.get(index.value++));
    }

    private static boolean byteToBoolean(byte b) {
        return b==1;
    }

    public static byte booleanToByte(boolean b) {
        return (byte) (b?1:0);
    }

    @Test
    public void testBooleanToByte() {
        assertTrue(byteToBoolean(booleanToByte(true)));
        assertFalse(byteToBoolean(booleanToByte(false)));
    }

    public static int twoBytesToUInt(List<Byte> data, IndexWrapper index) {
        return twoBytesToUInt(data.get(index.value++), data.get(index.value++));
    }

    private static int twoBytesToUInt(byte first, byte second) {
        return byteToShort(first)*256+ byteToShort(second);
    }

    public static ArrayList<Byte> uIntToBytes(int value) {
        ArrayList<Byte> a=new ArrayList<>(2);
        a.add((byte) (value/256)); a.add((byte) (value%256));
        return a;
    }

    @Test
    public void testUIntToBytes() {
        for (int i=0; i<50000; i++) {
            ArrayList<Byte> transformed=uIntToBytes(i);
            assertEquals(i, twoBytesToUInt(transformed.get(0), transformed.get(1)));
        }
    }

    public static int byteToShort(List<Byte> data, IndexWrapper index) {
        return byteToShort(data.get(index.value++));
    }

    private static int byteToShort(byte b) {
        if (b>=0) return b;
        return b+256;
    }

    @Test
    public void testByteToShort() {
        for (int i=0; i<256; i++) {
            assertEquals(i, byteToShort((byte) i));
        }
    }

    public static String serializeBytes(List<Byte> data) {
        StringBuilder s=new StringBuilder();
        for (Byte b: data) {
            s.append(String.valueOf(b));
            s.append(",");
        }
        s.setLength(s.length()-1);
        return s.toString();
    }

    public static ArrayList<Byte> deserializeBytes(String data) {
        String[] subdata=data.split(",");
        ArrayList<Byte> res=new ArrayList<>(subdata.length);
        for (String s: subdata) {
            res.add(Byte.valueOf(s));
        }
        return res;
    }

    public static String securityHash(String s) {
        return s; //VERY SECURE //TODO: SHA or MD5 or another moar secure stuff
    }

}
