package Shootan.OpenGLInterface.Game;

import Shootan.Bullets.Bullet;
import Shootan.OpenGLInterface.Graphics.AbstractTexture;
import Shootan.OpenGLInterface.Graphics.FBOTexture;
import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Main;
import Shootan.OpenGLInterface.Math.Matrix4f;
import Shootan.Units.Unit;
import Shootan.Worlds.ClientWorld;
import Shootan.Worlds.StrangeWorld;
import Shootan.Worlds.World;

import static Shootan.OpenGLInterface.Util.Utils.checkForGLError;
import static org.lwjgl.opengl.GL11.*;

public class WorldRender {

    private ClientWorld world;
    private Camera camera;

    private Texture brickTexture;
    private Texture floorTexture;

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

    private static final int fboWidth = 1024;
    private static final int fboHeight = 1024;

    public WorldRender(ClientWorld world, Camera camera) {
        this.world = world;
        this.camera = camera;

        brickTexture = new Texture("content/brick.png");
        floorTexture = new Texture("content/floor.png");

        for (int i = 0; i < StrangeWorld.SIZE; i++) {
            for (int j = 0; j < StrangeWorld.SIZE; j++) {
                blocks[i][j] = new BlockRenderer(world.getBlock(j, i).getType() == 0 ? brickTexture : floorTexture, j, i);
            }
        }

        scene=new FBOTexture(fboWidth, fboHeight);

        result=new FBOTexture(fboWidth, fboHeight);

        hblur = new FBOTexture(fboWidth, fboHeight);
        vblur = new FBOTexture(fboWidth, fboHeight);

        additiveBlend=new FBOTexture(512, 512);

        hardBlocksBuffer = new FBOTexture(512, 512);
        linearShadowsBuffer = new FBOTexture(512, 512);

        angledShadowBuffer = new FBOTexture(512, 1);
        fovBuffer = new FBOTexture(512, 512);
        lamp= new FBOTexture(512, 512);

        screen = new FBORenderer();

        checkForGLError();
    }

    public void dispose() {
        brickTexture.dispose();
        floorTexture.dispose();
        hardBlocksBuffer.dispose();
        linearShadowsBuffer.dispose();
        angledShadowBuffer.dispose();
        fovBuffer.dispose();
    }

    private BlockRenderer[][] blocks = new BlockRenderer[StrangeWorld.SIZE][StrangeWorld.SIZE];
    private HumanRenderer player = new HumanRenderer();
    private BulletRenderer bulletRenderer = new BulletRenderer();


    private void drawBlockLighted(int x, int y) {
        if (x >= 0 && y >= 0) {
            if (world.getBlock(x, y).getIsHard()) {
                blocks[y][x].render();
            }
        }
    }

    private void drawBlock(int x, int y) {
        int visibility = world.isVisible(x, y);
        if (visibility != 0) {

            float darkness = 0;
            switch (visibility) {
                case 1:
                    darkness = 0.25f;
                    break;
                case 2:
                    darkness = 0.5f;
                    break;
                case 3:
                    darkness = 0.75f;
                    break;
                case 4:
                    darkness = 1f;
                    break;
            }

            Shader.darkableShader.setUniform1f("darkness", darkness);
            blocks[y][x].render();
        }
    }


    private void drawUnit(Unit u) {


        player.render(u.getX(), u.getY(), u.getViewAngle());

		/*int diameter = (int) (blockSize * u.getRadius() * 2);
        g2.drawImage(
				textureLoader.getUnitTexture(u.getType())[((int) (u.getViewAngle() * 360 / 2 / Math.PI))],
				(int) ((u.getX() - u.getRadius()) * blockSize + dx),
				(int) ((u.getY() - u.getRadius()) * blockSize + dy),
				diameter, diameter, null);


		if (u.getHealth() > 0) {
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(1 - u.getHealth(), u.getHealth(), 0, 0.5f));
			g2.fillRect((int) ((u.getX() - u.getRadius()) * blockSize + dx),
					(int) ((u.getY() - u.getRadius()) * blockSize + dy - 15),
					(int) (diameter * u.getHealth()), 10);

			g2.setColor(new Color(1 - u.getHealth(), u.getHealth(), 0));
			g2.drawRect((int) ((u.getX() - u.getRadius()) * blockSize + dx),
					(int) ((u.getY() - u.getRadius()) * blockSize + dy - 15),
					(int) (diameter * u.getHealth()), 10);
			g2.drawRect((int) ((u.getX() - u.getRadius()) * blockSize + dx),
					(int) ((u.getY() - u.getRadius()) * blockSize + dy - 15),
					(int) (diameter), 10);
		}*/
    }

