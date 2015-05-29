package Shootan.OpenGLInterface.Game;

import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Graphics.VertexArray;
import Shootan.OpenGLInterface.Math.Vector3f;

public class GameObject {

	public VertexArray VAO;
	public Texture texture;
	public float[] vertices, texCoords;
	public byte[] indices;
	
	public Vector3f position = new Vector3f();

	public GameObject(float[] vertices, byte[] indices, float[] texCoords, String texPath){
		this.vertices = vertices;
		this.indices = indices;
		this.texCoords = texCoords;
		texture = new Texture(texPath);
		VAO = new VertexArray(this.vertices, this.indices, this.texCoords);
	}

	public GameObject(VertexArray VAO, Texture texture) {
		this.VAO=VAO;
		this.texture =texture;
	}

	public void translate(float dx, float dy, float dz){
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}

	public void translate(Vector3f vector){
		position.x += vector.x;
		position.y += vector.y;
		position.z += vector.z;
	}

	public void setPosition(float x, float y) {
		position.x=x;
		position.y=y;
	}

	
}
