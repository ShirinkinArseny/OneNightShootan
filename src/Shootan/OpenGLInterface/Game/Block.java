package Shootan.OpenGLInterface.Game;

import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Graphics.VertexArray;
import Shootan.OpenGLInterface.Math.Matrix4f;


public class Block {

	public static float width = 1f;
	public static float height = 1f;



	private static float[] vertices = new float[]{
			0, 0, 0.1f,
			0,  height, 0.1f,
			  width,  height, 0.1f,
			  width, 0, 0.1f
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

	private static VertexArray VAO;

	static {
		VAO = new VertexArray(vertices, indices, texCoords);
	}

	private Matrix4f position;
	private Texture texture;

	public Block(Texture texture, int x, int y) {
		this.texture=texture;
		position=Matrix4f.translate(x, y, 0);
	}

	public void renderShadowed(){
		texture.bind();
		Shader.darkableShader.setUniformMat4f("ml_matrix", position);
		VAO.render();
		texture.unbind();
	}

	public void render(){
		texture.bind();
		Shader.defaultShader.setUniformMat4f("ml_matrix", position);
		VAO.render();
		texture.unbind();
	}

	
}
