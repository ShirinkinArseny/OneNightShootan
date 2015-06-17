package Shootan.UI.OpenGLInterface.Game;

import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Graphics.Texture;
import Shootan.UI.OpenGLInterface.Graphics.VertexArray;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.UI.OpenGLInterface.Math.Vector3f;


public class HumanRenderer {

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
	
	private static int[] indices = new int[]{
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
	}

	public void bind() {
		texture.bind();
	}

	public void unbind() {
		texture.unbind();
	}


	public void render(float x, float y, float angle) {

		Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId,
				Matrix4f.translate(x, y, 0).multiply(Matrix4f.getRotated(angle)));

		VAO.render();
	}

	public Vector3f position = new Vector3f();

}
