package Shootan.OpenGLInterface.Game;

import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Math.Matrix4f;
import Shootan.OpenGLInterface.Math.Vector3f;

public class Camera {

	public Vector3f position = new Vector3f();
	public Vector3f offset = new Vector3f();
	
	private float rotation = 0f;

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y){
		this.position.x =x;
		this.position.y = y;
	}

	public void setPosition(Vector3f pos){
		this.position.x = pos.x;
		this.position.y = pos.y;
		this.position.z = pos.z;
	}
	

	public void update(){
	}


	public void render(){

		Matrix4f viewMatrix=Matrix4f.rotate(rotation).multiply(Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z)));

		Shader.getShadersStream().forEach(s -> {
			s.enable();
			s.setUniformMat4f("vw_matrix", viewMatrix);
			s.disable();
		});

	}

	public void setAngle(float angle) {
		this.rotation = (float) (-angle+Math.PI/2);
	}
}
