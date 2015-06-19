package Shootan.UI.OpenGLInterface.Game;

import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Graphics.VertexArray;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;


public class FBORenderer {


	public static float width = Camera.size;
	public static float height = Camera.size;

	private static float[] vertices = new float[]{
			-width, -height, 0.2f,
			-width,  height, 0.2f,
			width,  height, 0.2f,
			width, -height, 0.2f

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

	public FBORenderer() {
	}

	public void render(Matrix4f position) {
		Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, position);
		VAO.render();
	}

		public void render(float x, float y) {
			Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, Matrix4f.translate(x, y, 0));
		VAO.render();
	}

	
}
