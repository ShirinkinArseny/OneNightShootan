package Shootan.UI.OpenGLInterface.Game;

import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Graphics.Texture;
import Shootan.UI.OpenGLInterface.Graphics.VertexArray;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.UI.OpenGLInterface.Math.Vector3f;
import Shootan.Worlds.StrangeWorld;
import Shootan.Worlds.AbstractWorld;

import java.util.ArrayList;

import static Shootan.UI.OpenGLInterface.Util.Utils.checkForGLError;


public class MapRenderer {

	public static float width = 1f;
	public static float height = 1f;

	private static String texPath = "content/blocks.png";
	private static String texPath2 = "content/blocksNormals.png";
	private static Texture texture = new Texture(texPath);
	private static Texture texture2 = new Texture(texPath2);

	private VertexArray VAO;
	private VertexArray VAOHard;

	private void initFullVAO(AbstractWorld w) {

		int freeBlocks=0;
		for (int x=0; x<StrangeWorld.SIZE; x++) {
			for (int y=0; y<StrangeWorld.SIZE; y++) {
				if (!w.getBlock(x, y).getIsHard())
					freeBlocks++;
			}
		}

		float[] vertices=new float[freeBlocks*12];

		float[] texCoords=new float[freeBlocks*8];

		int[] indices=new int[freeBlocks*6];

		int index=0;

		for (int x=0; x<StrangeWorld.SIZE; x++) {
			for (int y=0; y<StrangeWorld.SIZE; y++) {

				if (!w.getBlock(x, y).getIsHard()) {
					vertices[index * 12 + 0] = x;
					vertices[index * 12 + 1] = y;
					vertices[index * 12 + 2] = 0.1f;
					vertices[index * 12 + 3] = x;
					vertices[index * 12 + 4] = y + 1;
					vertices[index * 12 + 5] = 0.1f;
					vertices[index * 12 + 6] = x + 1;
					vertices[index * 12 + 7] = y + 1;
					vertices[index * 12 + 8] = 0.1f;
					vertices[index * 12 + 9] = x + 1;
					vertices[index * 12 + 10] = y;
					vertices[index * 12 + 11] = 0.1f;

					if (w.getBlock(x, y).getType() == 0) {
						texCoords[index * 8 + 0] = 0f;
						texCoords[index * 8 + 1] = 0.25f;
						texCoords[index * 8 + 2] = 0f;
						texCoords[index * 8 + 3] = 0f;
						texCoords[index * 8 + 4] = 0.25f;
						texCoords[index * 8 + 5] = 0f;
						texCoords[index * 8 + 6] = 0.25f;
						texCoords[index * 8 + 7] = 0.25f;
					} else {
						texCoords[index * 8 + 0] = 0.25f;
						texCoords[index * 8 + 1] = 0.25f;
						texCoords[index * 8 + 2] = 0.25f;
						texCoords[index * 8 + 3] = 0f;
						texCoords[index * 8 + 4] = 0.5f;
						texCoords[index * 8 + 5] = 0f;
						texCoords[index * 8 + 6] = 0.5f;
						texCoords[index * 8 + 7] = 0.25f;
					}

					indices[index * 6 + 0] = index * 4;
					indices[index * 6 + 1] = index * 4 + 1;
					indices[index * 6 + 2] = index * 4 + 2;

					indices[index * 6 + 3] = index * 4 + 2;
					indices[index * 6 + 4] = index * 4 + 3;
					indices[index * 6 + 5] = index * 4;


					index++;
				}
			}
		}

		VAO = new VertexArray(vertices, indices, texCoords);

		System.out.println("MapRenderer runnable vertices: "+freeBlocks*12);
		System.out.println("MapRenderer runnable texCoords: "+freeBlocks*8);
		System.out.println("MapRenderer runnable indices: "+freeBlocks*6);
	}

