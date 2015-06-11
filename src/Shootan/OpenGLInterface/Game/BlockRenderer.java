package Shootan.OpenGLInterface.Game;

import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Graphics.VertexArray;
import Shootan.OpenGLInterface.Math.Matrix4f;


public class BlockRenderer {

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

	private static int[] indices = new int[]{
			0,1,2,
			2,3,0
	};

	private static VertexArray VAO;

	static {
		VAO = new VertexArray(vertices, indices, texCoords);
	}

	private Matrix4f position;
	private Texture texture;

	public BlockRenderer(Texture texture, int x, int y) {
		this.texture=texture;
		position=Matrix4f.translate(x, y, 0);
	}

	public void renderWithoutBinding(){
		Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, position);
		VAO.render();
	}

	public void render(){
		texture.bind();
		Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, position);
		VAO.render();
		texture.unbind();
	}
	
}
