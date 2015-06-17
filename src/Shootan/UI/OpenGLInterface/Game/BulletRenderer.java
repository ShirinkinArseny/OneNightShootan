package Shootan.UI.OpenGLInterface.Game;

import Shootan.GameEssences.Bullets.Bullet;
import Shootan.GameEssences.Bullets.Flame;
import Shootan.GameEssences.Bullets.SmallFlame;
import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Graphics.Texture;
import Shootan.UI.OpenGLInterface.Graphics.VertexArray;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.UI.OpenGLInterface.Math.Vector3f;


public class BulletRenderer {

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

	private static String texPath = "content/bullet.png";
	private static String texPathFlame = "content/flame.png";
	private static Texture texture = new Texture(texPath);
	private static Texture textureFlame = new Texture(texPathFlame);

	private static VertexArray VAO;

	static {
		VAO = new VertexArray(vertices, indices, texCoords);
	}

	public BulletRenderer(){
	}

	public void render(Bullet bullet) {

		boolean isFlame=bullet.getType()== Flame.type || bullet.getType()== SmallFlame.type;

		if (isFlame) {
			textureFlame.bind();
		} else {
			texture.bind();
		}


			Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId,
					Matrix4f.translate(bullet.getX(), bullet.getY(), 0).multiply(Matrix4f.getRotated(bullet.getAngle())));

		VAO.render();


		if (isFlame) {
			textureFlame.unbind();
		} else {
			texture.unbind();
		}

	}

	public Vector3f position = new Vector3f();
}
