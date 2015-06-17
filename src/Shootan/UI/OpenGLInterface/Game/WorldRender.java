package Shootan.UI.OpenGLInterface.Game;

import Shootan.Benchmark;
import Shootan.GameEssences.Bullets.Bullet;
import Shootan.UI.OpenGLInterface.Graphics.AbstractTexture;
import Shootan.UI.OpenGLInterface.Graphics.FBOTexture;
import Shootan.UI.OpenGLInterface.Graphics.Shader;
import Shootan.UI.OpenGLInterface.Math.Matrix4f;
import Shootan.GameEssences.Units.Unit;
import Shootan.Worlds.ClientWorld;
import org.lwjgl.opengl.GL14;


import static Shootan.UI.OpenGLInterface.Util.Utils.checkForGLError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendEquation;

public class WorldRender {

    private ClientWorld world;
    private Camera camera;

    private FBOTexture hardBlocksBuffer;
    private FBOTexture linearShadowsBuffer;
    private FBOTexture angledShadowBuffer;
    private FBOTexture fovBuffer;

    private FBOTexture lamp;

    private FBOTexture hblur;
    private FBOTexture vblur;

    private FBOTexture result;

    private FBOTexture scene;

    private FBOTexture additiveBlend;

    private FBORenderer screen;

    private FontRenderer fontRenderer;

    private MapRenderer mapRenderer;

    private static final int fboSize = 1024;
    private String message="Game had been started!";

    public WorldRender(ClientWorld world, Camera camera) {
        this.world = world;
        this.camera = camera;

        scene=new FBOTexture(fboSize, fboSize);

        result=new FBOTexture(fboSize, fboSize);

        hblur = new FBOTexture(fboSize, fboSize);
        vblur = new FBOTexture(fboSize, fboSize);

        additiveBlend=new FBOTexture(fboSize/2, fboSize/2);

        hardBlocksBuffer = new FBOTexture(fboSize/2, fboSize/2);
        linearShadowsBuffer = new FBOTexture(fboSize/2, fboSize/2);

        angledShadowBuffer = new FBOTexture(fboSize/2, 1);
        fovBuffer = new FBOTexture(fboSize/2, fboSize/2);
        lamp= new FBOTexture(fboSize/2, fboSize/2);

        screen = new FBORenderer();

        fontRenderer=new FontRenderer(0.6f, 0.6f);

        mapRenderer=new MapRenderer(world);

        checkForGLError();
    }

