package Shootan.OpenGLInterface.Game;

import Shootan.ClientConfigs;
import Shootan.Network.ClientConnection;
import Shootan.OpenGLInterface.Input.Input;
import Shootan.Worlds.ClientWorld;
import org.lwjgl.glfw.GLFW;

import static Shootan.OpenGLInterface.Util.Utils.checkForGLError;

public class Game {

	public Camera camera = new Camera();

	private ClientWorld world;

	private WorldRender worldRender;

	public Game(){
		init();
	}

	public void mousePressed(float x, float y) {
		world.getMe().setWannaShot(true);
	}

	public void mouseReleased(float x, float y) {
		world.getMe().setWannaShot(false);
	}

	public void wheelScrolled(int side) {
		if (side>0) world.getMe().selectNextWeapon(); else world.getMe().selectPreviousWeapon();
	}

	public void mouseMoved(float x, float y) {


		float dx=(x-0.5f)*16/9;
		float dy=y-0.5f;
		float angle=0;
		if (dx!=0 || dy!=0) {
			angle= -(float) Math.atan2(dy, dx);
		}
		world.getMe().setViewAngle(angle);
	}

	public void init() {

		checkForGLError();


		world=new ClientWorld();

		worldRender=new WorldRender(world, camera);

		world.setOnInputMessage(worldRender::setMessage);

		try {
			ClientConnection c = new ClientConnection(ClientConfigs.serverIp, ClientConfigs.serverPort);
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
		} catch (Exception e) {
			System.err.println("Cannot connect to server, working locally...");
		}
	}

	
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
			world.getMe().setMotionAngle((float) (angle+Math.PI));
			world.getMe().setIsMoving(true);
		} else {
			world.getMe().setIsMoving(false);
		}



		world.update(seconds);


		camera.update();
	}

	public void render() {
		worldRender.render();
	}

	public void dispose() {
		worldRender.dispose();
	}

}
