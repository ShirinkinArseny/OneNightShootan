package Shootan.UI.OpenGLInterface;

import Shootan.UI.OpenGLInterface.Game.Game;
import Shootan.UI.OpenGLInterface.Input.Input;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import static Shootan.UI.OpenGLInterface.Util.Utils.checkForGLError;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main implements Runnable{

	public boolean running = true;
	
	private long window;
	
	public static final int width = 1280, height = 720;
	
	public Game game;

	private GLFWKeyCallback keyCallback;
	private GLFWScrollCallback wheelCallback;
	
	public static void main(String args[]) {
		
		Main game = new Main();
		game.start();
		
		
	}
	
	public void start() {
		running = true;
		Thread thread = new Thread(this, "haha diz z thread name");
		thread.start();
	}

	private void initGLFW() {
		if(glfwInit() != GL_TRUE){
			System.err.println("GLFW initialization failed!");
		}

		GLFWErrorCallback errorCallback = Callbacks.errorCallbackPrint();
		glfwSetErrorCallback(errorCallback);

		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);


		window = glfwCreateWindow(width, height, "Fucking window title. Who cares?", NULL, NULL);

		if(window == NULL) {
			System.err.println("Could not createShader our Window!");
		}

		//glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);


		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, 2200, 100);

		glfwSetKeyCallback(window, keyCallback = new Input());

		glfwSetScrollCallback(window, wheelCallback=new GLFWScrollCallback() {
			@Override
			public void invoke(long l, double v, double v1) {
				game.wheelScrolled((int) v);
			}
		});

		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
	}


	private void initOpenGL() {
		GLContext currentContext = GLContext.createFromCurrent();

		glClearColor(0f,0f,0f,1.0f);


		glEnable(GL_DEPTH_TEST);

		glEnable(GL_BLEND);

		System.out.println("Supported OpenGL version: " + glGetString(GL_VERSION));


	}

	private void init(){

		initGLFW();
		initOpenGL();
		checkForGLError();
		game = new Game();
	}

	private boolean lastPressed=false;
	private double x;
	private double y;
	private DoubleBuffer mouseXPosition = BufferUtils.createDoubleBuffer(1);
	private DoubleBuffer mouseYPosition = BufferUtils.createDoubleBuffer(1);
	private void update(float seconds){

		glfwPollEvents();

		glfwGetCursorPos(window, mouseXPosition, mouseYPosition);
		double x=mouseXPosition.get(0);
		double y=mouseYPosition.get(0);

		boolean pressed=1==glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1);
		if (pressed!=lastPressed) {
			if (pressed) game.mousePressed((float)x, (float)y);
			if (!pressed) game.mouseReleased((float) x, (float) y);
		}
		lastPressed=pressed;

		if (x-this.x!=0 || y-this.y!=0) {
			game.mouseMoved(
					(float)x/width,
					(float)y/height
			);
		}
		this.x=x;
		this.y=y;

		game.update(seconds);

		if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			running=false;
		}



	}
	
	public void render(){

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glViewport(0,0,width,height);

		game.render();

		checkForGLError();

		glfwSwapBuffers(window);
	}
	
	@Override
	public void run() {
		init();

		long lastTime = System.nanoTime();
		while (running) {

			long now = System.nanoTime();
			float deltaTime = (now - lastTime) / 1000000000f;
			lastTime = now;

			update(deltaTime);
			render();

			if (glfwWindowShouldClose(window) == GL_TRUE)
				running = false;
		}

		game.dispose();
		keyCallback.release();
		wheelCallback.release();
		glfwDestroyWindow(window);
		glfwTerminate();
		System.exit(0);
	}
	
	

}
