package Shootan.OpenGLInterface.Game;

import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Graphics.VertexArray;
import Shootan.OpenGLInterface.Math.Matrix4f;


public class HumanRenderer extends GameObject{

	public static float width = 1f;
	public static float height = 1f;

	private static float[] vertices = new float[]{
			-width/2, -height/2, 0.2f,
			-width/2,  height/2, 0.2f,
			width/2,  height/2, 0.2f,
			width/2, -height/2, 0.2f
	};
	
	private static float[] texCoords = new float[]{
			0, 1,
			0, 0,
			1, 0,
			1, 1
	};
	
	private static byte[] indices = new byte[]{
			0,1,2,
			2,3,0
	};
	
	private static String texPath = "content/human.png";
	private static Texture texture = new Texture(texPath);

	private static VertexArray VAO;

	static {
		VAO = new VertexArray(vertices, indices, texCoords);
	}

	public HumanRenderer(){
		super(VAO, texture);
	}

	public void bind() {
		texture.bind();
	}

	public void unbind() {
		texture.unbind();
	}


	public void render(float x, float y, float angle) {

		//Shader.defaultShader.setUniformMat4f("ml_matrix", Matrix4f.translate(x, y, 0).multiply(Matrix4f.rotate(angle)));

		Shader.rotableShader.setUniformMat4f("mov_matrix", Matrix4f.translate(x, y, 0));
		Shader.rotableShader.setUniformMat4f("rot_matrix", Matrix4f.rotate(angle));

		//Shader.rotableShader.setUniformMat4f("ml_matrix", Matrix4f.rotate(angle).multiply(Matrix4f.translate(x, y, 0)));
		VAO.render();

		//Shader.rotableShader.setUniformMat4f("ml_matrix", Matrix4f.translate(x, y, 0).multiply(Matrix4f.rotate(angle)));

	}


	public void setPosition(float x, float y) {
		super.setPosition(x *width, y*height);
	}
}
