package Shootan.UI.OpenGLInterface.Game;

import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.UI.OpenGLInterface.Math.Vector3f;
import Shootan.Worlds.World;

public class Camera {

    public Vector3f position = new Vector3f();

    private Matrix4f playerPosition = Matrix4f.identity();
    private Matrix4f playerPositionClone = playerPosition.clone();


    public void setPlayerPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        playerPosition = Matrix4f.translate(-position.x, -position.y, -position.z);
        playerPositionClone=playerPosition.clone();
    }

    public void update() {
    }

    public static final float size = World.potentialViewDistance;
    public static final int intSize = (int) size;
    public static final Matrix4f pr_matrix =
            Matrix4f.orthographic(-size * 16 / 9, size * 16 / 9, -size, size, -1.0f, 1.0f);

    public static final Matrix4f pr_matrix_1024 =
            Matrix4f.orthographic(-size, size, size, -size, -1.0f, 1.0f);

    public void lookAt(float x, float y) {

        Matrix4f playerCameraMatrix =/*
                Matrix4f
                        .getRotated(rotation)
                        .multiply(*/
                                Matrix4f.translate(-x, -y, 0);/*
                        );*/

        Shader s = Shader.getCurrentShader();

        if (s != null) {
            s.setUniformMat4f(s.viewMatrixUniformId, playerCameraMatrix);
            s.setUniformMat4f(s.projectionMatrixUniformId, pr_matrix);
        } else {
            new Exception("Current shader is not selected").printStackTrace();
        }

    }

    public void lookAtPlayer() {

        Shader s = Shader.getCurrentShader();

        if (s != null) {
            s.setUniformMat4f(s.viewMatrixUniformId, playerPositionClone);
            s.setUniformMat4f(s.projectionMatrixUniformId, pr_matrix);
        } else {
            new Exception("Current shader is not selected").printStackTrace();
        }

    }

    public void lookAtScreenCenter() {

        Shader s = Shader.getCurrentShader();
        if (s != null) {
            s.setUniformMat4f(s.viewMatrixUniformId, Matrix4f.IDENTITY);
            s.setUniformMat4f(s.projectionMatrixUniformId, pr_matrix);
        } else {
            new Exception("Current shader is not selected").printStackTrace();
        }

    }

    public void lookAtFBOCenter() {

        Shader s = Shader.getCurrentShader();
        if (s != null) {
            s.setUniformMat4f(s.viewMatrixUniformId, Matrix4f.IDENTITY);
            s.setUniformMat4f(s.projectionMatrixUniformId, pr_matrix_1024);
        } else {
            new Exception("Current shader is not selected").printStackTrace();
        }

    }
}
