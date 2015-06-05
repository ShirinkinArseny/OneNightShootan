package Shootan.OpenGLInterface.Graphics;

import Shootan.OpenGLInterface.Main;

import java.nio.ByteBuffer;

import static Shootan.OpenGLInterface.Util.Utils.checkForGLError;
import static Shootan.OpenGLInterface.Util.Utils.processError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

public class FBOTexture extends AbstractTexture {


    private int fboId;
    private int width;
    private int height;

    private static int createTexture(int width, int height) {

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D,
                0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);

        System.out.println("Creating FBO texture "+width+"x"+height);

        glBindTexture(GL_TEXTURE_2D, 0);

        checkForGLError();

        return textureId;
    }

    public FBOTexture(int width, int height) {
        super(createTexture(width, height));
        this.width = width;
        this.height = height;

        fboId=glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, getTextureId(), 0);

        int errCode=glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (errCode !=GL_FRAMEBUFFER_COMPLETE) {
            processError("Cannot create FBO");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }


    public void bindForWriting(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glViewport(0,0,width,height);
    }

    public void unbindForWriting(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0,0, Main.width, Main.height);
    }

    public void dispose() {
        glDeleteFramebuffers(fboId);
        glDeleteTextures(getTextureId());
    }
}
