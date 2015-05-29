package Shootan.OpenGLInterface.Graphics;

import Shootan.OpenGLInterface.Math.Matrix4f;
import Shootan.OpenGLInterface.Math.Vector3f;
import Shootan.OpenGLInterface.Util.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	public static final int VERTEX_ATTRIB = 0;
	public static final int TEXTURE_COORDS_ATTRIB = 1;
    private int ID;
    private Map<String, Integer> locationCache = new HashMap<>();
    
    private boolean enabled = false;

	public static final Shader darkableShader= new Shader("content/darkable.vert", "content/darkable.frag");
	public static final Shader rotableShader= new Shader("content/rotable.vert", "content/rotable.frag");
    public static final Shader defaultShader= new Shader("content/static.vert", "content/static.frag");
	
	public Shader(String vertex, String fragment){
		ID = ShaderUtils.loadShader(vertex, fragment);
		System.out.println("Shader "+vertex+"+"+fragment+" binded to id "+ID);
	}
	
	public int getUniform(String name){
		if (!enabled) forceEnable();

		if (locationCache.containsKey(name)){
			return locationCache.get(name);
		}
		int result = glGetUniformLocation(ID, name);
		if(result == -1) {
			new Exception("Could not find uniform variable'" + name + "'!").printStackTrace();
			System.exit(1);
		} else
			locationCache.put(name, result);
		return glGetUniformLocation(ID, name);
	}

	public void setUniform1i(String name, int value) {
		if (!enabled) forceEnable();
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		if (!enabled) forceEnable();
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y) {
		if (!enabled) forceEnable();
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		if (!enabled) forceEnable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled) forceEnable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}

	public void forceEnable() {
		new Exception("Force enabling shader!").printStackTrace();
		glUseProgram(ID);
		enabled = true;
	}

	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}
	
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}

}