    private void drawBullet(Bullet b) {
        if (world.isVisible((int) b.getX(), (int) b.getY()) != 0) {
            bulletRenderer.render(b.getX(), b.getY(), b.getAngle());
        }
    }

    private AbstractTexture renderWorld() {

            camera.setPosition(world.getMe().getX(), world.getMe().getY());

            scene.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            int blockX = (int) world.getMe().getX();
            int blockY = (int) world.getMe().getY();

            Shader.darkableShader.enable();
            camera.lookAtPlayer();
            for (int x = blockX - Camera.intSize; x <= blockX + Camera.intSize; x++) {
                for (int y = blockY - Camera.intSize; y <= blockY + Camera.intSize; y++) {
                    drawBlock(x, y);
                }
            }
            Shader.darkableShader.disable();

            Shader.defaultShader.enable();
            camera.lookAtPlayer();
            player.bind();
            world.getUnits()
                    .stream()
                    .filter(u -> u.getId() != world.getMe().getId())
                    .filter(u -> world.isVisible((int) u.getX(), (int) u.getY()) != 0)
                    .forEach(this::drawUnit);
            drawUnit(world.getMe());
            player.unbind();

            bulletRenderer.bind();
            world.getBullets().forEach(this::drawBullet);
            bulletRenderer.unbind();
            Shader.defaultShader.disable();

            scene.unbindForWriting();
            return scene;
    }

    private AbstractTexture renderHardBlocks(float ddx, float ddy) {
        hardBlocksBuffer.bindForWriting();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        int dx = (int) ddx;
        int dy = (int) ddy;

        Shader.defaultShader.enable();

        camera.lookAt(ddx, ddy);

        for (int x = -Camera.intSize; x <= Camera.intSize; x++) {
            for (int y = -Camera.intSize; y <= Camera.intSize; y++) {
                drawBlockLighted(x + dx, y + dy);
            }
        }

        Shader.defaultShader.disable();
        hardBlocksBuffer.unbindForWriting();
        return hardBlocksBuffer;
    }

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
        Shader.unrollShadows.setUniform1f("color_r", r);
        Shader.unrollShadows.setUniform1f("color_g", g);
        Shader.unrollShadows.setUniform1f("color_b", b);
        camera.lookAtFBOCenter();
        screen.render(-(world.getMe().getX()-x)*9/16, world.getMe().getY()-y,
                Shader.unrollShadows);
        Shader.unrollShadows.disable();
        angledShadowBuffer.unbind();
        result.unbindForWriting();

        checkForGLError();
    }

    private void additiveBlend(FBOTexture result, AbstractTexture a, AbstractTexture b) {
        result.bindForWriting();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        a.bind();
        b.bindToSecond();
            Shader.additiveBlend.enable();
            camera.lookAtFBOCenter();
            screen.render(Matrix4f.IDENTITY, Shader.additiveBlend);
            Shader.additiveBlend.disable();
        a.unbind();
        a.unbindFromSecond();
        result.unbindForWriting();
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

    public void render() {

        AbstractTexture scene=renderWorld();

        renderLightMap(fovBuffer, 24, 24, 1f, 1f, 1f);
        renderLightMap(lamp, 16, 16, 1f, 0.4f, 0f);

        additiveBlend( additiveBlend,
                fovBuffer,
                lamp
        );

        renderLightMap(lamp, 32, 16, 1f, 0.4f, 0f);

        additiveBlend( fovBuffer,
                additiveBlend,
                lamp
        );

        renderLightMap(lamp, world.getMe().getX(), world.getMe().getY(), 1f, 1f, 0f);

        additiveBlend( additiveBlend,
                fovBuffer,
                lamp
        );

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


        Shader.defaultShader.enable();
        camera.lookAtFBOCenter();

        result.bind();
        screen.render(Matrix4f.IDENTITY, Shader.defaultShader);
        result.unbind();

        Shader.defaultShader.disable();
    }

}
