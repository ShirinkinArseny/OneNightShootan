package Shootan.UI.OpenGLInterface.Game;

import Shootan.Benchmark;
import Shootan.GameEssences.Bullets.Bullet;
import Shootan.UI.OpenGLInterface.Graphics.AbstractTexture;
import Shootan.UI.OpenGLInterface.Graphics.FBOTexture;
import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Main;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.GameEssences.Units.Unit;
import Shootan.UI.OpenGLInterface.Math.Vector3f;
import Shootan.Worlds.ClientWorld;
import org.lwjgl.opengl.GL14;


import java.util.ArrayList;

import static Shootan.UI.OpenGLInterface.Util.Utils.checkForGLError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendEquation;

public class WorldRender {

    private ClientWorld world;
    private Camera camera;

    /*
    Lightning
     */
    private FBOTexture hardBlocksBuffer;
    private FBOTexture lampLightMapBuffer;
    private FBOTexture linearShadowsBuffer;
    private FBOTexture angledShadowBuffer;
    private FBOTexture fovBuffer;
    private FBOTexture lightenedScene;
    private FBOTexture scene;
    private FBOTexture additiveBlend;

    /*
    User UI
     */
    private FBOTexture hudBuffer;

    /*
    Blur
     */
    private FBOTexture hblur;
    private FBOTexture vblur;

    /*
    Result framebuffer
     */
    private FBOTexture finalFrame;

    private FBORenderer screen;

    private FontRenderer fontRenderer;

    private MapRenderer mapRenderer;

    private static final int fboSize = 1024;
    private String message="Game had been started!";

    public WorldRender(ClientWorld world, Camera camera) {
        this.world = world;
        this.camera = camera;

        finalFrame=new FBOTexture(Main.width, Main.height);
        hudBuffer=new FBOTexture(Main.width, Main.height);

        scene=new FBOTexture(fboSize, fboSize);

        lightenedScene =new FBOTexture(fboSize, fboSize);

        hblur = new FBOTexture(fboSize, fboSize);
        vblur = new FBOTexture(fboSize, fboSize);

        additiveBlend=new FBOTexture(fboSize/2, fboSize/2);

        hardBlocksBuffer = new FBOTexture(fboSize/2, fboSize/2);
        linearShadowsBuffer = new FBOTexture(fboSize/2, fboSize/2);

        angledShadowBuffer = new FBOTexture(fboSize/2, 1);
        fovBuffer = new FBOTexture(fboSize/2, fboSize/2);
        lampLightMapBuffer = new FBOTexture(fboSize/2, fboSize/2);

        screen = new FBORenderer();

        fontRenderer=new FontRenderer(0.7f, 0.35f);

        mapRenderer=new MapRenderer(world);

        checkForGLError();
    }

    public void dispose() {
        FBOTexture.disposeAll();
    }

    private HumanRenderer player = new HumanRenderer();
    private BulletRenderer bulletRenderer = new BulletRenderer();

    private String floatToString(float f) {
        String h= String.valueOf(f*100);
        if (h.length()>4) {
            if (h.charAt(3)=='.') h=h.substring(0, 3); else
            h = h.substring(0, 4);
        }
        return h;
    }

    private String getUnitState(Unit u) {
        return u.getId()+" ["+floatToString(u.getHealth())+"]";
    }

    private void drawUnit(Unit u) {

        if (world.isVisible(
                (int)u.getTimeApproxX(),
                (int)u.getTimeApproxY())) {

            player.bind();
            player.render(u.getTimeApproxX(), u.getTimeApproxY(), u.getViewAngle());
            player.unbind();

            fontRenderer.render(getUnitState(u), u.getTimeApproxX(), u.getTimeApproxY());
        }
    }

    private void drawBullet(Bullet b) {
        if (world.isVisible((int) b.getTimeApproxX(), (int) b.getTimeApproxY())) {
            bulletRenderer.render(b);
        }
    }

