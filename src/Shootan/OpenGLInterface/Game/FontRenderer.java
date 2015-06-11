package Shootan.OpenGLInterface.Game;

import Shootan.Benchmark;
import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Graphics.VertexArray;
import Shootan.OpenGLInterface.Math.Matrix4f;
import Shootan.OpenGLInterface.Math.Vector3f;


public class FontRenderer {


	private static final String[] font=new String[]{"ABCDEFGHIJKLMNOP",
			"QRSTUVWXYZabcdef",
			"ghijklmnopqrstuv",
			"wxyzАБВГДЕЁЖЗИЙК",
			"ЛМНОПРСТУФХЦЧШЩЪ",
			"ЫЬЭЮЯабвгдеёжзий",
			"клмнопрстуфхцчшщ",
			"ъыьэюя0123456789",
			"!@\"#№$;%:^?&**()",
			"-_=+[]{};:'\"\\|,<",
			".>/?`~"};

	private static int[] indices = new int[]{
			0,1,2,
			2,3,0
	};

	private static String texPath = "content/font.png";
	private static Texture texture = new Texture(texPath);

	private VertexArray[] VAO;
	private float spacing;

	public FontRenderer(float size, float spacing){
		this.spacing = spacing;

		float[] vertices = new float[]{
				0, 0, 1f,
				0,  size,  1f,
				size,  size, 1f,
				size, 0, 1f
		};

		VAO=new VertexArray[256*256];
		for (int y=0; y<font.length; y++) {

			float sy=y/16f;

			for (int x=0; x<font[y].length(); x++) {

				float sx=x/16f;

				float[] texCoords = new float[]{
						sx, sy+1/16f,
						sx, sy,
						sx+1/16f, sy,
						sx+1/16f, sy+1/16f
				};
				VAO[font[y].charAt(x)] = new VertexArray(vertices, indices, texCoords);


			}

		}
	}

	public void render(float x, float y, char c) {

		if (VAO[c]!=null) {

			Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId,
					Matrix4f.translate(x, y, 0));

			VAO[c].render();
		}
	}

	private Benchmark fontdraw=new Benchmark("Draw fonts", false);

	public void render(String s, float x, float y) {
		fontdraw.tick();
		texture.bind();
		for (int i=0; i<s.length(); i++) {
			if (s.charAt(i)!=' ') {
				render(x + i*spacing, y, s.charAt(i));
			}
		}
		texture.unbind();
		fontdraw.tack();
	}

	public Vector3f position = new Vector3f();
}
