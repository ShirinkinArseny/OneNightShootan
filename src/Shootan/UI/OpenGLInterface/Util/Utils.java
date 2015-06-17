package Shootan.UI.OpenGLInterface.Util;

import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class Utils {

    public static void processError(Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    public static void processError(String s) {
        processError(new Exception(s));
    }

    public static void checkForGLError() {
        int errCode = glGetError();
        if (errCode != GL_NO_ERROR) {
            String errString = "Unknown error code: " + errCode;

            switch (errCode) {
                case GL11.GL_INVALID_ENUM:
                    errString = "GL_INVALID_ENUM";
                    break;
                case GL11.GL_INVALID_VALUE:
                    errString = "GL_INVALID_VALUE";
                    break;
                case GL11.GL_INVALID_OPERATION:
                    errString = "GL_INVALID_OPERATION";
                    break;
                case GL11.GL_STACK_OVERFLOW:
                    errString = "GL_STACK_OVERFLOW";
                    break;
                case GL11.GL_STACK_UNDERFLOW:
                    errString = "GL_STACK_UNDERFLOW";
                    break;
                case GL11.GL_OUT_OF_MEMORY:
                    errString = "GL_OUT_OF_MEMORY";
                    break;
            }

            processError("OpenGL error occurred: " + errString);
        }
    }

    public static String loadFileAsString(String location) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(location));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer);
                result.append("\n");
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static ByteBuffer createByteBuffer(byte[] array) {
        ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
        result.put(array).flip();
        return result;
    }

    public static FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(array).flip();
        return result;
    }

    public static IntBuffer createIntBuffer(int[] array) {
        IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(array).flip();
        return result;
    }

}
