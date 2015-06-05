package Shootan.OpenGLInterface.Graphics;

import Shootan.OpenGLInterface.Math.Matrix4f;
import Shootan.OpenGLInterface.Math.Vector3f;
import Shootan.OpenGLInterface.Util.ShaderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import static Shootan.OpenGLInterface.Util.Utils.checkForGLError;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	public static final int VERTEX_ATTRIB = 0;
	public static final int TEXTURE_COORDS_ATTRIB = 1;


	private static CopyOnWriteArrayList<Shader> allShaders=new CopyOnWriteArrayList<>();

	public static Stream<Shader> getShadersStream() { return allShaders.stream(); }

    private int ID;
    private Map<String, Integer> locationCache = new HashMap<>();

	public static final Shader darkableShader =
			new Shader("content/darkable.vert", "content/darkable.frag")
				.bindFirstTexture()
				.bindPositionToCamera()
			;

    public static final Shader defaultShader =
			new Shader("content/static.vert", "content/static.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;

	public static final Shader darknessShader =
			new Shader("content/darkness.vert", "content/darkness.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;

	public static final Shader rollShadows =
			new Shader("content/lightmap/rollShadows.vert", "content/lightmap/rollShadows.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;

	public static final Shader angleShadows =
			new Shader("content/lightmap/angleShadows.vert", "content/lightmap/angleShadows.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;

	public static final Shader unrollShadows =
			new Shader("content/lightmap/unrollShadows.vert", "content/lightmap/unrollShadows.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;

	public static final Shader vblur =
			new Shader("content/blur/vblur.vert", "content/blur/blur.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;


	public static final Shader hblur =
			new Shader("content/blur/hblur.vert", "content/blur/blur.frag")
					.bindFirstTexture()
					.bindPositionToCamera()
			;

	public static final Shader multiplyColors =
			new Shader("content/multiplyColors.vert", "content/multiplyColors.frag")
					.bindFirstTexture()
					.bindSecondTexture()
					.bindPositionToCamera()
			;

	public static final Shader additiveBlend =
			new Shader("content/additiveBlend.vert", "content/additiveBlend.frag")
					.bindFirstTexture()
					.bindSecondTexture()
					.bindPositionToCamera()
			;

	private static Shader currentShader=null;

	public Shader(String vertex, String fragment){
		ID = ShaderUtils.loadShader(vertex, fragment);
		checkForGLError();
		System.out.println("Shader "+vertex+"+"+fragment+" binded to id "+ID);
	}

	public static Shader getCurrentShader() {
		return currentShader;
	}


	public Shader bindFirstTexture() {
		enable();
		setUniform1i("tex", 0);
		disable();
		return this;
	}

	public Shader bindSecondTexture() {
		enable();
		setUniform1i("tex2", 1);
		disable();
		return this;
	}

	public Shader bindPositionToCamera() {
		allShaders.add(this);
		return this;
	}

	private static long times=0;
	private static long sumNanos=0;

	public int getUniform(String name){
		/*if (times%10000==0) {
			System.out.println("getUniform timing: \n" +
					"	used "+times+" times,\n" +
					"	totally "+sumNanos+" nanoseconds");
		}*/
		times++;
		long localNano=System.nanoTime();

		if (locationCache.containsKey(name)){
			int id=locationCache.get(name);
			sumNanos+=System.nanoTime()-localNano;
			return id;
		}
		int result = glGetUniformLocation(ID, name);
		if(result == -1) {
			new Exception("Could not find uniform variable'" + name + "'!\n" +
					"Shader id: "+ID).printStackTrace();
			System.exit(1);
		} else
			locationCache.put(name, result);


		int id=locationCache.get(name);
		sumNanos+=System.nanoTime()-localNano;
		return id;


	}

	public void setUniform1i(String name, int value) {
		forceEnable();
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		forceEnable();
		glUniform1f(getUniform(name), value);
	}

	public void setUniformMat4f(String name, Matrix4f matrix) {
		forceEnable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
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