	private void initHardVAO(AbstractWorld w) {

		int hardBlocks=0;
		for (int x=0; x<StrangeWorld.SIZE; x++) {
			for (int y=0; y<StrangeWorld.SIZE; y++) {
				if (w.getBlock(x, y).getIsHard())
					hardBlocks++;
			}
		}

		float[] vertices=new float[hardBlocks*12];

		float[] texCoords=new float[hardBlocks*8];

		int[] indices=new int[hardBlocks*6];

		int index=0;

		for (int x=0; x<StrangeWorld.SIZE; x++) {
			for (int y=0; y<StrangeWorld.SIZE; y++) {

				if (w.getBlock(x, y).getIsHard()) {
					vertices[index * 12 + 0] = x;
					vertices[index * 12 + 1] = y;
					vertices[index * 12 + 2] = 0.1f;
					vertices[index * 12 + 3] = x;
					vertices[index * 12 + 4] = y + 1;
					vertices[index * 12 + 5] = 0.1f;
					vertices[index * 12 + 6] = x + 1;
					vertices[index * 12 + 7] = y + 1;
					vertices[index * 12 + 8] = 0.1f;
					vertices[index * 12 + 9] = x + 1;
					vertices[index * 12 + 10] = y;
					vertices[index * 12 + 11] = 0.1f;

					texCoords[index * 8 + 0] = 0f;
					texCoords[index * 8 + 1] = 0.25f;
					texCoords[index * 8 + 2] = 0f;
					texCoords[index * 8 + 3] = 0f;
					texCoords[index * 8 + 4] = 0.25f;
					texCoords[index * 8 + 5] = 0f;
					texCoords[index * 8 + 6] = 0.25f;
					texCoords[index * 8 + 7] = 0.25f;

					indices[index * 6 + 0] = index * 4;
					indices[index * 6 + 1] = index * 4 + 1;
					indices[index * 6 + 2] = index * 4 + 2;

					indices[index * 6 + 3] = index * 4 + 2;
					indices[index * 6 + 4] = index * 4 + 3;
					indices[index * 6 + 5] = index * 4;


					index++;
				}
			}
		}

		VAOHard = new VertexArray(vertices, indices, texCoords);

		System.out.println("MapRenderer hard vertices: "+hardBlocks*12);
		System.out.println("MapRenderer hard texCoords: "+hardBlocks*8);
		System.out.println("MapRenderer hard indices: "+hardBlocks*6);
	}


	public MapRenderer(AbstractWorld w) {
		initFullVAO(w);
		initHardVAO(w);
	}

	public void renderHard(){
		texture.bind();
		Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, Matrix4f.IDENTITY);
		VAOHard.render();
		texture.unbind();
	}

	private static int[] lightPositions=new int[16];
	private static int[] lightColors=new int[16];
	private static int lightsNumber;

	static {
		lightsNumber=Shader.normalMapping.getUniform("lightSourceNumbers");
		for (int i=0; i<16; i++) {
			lightPositions[i]=Shader.normalMapping.getUniform("lightPositions["+i+"]");
			lightColors[i]=Shader.normalMapping.getUniform("lightColors["+i+"]");
		}
	}

	public void render(Camera camera, ArrayList<Vector3f> colors, ArrayList<Vector3f> positions){


		Shader.normalMapping.enable();


		Shader.normalMapping.setUniform1i(lightsNumber, colors.size());
		checkForGLError();
		for (int i=0; i<Math.min(16, colors.size()); i++) {
			Shader.normalMapping.setUniform3f(lightColors[i], colors.get(i));
			Shader.normalMapping.setUniform3f(lightPositions[i], positions.get(i));

		}

		camera.lookAtPlayer();

		texture.bind();
		texture2.bindToSecond();
		Shader.getCurrentShader().setUniformMat4f(Shader.getCurrentShader().modelMatrixUniformId, Matrix4f.IDENTITY);
		VAO.render();
		texture2.unbindFromSecond();
		texture.unbind();

		Shader.normalMapping.disable();

	}
	
}
