package Shootan.OpenGLInterface.Game;

import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Math.Matrix4f;
import Shootan.OpenGLInterface.Math.Vector3f;

public class Camera {

    public Vector3f position = new Vector3f();

    private float rotation = 0f;
    private Matrix4f playerPosition = Matrix4f.identity();
    private Matrix4f playerPositionClone = playerPosition.clone();
    private Matrix4f playerCameraMatrix = Matrix4f.identity();

    private void updatePlayersMatrix() {
        playerCameraMatrix =
                Matrix4f
                        .getRotated(rotation)
                        .multiply(
                                playerPosition
                        );
    }

    public void setPlayerPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        playerPosition = Matrix4f.translate(-position.x, -position.y, -position.z);
        playerPositionClone=playerPosition.clone();
        updatePlayersMatrix();
    }

    public void update() {
    }

    public static final float size = 10f;
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

    public void lookAtFBOCenter(float dx, float dy) {

        Shader s = Shader.getCurrentShader();
        if (s != null) {

            Matrix4f translationMatrix = Matrix4f.translate(dx, dy, 0);

            s.setUniformMat4f(s.viewMatrixUniformId, translationMatrix);
            s.setUniformMat4f(s.projectionMatrixUniformId, pr_matrix_1024);
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

    public void setAngle(float angle) {
        this.rotation = (float) (-angle + Math.PI / 2);
        updatePlayersMatrix();
    }
}