    private AbstractTexture renderWorld() {


        enableDefaultBlending();
            scene.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            mapRenderer.render(camera, colors, positions);


        Shader.defaultShader.enable();
        camera.lookAtPlayer();
            world.getBullets().forEach(this::drawBullet);

            world.getUnits()
                    .stream()
                    .filter(u -> u.getId() != world.getMe().getId())
                    .filter(u -> world.isVisible((int) u.getTimeApproxX(), (int) u.getTimeApproxY()))
                    .forEach(this::drawUnit);
            drawUnit(world.getMe());

            Shader.defaultShader.disable();

            scene.unbindForWriting();


        checkForGLError();
            return scene;
    }

    private AbstractTexture renderHardBlocks(float ddx, float ddy) {
        hardBlocksBuffer.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader.defaultShader.enable();

        camera.lookAt(ddx, ddy);

        mapRenderer.renderHard();

        Shader.defaultShader.disable();
        hardBlocksBuffer.unbindForWriting();
        return hardBlocksBuffer;
    }

    private int unrollShaderColorR=Shader.unrollShadows.getUniform("color_r");
    private int unrollShaderColorG=Shader.unrollShadows.getUniform("color_g");
    private int unrollShaderColorB=Shader.unrollShadows.getUniform("color_b");

    private void renderLightMap(FBOTexture result, float x, float y, float r, float g, float b) {

        AbstractTexture hardBlock = renderHardBlocks(x, y);

        linearShadowsBuffer.bindForWriting();


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        hardBlock.bind();
        Shader.rollShadows.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY);
        Shader.rollShadows.disable();
        hardBlock.unbind();
        linearShadowsBuffer.unbindForWriting();


