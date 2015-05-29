package Shootan.OpenGLInterface.Game;

import Shootan.Bullets.Bullet;
import Shootan.Network.ClientConnection;
import Shootan.OpenGLInterface.Graphics.Shader;
import Shootan.OpenGLInterface.Graphics.Texture;
import Shootan.OpenGLInterface.Input.Input;
import Shootan.Units.Unit;
import Shootan.Worlds.ClientWorld;
import Shootan.Worlds.StrangeWorld;
import Shootan.Worlds.World;
import org.lwjgl.glfw.GLFW;

import java.util.ConcurrentModificationException;

public class Game {

	public Camera camera = new Camera();

	public Game(){
		init();
	}

	private Texture brickTexture=new Texture("content/brick.png");
	private Texture floorTexture=new Texture("content/floor.png");

	public void mousePressed(float x, float y) {
		world.getMe().setWannaShot(true);
	}

	public void mouseReleased(float x, float y) {
		world.getMe().setWannaShot(false);
	}

	public void wheelScrolled(int side) {
		if (side>0) world.getMe().selectNextWeapon(); else world.getMe().selectPreviousWeapon();
	}

	public void mouseMoved(float dx, float dy) {
		float angle=-(float) (dx*2*Math.PI);
		world.getMe().changeViewAngle(angle);

		camera.setAngle(world.getMe().getViewAngle());
	}

	public void init(){

		world=new ClientWorld(
				(unit, abstractBullet) ->
						System.out.println(unit + " killed by " + abstractBullet)
		);

		for (int i=0; i<StrangeWorld.SIZE; i++) {
			for (int j=0; j<StrangeWorld.SIZE; j++) {
				blocks[i][j]=new Block(world.getBlock(j, i).getType()==0?brickTexture:floorTexture, j, i);
			}
		}

		ClientConnection c = new ClientConnection("127.0.0.1", 1234);
		System.out.println("Client connection created!");
		c.setOnInputEvent(world::acceptWorldDump);
		System.out.println("Callback setted!");
		c.start();

		new Thread(() -> {
			while (true) {
				c.sendMessage(world.createUnitChangedState());
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private Block[][] blocks=new Block[StrangeWorld.SIZE][StrangeWorld.SIZE];
	private ClientWorld world;
	private HumanRenderer player=new HumanRenderer();
	private BulletRenderer bulletRenderer=new BulletRenderer();

	
	public void update(float seconds){

		boolean up=false;
		boolean down=false;
		boolean left=false;
		boolean right=false;

		if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
			up=true;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
			left=true;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
			down=true;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
			right=true;
		}

		if (up && down) {
			up=false;
			down=false;
		}

		if (left && right) {
			left=false;
			right=false;
		}

		double angle=-1;

		if (up) {
			if (left) {
				angle=Math.PI*5/4;
			} else
			if (right) {
				angle=Math.PI*7/4;
			} else
				angle=Math.PI*6/4;
		} else
		if (down) {
			if (left) {
				angle=Math.PI*3/4;
			} else
			if (right) {
				angle=Math.PI*1/4;
			} else
				angle=Math.PI*2/4;
		} else
		if (left) {
			angle=Math.PI*4/4;
		} else
		if (right) {
			angle=0;
		}

		if (angle >= 0) {
			world.getMe().setMotionAngle((float) (angle+world.getMe().getViewAngle()+Math.PI/2));
			world.getMe().setIsMoving(true);
		} else {
			world.getMe().setIsMoving(false);
		}



		world.update(seconds);


		camera.update();
		camera.setPosition(world.getMe().getX(), world.getMe().getY());
	}


	private void drawBlock(int x, int y) {
		int visibility=world.isVisible(x, y);
		if (visibility!=0) {

			float darkness=0;
			switch (visibility) {
				case 1: darkness=0.25f; break;
				case 2: darkness=0.5f; break;
				case 3: darkness=0.75f; break;
				case 4: darkness=1f; break;
			}

			Shader.darkableShader.setUniform1f("darkness", darkness);
			blocks[y][x].renderShadowed();
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
		if (world.isVisible((int) b.getX(), (int) b.getY())!=0) {
			bulletRenderer.render(b.getX(), b.getY(), b.getAngle());
		}
	}

	public void render() {

		camera.render();


		try {
			int blockX = (int) world.getMe().getX();
			int blockY = (int) world.getMe().getY();

			Shader.darkableShader.enable();
			for (int x = blockX - World.getPotentialViewDistance; x <= blockX + World.getPotentialViewDistance; x++) {
				for (int y = blockY - World.getPotentialViewDistance; y <= blockY + World.getPotentialViewDistance; y++) {
					drawBlock(x, y);
				}
			}
			Shader.darkableShader.disable();

			Shader.rotableShader.enable();
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
			Shader.rotableShader.disable();


		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		}
	}

}
