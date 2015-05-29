package Shootan.OpenGLInterface.Input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input extends GLFWKeyCallback {

	public static boolean[] keys = new boolean[65535];
	public static boolean[] pKeys = new boolean[65535];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key>=0 && key<65535) {
			keys[key] = action != GLFW_RELEASE;
			if (action == GLFW_RELEASE) {
				pKeys[key] = true;
			}
		}
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static boolean isKeyUp(int keycode){
		return pKeys[keycode];
	}
	
	
}