        angledShadowBuffer.bindForWriting();


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        linearShadowsBuffer.bind();
        Shader.angleShadows.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY);
        Shader.angleShadows.disable();
        linearShadowsBuffer.unbind();
        angledShadowBuffer.unbindForWriting();

        result.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        angledShadowBuffer.bind();
        Shader.unrollShadows.enable();
        Shader.unrollShadows.setUniform1f(unrollShaderColorR, r);
        Shader.unrollShadows.setUniform1f(unrollShaderColorG, g);
        Shader.unrollShadows.setUniform1f(unrollShaderColorB, b);
        camera.lookAtFBOCenter();
        screen.render(-(world.getMe().getTimeApproxX()-x)*9/16, world.getMe().getTimeApproxY()-y);
        Shader.unrollShadows.disable();
        angledShadowBuffer.unbind();
        result.unbindForWriting();
    }

    private AbstractTexture renderBlur(AbstractTexture img) {

        hblur.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        img.bind();
        Shader.hblur.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY);
        Shader.hblur.disable();
        img.unbind();
        hblur.unbindForWriting();

        vblur.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        hblur.bind();
        Shader.vblur.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY);
        Shader.vblur.disable();
        hblur.unbind();
        vblur.unbindForWriting();

        checkForGLError();

        return vblur;
    }


    private ArrayList<Vector3f> colors=new ArrayList<>();
    private ArrayList<Vector3f> positions=new ArrayList<>();

    private void dropLightning() {

        colors.clear();
        positions.clear();

        additiveBlend.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        additiveBlend.unbindForWriting();
    }

    private Benchmark lightning=new Benchmark("Lightning", false);

    private void renderLightSource(float x, float y, float r, float g, float b) {

        if (Math.abs(x-world.getMe().getTimeApproxX())>=Camera.intSize*2 || Math.abs(y-world.getMe().getTimeApproxY())>=Camera.intSize*2) {
            return;
        }

        colors.add(new Vector3f(r, g, b));
        positions.add(new Vector3f(x, y, 3f));

        lightning.tick();
        renderLightMap(lampLightMapBuffer, x, y, r, g, b);

        additiveBlend.bindForWriting();

        lampLightMapBuffer.bind();
        Shader.defaultShader.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY);
        Shader.defaultShader.disable();
        lampLightMapBuffer.unbind();

        additiveBlend.unbindForWriting();

        lightning.tack();
    }

    private void enableAdditiveBlending() {
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendEquation(GL14.GL_FUNC_ADD);
    }

    private void enableDefaultBlending() {
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glBlendEquation(GL14.GL_FUNC_ADD);
    }

    private void applyFov() {

        renderLightMap(fovBuffer, world.getMe().getTimeApproxX(), world.getMe().getTimeApproxY(), 1f, 1f, 1f);


        finalFrame.bindForWriting();


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Shader.multiplyColors.enable();
            camera.lookAtFBOCenter();

            lightenedScene.bind();
            fovBuffer.bindToSecond();
            screen.render(Matrix4f.IDENTITY);
            fovBuffer.unbindFromSecond();
            lightenedScene.unbind();

            Shader.multiplyColors.disable();
        finalFrame.unbindForWriting();
    }

    private void drawHUD() {
        hudBuffer.bindForWriting();
        Shader.defaultShader.enable();
        camera.lookAtFBOCenter();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        fontRenderer.render(
                world.getMe().getWeapon().getName()+" "+
                        world.getMe().getWeapon().getHaveBulletsInCage()+"/"+world.getMe().getWeapon().getHaveBullets()+" "+
                        world.getMe().getWeapon().getTimeToNextShot()
                , -10, 9);

        fontRenderer.render(
                message
                , -10, -9);

        Shader.defaultShader.disable();
        hudBuffer.unbindForWriting();

        finalFrame.bindForWriting();

            Shader.defaultShader.enable();
            camera.lookAtScreenCenter();



            hudBuffer.bind();
            screen.render(Matrix4f.IDENTITY);
            hudBuffer.unbind();
            Shader.defaultShader.disable();
        finalFrame.unbindForWriting();

    }

    private void drawLightSources() {
        dropLightning();
        enableAdditiveBlending();
        renderLightSource(10, 10, 1f, 1f, 1f);
        renderLightSource(10, 45, 1f, 0f, 1f);
        renderLightSource(45, 10, 1f, 1f, 0f);
        renderLightSource(45, 45, 0f, 1f, 1f);

        float time=(System.currentTimeMillis()%1000)/1000f;

        renderLightSource(
                (float)(27+4*Math.cos(time*2*Math.PI)),
                (float)(27+4*Math.sin(time*2*Math.PI)),
                1f, 0f, 0f);

        renderLightSource(
                (float)(27-4*Math.cos(time*2*Math.PI)),
                (float)(27-4*Math.sin(time*2*Math.PI)),
                1f, 1f, 0f);


        renderLightSource(
                27,
                27,
                0f, 0f, (float) ((1+Math.sin(time*2*Math.PI))/2));

        world.getBullets()
                .stream()
                .filter(Bullet::getHasLightning)
                .forEach(b -> {
                    float[] light = b.getRGBLigtning();
                    if (light[0]>1f || light[1]>1f || light[2]>1f) {
                        new Exception("Lightsource >1f").printStackTrace();
                    }
                    renderLightSource(b.getTimeApproxX(), b.getTimeApproxY(), light[0], light[1], light[2]);
                });
        renderLightSource(world.getMe().getTimeApproxX(), world.getMe().getTimeApproxY(), 0.5f, 0.3f, 0.0f);
    }

    private void applyLightning() {
        lightenedScene.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        scene.bind();
        additiveBlend.bindToSecond();

        Shader.multiplyColors.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY);
        Shader.multiplyColors.disable();

        additiveBlend.unbindFromSecond();
        scene.unbind();

        lightenedScene.unbindForWriting();
    }

    Benchmark render=new Benchmark("Render", true);

    public void render() {

        render.tick();

        camera.setPlayerPosition(world.getMe().getTimeApproxX(), world.getMe().getTimeApproxY());

        renderWorld();
        drawLightSources();
        enableDefaultBlending();
        applyLightning();
        applyFov();
        drawHUD();

        //AbstractTexture blurred=renderBlur(finalFrame);


        Shader.defaultShader.enable();
        camera.lookAtFBOCenter();
        finalFrame.bind();
        screen.render(Matrix4f.IDENTITY);
        finalFrame.unbind();
        Shader.defaultShader.disable();


        render.tack();

    }

    public void setMessage(String message) {
        this.message = message;
    }

}
