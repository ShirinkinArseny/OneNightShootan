package Shootan.UI.OpenGLInterface.Graphics;

import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.UI.OpenGLInterface.Math.Vector3f;
import Shootan.UI.OpenGLInterface.Util.ShaderUtils;
import org.lwjgl.Sys;

import static Shootan.UI.OpenGLInterface.Util.Utils.checkForGLError;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	public static final int VERTEX_ATTRIB = 0;
	public static final int TEXTURE_COORDS_ATTRIB = 1;

    private int ID;

    public static final Shader defaultShader =
			new Shader("content/static.vert", "content/static.frag")
					.bindFirstTexture()
			;

	public static final Shader rollShadows =
			new Shader("content/lightmap/rollShadows.vert", "content/lightmap/rollShadows.frag")
					.bindFirstTexture()
			;

	public static final Shader angleShadows =
			new Shader("content/lightmap/angleShadows.vert", "content/lightmap/angleShadows.frag")
					.bindFirstTexture()
			;

	public static final Shader unrollShadows =
			new Shader("content/lightmap/unrollShadows.vert", "content/lightmap/unrollShadows.frag")
					.bindFirstTexture()
			;

	public static final Shader vblur =
			new Shader("content/blur/vblur.vert", "content/blur/blur.frag")
					.bindFirstTexture()
			;


	public static final Shader hblur =
			new Shader("content/blur/hblur.vert", "content/blur/blur.frag")
					.bindFirstTexture()
			;

	public static final Shader multiplyColors =
			new Shader("content/multiplyColors.vert", "content/multiplyColors.frag")
					.bindFirstTexture()
					.bindSecondTexture()
			;

	public static final Shader normalMapping =
			new Shader("content/normalmapping.vert", "content/normalmapping.frag")
					.bindFirstTexture()
					.bindSecondTexture()
			;

	private static Shader currentShader=null;

	public final int modelMatrixUniformId;
	public final int viewMatrixUniformId;
	public final int projectionMatrixUniformId;

	public Shader(String vertex, String fragment){
		ID = ShaderUtils.loadShader(vertex, fragment);
		checkForGLError();
		System.out.println("Shader "+vertex+"+"+fragment+" binded to id "+ID);

		modelMatrixUniformId =getUniform("ml_matrix");
		viewMatrixUniformId =getUniform("vw_matrix");
		projectionMatrixUniformId =getUniform("pr_matrix");
	}

	public static Shader getCurrentShader() {
		return currentShader;
	}


	public Shader bindFirstTexture() {
		enable();
		setUniform1i(getUniform("tex"), 0);
		disable();
		return this;
	}

	public Shader bindSecondTexture() {
		enable();
		setUniform1i(getUniform("tex2"), 1);
		disable();
		return this;
	}

	public int getUniform(String name){
		int result = glGetUniformLocation(ID, name);
		if(result == -1) {
			new Exception("Could not find uniform variable'" + name + "'!\n" +
					"Shader id: "+ID).printStackTrace();
			System.exit(1);
			return -1;
		}

		System.out.println("Shader "+ID+" registered uniform '"+name+"' as "+result);
		return result;
	}

	public void setUniform1i(int id, int value) {
		forceEnable();
		glUniform1i(id, value);
	}

	public void setUniform1f(int id, float value) {
		forceEnable();
		glUniform1f(id, value);
	}

	public void setUniform3f(int id, Vector3f vec) {
		forceEnable();
		glUniform3f(id, vec.x, vec.y, vec.z);
		checkForGLError();
	}

	public void setUniformMat4f(int id, Matrix4f matrix) {
		forceEnable();
		glUniformMatrix4fv(id, false, matrix.toFloatBuffer());
	}

	public void forceEnable() {
		if (currentShader==null || currentShader.ID!=ID) {
			new Exception("Force enabling shader!").printStackTrace();
			enable();
		}
	}

	public void enable() {
		glUseProgram(ID);
		currentShader=this;
	}
	
	public void disable() {
		glUseProgram(0);
		currentShader=null;
	}

}