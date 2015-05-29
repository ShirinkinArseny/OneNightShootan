package Shootan.OpenGLInterface.Math;

import Shootan.OpenGLInterface.Util.Utils;

import java.nio.FloatBuffer;

public class Matrix4f {

	public static final int SIZE = 4 * 4;
	public float[] elements = new float[SIZE];
	
	public Matrix4f(){ }
	
	public static Matrix4f identity(){
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 0 * 4] = 1.0f;
		matrix.elements[1 + 1 * 4] = 1.0f;
		matrix.elements[2 + 2 * 4] = 1.0f;
		matrix.elements[3 + 3 * 4] = 1.0f;

		return matrix;
	}
	
	
	// Gives us our orthographic matrix
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far){
		Matrix4f matrix = new Matrix4f();
		
		matrix.elements[0 + 0 * 4] = 2.0f / (right - left);
		matrix.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		matrix.elements[2 + 2 * 4] = 2.0f / (near - far);

		matrix.elements[0 + 3 * 4] = (left + right) / (left - right);
		matrix.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		matrix.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		matrix.elements[3 + 3 * 4] = 1.0f;
		
		return matrix;
	}

	public static Matrix4f translate(float x, float y, float z){
		Matrix4f matrix = identity();
		matrix.elements[0 + 3 * 4] = x;
		matrix.elements[1 + 3 * 4] = y;
		matrix.elements[2 + 3 * 4] = z;
		return matrix;
	}

	public static Matrix4f translate(Vector3f vector){
		Matrix4f matrix = identity();
		matrix.elements[0 + 3 * 4] = vector.x;
		matrix.elements[1 + 3 * 4] = vector.y;
		matrix.elements[2 + 3 * 4] = vector.z;
		return matrix;
	}

	public static Matrix4f rotate(float angle){
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		
		matrix.elements[0 + 0 * 4] = cos;
		matrix.elements[1 + 0 * 4] = sin;
		matrix.elements[0 + 1 * 4] = -sin;
		matrix.elements[1 + 1 * 4] = cos;		
		return matrix;
	}

	public Matrix4f multiply(Matrix4f matrix){
		Matrix4f result = new Matrix4f();
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				float sum = 0.0f;
				for(int e = 0; e < 4; e++){
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
				}
				result.elements[x + y * 4] = sum;
			}
		}
		return result;
	}
	
	public FloatBuffer toFloatBuffer(){
		return Utils.createFloatBuffer(elements);
	}
	
	
}
