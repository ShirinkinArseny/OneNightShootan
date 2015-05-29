package Shootan.OpenGLInterface.Util;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
// the GL20 library allows us to use most
// opengl shader things.


public class ShaderUtils {

	public static int loadShader(String vertPath, String fragPath){
		String vert = Utils.loadFileAsString(vertPath);
		String frag = Utils.loadFileAsString(fragPath);
		return createShader(vert, frag);
	}
	
	public static int createShader(String vert, String frag){
		int program = glCreateProgram();
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);

		glCompileShader(vertID);
		if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE){
			new Exception("Failed to compile vertex shader: "+glGetShaderInfoLog(vertID)).printStackTrace();
			System.exit(1);
		}

		glCompileShader(fragID);
		if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE){
			new Exception("Failed to compile fragment shader: "+glGetShaderInfoLog(fragID)).printStackTrace();
			System.exit(1);
		}

		glAttachShader(program, vertID);
		glAttachShader(program, fragID);

		glLinkProgram(program);

		glValidateProgram(program);

		return program;
	}
	
}
