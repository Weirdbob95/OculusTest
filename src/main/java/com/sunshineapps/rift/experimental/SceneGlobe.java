package com.sunshineapps.rift.experimental;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;

public final class SceneGlobe implements Scene {
    private final double scale = 100000;

    private final class VBO {
        int vHandle;
        int cHandle;
        int vertexCount;
        FloatBuffer colorFB;
        FloatBuffer vertexFB;
    }
    private ArrayList<VBO> sceneVBOs = new ArrayList<VBO>(1000);

     private double[] method2(final double lat, final double lon) {
        double cosLat = Math.cos(lat * Math.PI / 180.0f);
        double sinLat = Math.sin(lat * Math.PI / 180.0f);
        double cosLon = Math.cos(lon * Math.PI / 180.0f);
        double sinLon = Math.sin(lon * Math.PI / 180.0f);
        double rad = 6378137.0;

        double f = 1.0f / 298.257224f;
        double C = 1.0f / Math.sqrt(cosLat * cosLat + (1f - f) * (1f - f) * sinLat * sinLat);
        double S = (1.0f - f) * (1.0f - f) * C;
        double h = 0.0f;
        double x = (rad * C + h) * cosLat * cosLon;
        double y = (rad * C + h) * cosLat * sinLon;
        double z = (rad * S + h) * sinLat;
        return new double[] { x/scale, z/scale, y/scale };
    }

    
    private double[] method4(final double lat, final double lon) {
        double a = 6378137;
        double e = 8.1819190842622e-2;

        double N = a / Math.sqrt(1 - Math.pow(e, 2) * Math.pow(Math.sin(lat), 2));

        double alt = 50;
        
        double x = (N+alt) * Math.cos(lat) * Math.cos(lon);
        double y = (N+alt) * Math.cos(lat) * Math.sin(lon);
        double z = ((1-Math.pow(e, 2)) * N + alt) * Math.sin(lat);
        
        return new double[] { x/scale, y/scale, z/scale };
    }
    
    
    private void drawLon() {
        final int slices = 36;
        final int layers = 18;
        
        double lat = -90;
        for (int layer=0;layer<layers;layer++) {
            VBO vbo = new VBO();
            IntBuffer ib = BufferUtils.createIntBuffer(2);
            ARBVertexBufferObject.glGenBuffersARB(ib);
            vbo.vHandle = ib.get(0);
            vbo.cHandle = ib.get(1);
            vbo.vertexCount = (slices+1);
            vbo.vertexFB = BufferUtils.createFloatBuffer(vbo.vertexCount*3);
            vbo.colorFB = BufferUtils.createFloatBuffer(vbo.vertexCount*3);
            
            double lon = -180;
            for (int slice=0; slice<slices+1; slice++) {

                double[] result = method2(lat, lon);

                vbo.vertexFB.put((float) result[0]);
                vbo.vertexFB.put((float) result[1]);
                vbo.vertexFB.put((float) result[2]);

                vbo.colorFB.put(0f);
                vbo.colorFB.put(0f);
                vbo.colorFB.put(1f);
                
                lon += (360/slices);
            }
            
            vbo.vertexFB.flip();
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.vHandle);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.vertexFB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
            
            vbo.colorFB.flip();
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.cHandle);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.colorFB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
            
            sceneVBOs.add(vbo);
            
            lat += (180/layers);
        }
    }
    
    private void drawLat() {
        final int slices = 36;
        final int layers = 18;
        
        
        double lon = -180;
        for (int slice=0; slice<slices; slice++) {
       
            VBO vbo = new VBO();
            IntBuffer ib = BufferUtils.createIntBuffer(2);
            ARBVertexBufferObject.glGenBuffersARB(ib);
            vbo.vHandle = ib.get(0);
            vbo.cHandle = ib.get(1);
            vbo.vertexCount = (layers+1);

            vbo.vertexFB = BufferUtils.createFloatBuffer(vbo.vertexCount*3);
            vbo.colorFB = BufferUtils.createFloatBuffer(vbo.vertexCount*3);
            
            
            double lat = -90;
            for (int layer=0;layer<layers+1;layer++) {
                double[] result = method2(lat, lon);

                vbo.vertexFB.put((float) result[0]);
                vbo.vertexFB.put((float) result[1]);
                vbo.vertexFB.put((float) result[2]);

                vbo.colorFB.put(1f);
                vbo.colorFB.put(1f);
                vbo.colorFB.put(0f);
                
                lat += (180/layers);
            }
            
            vbo.vertexFB.flip();
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.vHandle);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.vertexFB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
            
            vbo.colorFB.flip();
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.cHandle);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.colorFB, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
            
            sceneVBOs.add(vbo);
            
            lon += (360/slices);
            
        }
    }
    
    
    @Override
    public void init() {
       drawLat();
       drawLon();
    }
    
    public void render() {
        for (VBO vbo : sceneVBOs) {
            vbo.colorFB = null;
            vbo.vertexFB = null;
            
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.vHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo.cHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            
            glDrawArrays(GL_LINE_STRIP, 0, vbo.vertexCount);
        }
    }

}