    public void dispose() {
        hardBlocksBuffer.dispose();
        linearShadowsBuffer.dispose();
        angledShadowBuffer.dispose();
        fovBuffer.dispose();
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
                (int)u.getX(),
                (int)u.getY())) {

            player.bind();
            player.render(u.getX(), u.getY(), u.getViewAngle());
            player.unbind();

            fontRenderer.render(getUnitState(u), u.getX(), u.getY());
        }
    }

    private void drawBullet(Bullet b) {
        if (world.isVisible((int) b.getX(), (int) b.getY())) {
            bulletRenderer.render(b);
        }
    }

    private AbstractTexture renderWorld() {

        enableDefaultBlending();
            scene.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Shader.defaultShader.enable();
            camera.lookAtPlayer();

            mapRenderer.render(0, 0);

            world.getUnits()
                    .stream()
                    .filter(u -> u.getId() != world.getMe().getId())
                    .filter(u -> world.isVisible((int) u.getX(), (int) u.getY()))
                    .forEach(this::drawUnit);
            drawUnit(world.getMe());

            world.getBullets().forEach(this::drawBullet);
            Shader.defaultShader.disable();

            scene.unbindForWriting();
            return scene;
    }

    private AbstractTexture renderHardBlocks(float ddx, float ddy) {
        hardBlocksBuffer.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Shader.defaultShader.enable();

        camera.lookAt(ddx, ddy);

        mapRenderer.renderHard(0, 0);

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
        screen.render(Matrix4f.IDENTITY, Shader.rollShadows);
        Shader.rollShadows.disable();
        hardBlock.unbind();
        linearShadowsBuffer.unbindForWriting();

        angledShadowBuffer.bindForWriting();


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        linearShadowsBuffer.bind();
        Shader.angleShadows.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY, Shader.angleShadows);
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
        screen.render(-(world.getMe().getX()-x)*9/16, world.getMe().getY()-y,
                Shader.unrollShadows);
        Shader.unrollShadows.disable();
        angledShadowBuffer.unbind();
        result.unbindForWriting();

        checkForGLError();
    }

    private AbstractTexture renderBlur(AbstractTexture img) {

        hblur.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        img.bind();
        Shader.hblur.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY, Shader.hblur);
        Shader.hblur.disable();
        img.unbind();
        hblur.unbindForWriting();

        vblur.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        hblur.bind();
        Shader.vblur.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY, Shader.vblur);
        Shader.vblur.disable();
        hblur.unbind();
        vblur.unbindForWriting();

        checkForGLError();

        return vblur;
    }


    private void dropLightning() {
        additiveBlend.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        additiveBlend.unbindForWriting();
    }

    private Benchmark lightning=new Benchmark("Lightning", false);

    private void renderLightSource(float x, float y, float r, float g, float b) {


        if (Math.abs(x-world.getMe().getX())>=Camera.intSize*2 || Math.abs(y-world.getMe().getY())>=Camera.intSize*2) {
            return;
        }

        lightning.tick();
        renderLightMap(lamp, x, y, r, g, b);

        additiveBlend.bindForWriting();

        lamp.bind();
        Shader.defaultShader.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY, Shader.defaultShader);
        Shader.defaultShader.disable();
        lamp.unbind();

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
        renderLightMap(fovBuffer, world.getMe().getX(), world.getMe().getY(), 1f, 1f, 1f);

        Shader.multiplyColors.enable();
        camera.lookAtFBOCenter();

        result.bind();
        fovBuffer.bindToSecond();
        screen.render(Matrix4f.IDENTITY, Shader.multiplyColors);
        fovBuffer.unbindFromSecond();
        result.unbind();

        Shader.multiplyColors.disable();
    }

    private void drawHUD() {
        Shader.defaultShader.enable();
        camera.lookAtScreenCenter();

        fontRenderer.render(
                world.getMe().getWeapon().getName()+" "+
                        world.getMe().getWeapon().getHaveBulletsInCage()+"/"+world.getMe().getWeapon().getHaveBullets()+" "+
                        world.getMe().getWeapon().getTimeToNextShot()
                , -10, 9);

        fontRenderer.render(
                message
                , -10, -9);

        Shader.defaultShader.disable();
    }

    private void drawLightSources() {
        final int[] lightSources = {0};
        dropLightning();
        enableAdditiveBlending();
        renderLightSource(10, 10, 1f, 1f, 1f); lightSources[0]++;
        renderLightSource(10, 45, 1f, 0f, 1f); lightSources[0]++;
        renderLightSource(45, 10, 1f, 1f, 0f); lightSources[0]++;
        renderLightSource(45, 45, 0f, 1f, 1f); lightSources[0]++;

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
                    renderLightSource(b.getX(), b.getY(), light[0], light[1], light[2]);
                    lightSources[0]++;
                });
        renderLightSource(world.getMe().getX(), world.getMe().getY(), 0.1f, 0.1f, 0.1f);
        lightSources[0]++;

        //System.out.println("Lightsources: "+lightSources[0]);
    }

    private void applyLightning() {
        result.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        scene.bind();
        additiveBlend.bindToSecond();

        Shader.multiplyColors.enable();
        camera.lookAtFBOCenter();
        screen.render(Matrix4f.IDENTITY, Shader.multiplyColors);
        Shader.multiplyColors.disable();

        additiveBlend.unbindFromSecond();
        scene.unbind();

        result.unbindForWriting();
    }

    Benchmark render=new Benchmark("Render", true);

    public void render() {

        render.tick();

        camera.setPlayerPosition(world.getMe().getX(), world.getMe().getY());

        renderWorld();
        drawLightSources();
        enableDefaultBlending();
        applyLightning();
        applyFov();
        drawHUD();



        render.tack();

    }

    public void setMessage(String message) {
        this.message = message;
    }

}
