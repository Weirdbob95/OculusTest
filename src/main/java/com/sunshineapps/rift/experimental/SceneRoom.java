package com.sunshineapps.rift.experimental;

import static org.lwjgl.opengl.GL11.GL_DECAL;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;

import com.sunshineapps.riftexample.thirdparty.Texture;
import com.sunshineapps.riftexample.thirdparty.TextureLoader;

public final class SceneRoom implements Scene {
    private Texture floorTexture;
    private Texture wallTexture;
    private Texture ceilingTexture;
    private float roomSize = 6.0f;
    private float roomHeight = 2.6f*2;
    
    public final void init() {
        TextureLoader loader = new TextureLoader();
        try {
            floorTexture = loader.getTexture("floor512512.png");
            wallTexture = loader.getTexture("panel512512.png");
            ceilingTexture = loader.getTexture("ceiling512512.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public final void render() {
        glEnable(GL_TEXTURE_2D);

        floorTexture.bind();
        drawPlaneFloor();
        floorTexture.unbind();
        
        wallTexture.bind();
        drawPlaneWallLeft();
        drawPlaneWallFront();
        drawPlaneWallRight();
        drawPlaneWallBack();
        wallTexture.unbind();
        
        ceilingTexture.bind();
        glTranslatef(0.0f, roomHeight, 0.0f);
        drawPlaneCeiling();
        glTranslatef(0.0f, -roomHeight, 0.0f);
        ceilingTexture.unbind();

        glDisable(GL_TEXTURE_2D);
    }
    
    
    //Z- is into the screen
    private void drawPlaneFloor() {
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        
        float tileSize = 1.0f; // if same then there are two tiles per square
        glBegin(GL_QUADS);
        {
            glNormal3f(0f, 1f, 0f);
            glColor4f(1f, 1f, 1f, 1f);
            
            glTexCoord2f(0f, 0f);
            glVertex3f(-roomSize, 0f, roomSize);
            
            glTexCoord2f(tileSize, 0f);
            glVertex3f(roomSize, 0f, roomSize);
            
            glTexCoord2f(tileSize, tileSize);
            glVertex3f(roomSize, 0f, -roomSize);
            
            glTexCoord2f(0f, tileSize);
            glVertex3f(-roomSize, 0f, -roomSize);
        }
        glEnd();
    }
    
    
    private void drawPlaneCeiling() {
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        
        float tileSize = 1.0f; // if same then there are two tiles per square
        glBegin(GL_QUADS);
        {
            glNormal3f(0f, -1f, 0f);
            glColor4f(1f, 1f, 1f, 1f);
            
            glTexCoord2f(0f, 0f);
            glVertex3f(-roomSize, 0f, roomSize);
            
            glTexCoord2f(0f, tileSize);
            glVertex3f(-roomSize, 0f, -roomSize);
            
            glTexCoord2f(tileSize, tileSize);
            glVertex3f(roomSize, 0f, -roomSize);
            
            glTexCoord2f(tileSize, 0f);
            glVertex3f(roomSize, 0f, roomSize);
        }
        glEnd();
    }
    
    //Z- is into the screen
    private void drawPlaneWallLeft() {     //appears in front
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        
        float tileSize = 1.0f; 
        glBegin(GL_QUADS);
        {
            glNormal3f(1f, 0f, 0f);
            glColor4f(1f, 1f, 1f, 1f);
            
            glTexCoord2f(tileSize*6, 0f);
            glVertex3f(-roomSize, 0f, roomSize);
            
            glTexCoord2f(0f, 0f);
            glVertex3f(-roomSize, 0f, -roomSize);
            
            glTexCoord2f(0f, tileSize);
            glVertex3f(-roomSize, roomHeight, -roomSize);
            
            glTexCoord2f(tileSize*6, tileSize);
            glVertex3f(-roomSize, roomHeight, roomSize);
        }
        glEnd();
    }
    
    //Z- is into the screen
    private void drawPlaneWallFront() {    //appears to right
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        
        float tileSize = 1.0f; 
        glBegin(GL_QUADS);
        {
            glNormal3f(0f, 0f, 1f);
            glColor4f(1f, 1f, 1f, 1f);
            
            glTexCoord2f(tileSize*6, 0f);
            glVertex3f(-roomSize, 0f, -roomSize);

            glTexCoord2f(0f, 0f);
            glVertex3f(roomSize, 0f, -roomSize);

            glTexCoord2f(0f, tileSize);
            glVertex3f(roomSize, roomHeight, -roomSize);

            glTexCoord2f(tileSize*6, tileSize);
            glVertex3f(-roomSize, roomHeight, -roomSize);
        }
        glEnd();
    }
    
    private void drawPlaneWallRight() {    //appears behind
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        
        float tileSize = 1.0f; 
        glBegin(GL_QUADS);
        {
            glNormal3f(-1f, 0f, 0f);
            glColor4f(1f, 1f, 1f, 1f);
            
            glTexCoord2f(tileSize*6, 0f);
            glVertex3f(roomSize, 0f, -roomSize);
            
            glTexCoord2f(0f, 0f);
            glVertex3f(roomSize, 0f, roomSize);
            
            glTexCoord2f(0f, tileSize);
            glVertex3f(roomSize, roomHeight, roomSize);
            
            glTexCoord2f(tileSize*6, tileSize);
            glVertex3f(roomSize, roomHeight, -roomSize);
        }
        glEnd();
    }
   
    private void drawPlaneWallBack() {    //appears 
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        
        float tileSize = 1.0f; 
        glBegin(GL_QUADS);
        {
            glNormal3f(0f, 0f, -1f);
            glColor4f(1f, 1f, 1f, 1f);
            
            glTexCoord2f(tileSize*6, 0f);
            glVertex3f(roomSize, 0f, roomSize);

            glTexCoord2f(0f, 0f);
            glVertex3f(-roomSize, 0f, roomSize);
            
            glTexCoord2f(0f, tileSize);
            glVertex3f(-roomSize, roomHeight, roomSize);
            
            glTexCoord2f(tileSize*6, tileSize);
            glVertex3f(roomSize, roomHeight, roomSize);
        }
        glEnd();
    }
    

}
