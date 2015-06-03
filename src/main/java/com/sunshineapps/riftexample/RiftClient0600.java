package com.sunshineapps.riftexample;


import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DECAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.GL_SPOT_CUTOFF;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLightf;
import static org.lwjgl.opengl.GL11.glLightfv;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMaterialfv;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.ovr.OVR.ovrEye_Count;
import static org.lwjgl.ovr.OVR.ovrEye_Left;
import static org.lwjgl.ovr.OVR.ovrEye_Right;
import static org.lwjgl.ovr.OVR.ovrHmd_ConfigureTracking;
import static org.lwjgl.ovr.OVR.ovrHmd_Create;
import static org.lwjgl.ovr.OVR.ovrHmd_CreateDebug;
import static org.lwjgl.ovr.OVR.ovrHmd_DK2;
import static org.lwjgl.ovr.OVR.ovrHmd_Destroy;
import static org.lwjgl.ovr.OVR.ovrHmd_DestroySwapTextureSet;
import static org.lwjgl.ovr.OVR.ovrHmd_Detect;
import static org.lwjgl.ovr.OVR.ovrHmd_GetFovTextureSize;
import static org.lwjgl.ovr.OVR.ovrHmd_GetFrameTiming;
import static org.lwjgl.ovr.OVR.ovrHmd_GetRenderDesc;
import static org.lwjgl.ovr.OVR.ovrHmd_GetTrackingState;
import static org.lwjgl.ovr.OVR.ovrHmd_RecenterPose;
import static org.lwjgl.ovr.OVR.ovrHmd_SubmitFrame;
import static org.lwjgl.ovr.OVR.ovrLayerFlag_TextureOriginAtBottomLeft;
import static org.lwjgl.ovr.OVR.ovrLayerType_EyeFov;
import static org.lwjgl.ovr.OVR.ovrSuccess;
import static org.lwjgl.ovr.OVR.ovrSuccess_NotVisible;
import static org.lwjgl.ovr.OVR.ovrTrackingCap_MagYawCorrection;
import static org.lwjgl.ovr.OVR.ovrTrackingCap_Orientation;
import static org.lwjgl.ovr.OVR.ovrTrackingCap_Position;
import static org.lwjgl.ovr.OVR.ovr_GetVersionString;
import static org.lwjgl.ovr.OVR.ovr_Initialize;
import static org.lwjgl.ovr.OVR.ovr_Shutdown;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memDecodeASCII;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.ovr.OVREyeRenderDesc;
import org.lwjgl.ovr.OVRFovPort;
import org.lwjgl.ovr.OVRFrameTiming;
import org.lwjgl.ovr.OVRGL;
import org.lwjgl.ovr.OVRGLTexture;
import org.lwjgl.ovr.OVRHmdDesc;
import org.lwjgl.ovr.OVRInitParams;
import org.lwjgl.ovr.OVRLayerEyeFov;
import org.lwjgl.ovr.OVRLogCallback;
import org.lwjgl.ovr.OVRMatrix4f;
import org.lwjgl.ovr.OVRPosef;
import org.lwjgl.ovr.OVRQuatf;
import org.lwjgl.ovr.OVRRecti;
import org.lwjgl.ovr.OVRSizei;
import org.lwjgl.ovr.OVRSwapTextureSet;
import org.lwjgl.ovr.OVRTrackingState;
import org.lwjgl.ovr.OVRUtil;
import org.lwjgl.ovr.OVRVector3f;
import org.lwjgl.system.MemoryUtil;
import org.saintandreas.math.Matrix4f;
import org.saintandreas.math.Quaternion;
import org.saintandreas.math.Vector3f;

import com.sunshineapps.riftexample.thirdparty.FrameBuffer;
import com.sunshineapps.riftexample.thirdparty.MatrixStack;
import com.sunshineapps.riftexample.thirdparty.Texture;
import com.sunshineapps.riftexample.thirdparty.TextureLoader;

public final class RiftClient0600 {
    
    //OVR
    private final float eyeHeight = 1.7f;       // OvrLibrary.OVR_DEFAULT_EYE_HEIGHT;  <- cant find this
    private final float windowScale = 0.5f;
    private long hmd;
    private OVRHmdDesc hmdDesc;
    private int resolutionW;        //pixels rift
    private int resolutionH;
    private final int[] eyeRenderOrder = new int[2];   //performance tip from developer guide for screens like DK2
    private final OVRMatrix4f[] projections = new OVRMatrix4f[2];
    private final OVRFovPort fovPorts[] = new OVRFovPort[2];
    private final OVRPosef eyePoses[] = new OVRPosef[2];
    //private final OVRSwapTextureSet textureSet[] = new OVRSwapTextureSet[2];
    private  OVRSwapTextureSet textureSetOne;
    private final OVREyeRenderDesc eyeRenderDesc[] = new OVREyeRenderDesc[2];
    
    private int texturesPerEyeCount;
    private OVRGLTexture textures[][];        //[eye][texturesPerEye]
    private PointerBuffer layers;
    private OVRLayerEyeFov layer0;
   
    //OpenGL
    private final FloatBuffer projectionDFB[] = new FloatBuffer[2];
    {
        for (int eye = 0; eye < 2; eye++) {
            projectionDFB[eye] = BufferUtils.createFloatBuffer(4*4); 
        }
    }
    private final FloatBuffer modelviewDFB = BufferUtils.createFloatBuffer(4*4);
    private FrameBuffer fbuffers[][];       //[eye][texturesPerEye]
    private Texture floorTexture;
    private Texture wallTexture;
    private Texture ceilingTexture;
    private float roomSize = 6.0f;
    private float roomHeight = 2.6f*2;
    
    //mirror
    private int mirrorFBId;
    private int mirrorTextureId;
    
    //GLFW
    private long window;
    private int windowW;
    private int windowH;
    private GLFWErrorCallback errorfun;
    private GLFWKeyCallback keyfun;  
    
    //FPS
    private final int fpsReportingPeriodSeconds = 5;
    private final ScheduledExecutorService fpsCounter = Executors.newSingleThreadScheduledExecutor();
    private final AtomicInteger frames = new AtomicInteger(0);
    private final AtomicInteger fps = new AtomicInteger(0);
    private Runnable fpsJob = new Runnable() {
        public void run() {
            int frameCount = frames.getAndSet(0);
            fps.set(frameCount/fpsReportingPeriodSeconds);
            frames.addAndGet(frameCount-(fps.get()*fpsReportingPeriodSeconds));
            System.out.println(frameCount+" frames in "+fpsReportingPeriodSeconds+"s. "+fps.get()+"fps");
        }
    };
    
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
    
    private void run() {
        System.out.println("Java "+System.getProperty("java.version"));
        
        // step 1 - hmd init
        System.out.println("step 1 - hmd init");
        OVRLogCallback callback = new OVRLogCallback() {
            @Override
            public void invoke(int level, long message) {
                System.out.println("LibOVR [" + level + "] " + memDecodeASCII(message));
            }
        };
        OVRInitParams initParams = new OVRInitParams();
        initParams.setLogCallback(callback.getPointer());
       // initParams.setFlags(ovrInit_Debug);
        if  (ovr_Initialize(initParams.buffer()) != ovrSuccess) {
            System.out.println("init failed");
        }
        System.out.println("ovr_GetVersionString = " + ovr_GetVersionString());
        
        // step 2 - hmd create
        System.out.println("step 2 - hmd create");
        PointerBuffer pHmd = BufferUtils.createPointerBuffer(1);
        boolean usingDebug = false;
        if (ovrHmd_Create(0, pHmd) != ovrSuccess) {
            System.out.println("create failed, try debug");
            if (ovrHmd_CreateDebug(ovrHmd_DK2, pHmd) != ovrSuccess) {
                System.out.println("debug failed, quit");
                return;
            }
            usingDebug = true;
        }

        // step 3 - hmdDesc queries
        System.out.println("step 3 - hmdDesc queries");
        hmd = pHmd.get(0);
        hmdDesc = new OVRHmdDesc(MemoryUtil.memByteBuffer(hmd, OVRHmdDesc.SIZEOF));
        resolutionW = hmdDesc.getResolutionW();
        resolutionH = hmdDesc.getResolutionH();
        System.out.println("resolution W=" + resolutionW + ", H=" + resolutionH);
        
        // eye order
        OVRSizei pairInts = new OVRSizei();             //bit naughty using this, just in case order of w and h changes
        hmdDesc.getEyeRenderOrder(pairInts.buffer());
        eyeRenderOrder[0] = pairInts.getW();
        eyeRenderOrder[1] = pairInts.getH();
        
        // FOV
        for (int eye = 0; eye < 2; eye++) {
            fovPorts[eye] = new OVRFovPort();
            hmdDesc.getDefaultEyeFov(fovPorts[eye].buffer(), eye);
            System.out.println("eye "+eye+" = "+fovPorts[eye].getUpTan() +", "+ fovPorts[eye].getDownTan()+", "+fovPorts[eye].getLeftTan()+", "+fovPorts[eye].getRightTan());
        }

        // step 4 - tracking
        System.out.println("step 4 - tracking");
        if (!usingDebug && ovrHmd_ConfigureTracking(hmd, ovrTrackingCap_Orientation | ovrTrackingCap_MagYawCorrection | ovrTrackingCap_Position, 0) != ovrSuccess) {
            throw new IllegalStateException("Unable to start the sensor");
        }
        
        // step 5 - projections
        System.out.println("step 5 - projections");
        for (int eye = 0; eye < 2; eye++) {
            projections[eye] = new OVRMatrix4f();
            OVRUtil.ovrMatrix4f_Projection(fovPorts[eye].buffer(), 0.1f, 1000000f, OVRUtil.ovrProjection_RightHanded, projections[eye].buffer());
        }
        
        // step 6 - render desc
        System.out.println("step 6 - render desc");
        for (int eye = 0; eye < 2; eye++) {
            eyeRenderDesc[eye] = new OVREyeRenderDesc();
            ovrHmd_GetRenderDesc(hmd, eye,  fovPorts[eye].buffer(), eyeRenderDesc[eye].buffer());
            System.out.println("ipd eye "+eye+" = "+eyeRenderDesc[eye].getHmdToEyeViewOffsetX());
        }
        
        // step 7 - recenter
        System.out.println("step 7 - recenter");
        ovrHmd_RecenterPose(hmd);

        // display
        try {
            createWindow();
            init();
            loop();
            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            fpsCounter.shutdown();
            keyfun.release();
            errorfun.release();
        } 
        if (textureSetOne != null) {
            ovrHmd_DestroySwapTextureSet(hmd, textureSetOne.buffer());
        }

        ovrHmd_Destroy(hmd);
        ovr_Shutdown();
    }
    
    private void createWindow() {
        // step 8 - opengl createWindow
        System.out.println("step 8 - createWindow - " +Sys.JNI_LIBRARY_NAME+"-"+Sys.getVersion());
        
        errorfun = errorCallbackPrint(System.err);
        glfwSetErrorCallback(errorfun);
        
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        windowW = (int)(resolutionW*windowScale); 
        windowH = (int)(resolutionH*windowScale);
        window = glfwCreateWindow(windowW, windowH, "Hello VR World!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        keyfun = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( action != GLFW_RELEASE) {
                    return;
                }
                switch (key) {
                    case GLFW_KEY_ESCAPE:
                        glfwSetWindowShouldClose(window, GL_TRUE);
                        break;
                    case GLFW_KEY_R:
                        ovrHmd_RecenterPose(hmd);
                        break;
                }
            }
        };
        glfwSetKeyCallback(window, keyfun);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);              //TODO swap is handled on rift, so this is just our mirror window now, actually it does affect FPS when on rift <-
        glfwShowWindow(window);
//        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);        //only after display create?
   //     glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);        //only after display create?`
        //glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);        //hide mouse
    }
    
    private void init() {
        // step 9 - init
        System.out.println("step 9 - init");

        GLContext.createFromCurrent();
        glClearColor(0.42f, 0.67f, 0.87f, 1.0f);
        
        glEnable(GL_CULL_FACE);
     //   glDisable(GL_CULL_FACE);
        glFrontFace(GL_CCW);
        
        // Lighting
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);

        FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(new float[]{0.5f, 0.0f, 1.0f, 0.0001f});
        lightPos.rewind();
        
        FloatBuffer noAmbient =  BufferUtils.createFloatBuffer(4);
        noAmbient.put(new float[]{0.2f, 0.2f, 0.2f, 1.0f});
        noAmbient.rewind();
        
        FloatBuffer diffuse =  BufferUtils.createFloatBuffer(4);
        diffuse.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        diffuse.rewind();
        
        FloatBuffer spec =  BufferUtils.createFloatBuffer(4);
        spec.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        spec.rewind();
        
        glLightfv(GL_LIGHT0, GL_AMBIENT, noAmbient);
        glLightfv(GL_LIGHT0, GL_SPECULAR, spec);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
        glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 45.0f);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Texture sizes
        float pixelsPerDisplayPixel = 1.0f;
        
        OVRSizei leftTextureSize = new OVRSizei();
        ovrHmd_GetFovTextureSize(hmd, ovrEye_Left, fovPorts[ovrEye_Left].buffer(), pixelsPerDisplayPixel, leftTextureSize.buffer());
        System.out.println("leftTextureSize W="+leftTextureSize.getW() +", H="+ leftTextureSize.getH());
        
        OVRSizei rightTextureSize = new OVRSizei();
        ovrHmd_GetFovTextureSize(hmd, ovrEye_Right, fovPorts[ovrEye_Right].buffer(), pixelsPerDisplayPixel, rightTextureSize.buffer());
        System.out.println("rightTextureSize W="+rightTextureSize.getW() +", H="+ rightTextureSize.getH());
        
        int textureW = (leftTextureSize.getW() + rightTextureSize.getW()) / 2;
        int textureH = Math.max(leftTextureSize.getH(), rightTextureSize.getH());
        System.out.println("request textureW=" + textureW + ", textureH=" + textureH);
        
        // TextureSets - one!
        PointerBuffer textureSetPB = BufferUtils.createPointerBuffer(1);
        if (OVRGL.ovrHmd_CreateSwapTextureSetGL(hmd, GL_RGBA, textureW*2, textureH, textureSetPB) != ovrSuccess) {
            throw new IllegalStateException("Failed to create Swap Texture Set");
        }
        long hts = textureSetPB.get(0);
        textureSetOne = new OVRSwapTextureSet(MemoryUtil.memByteBuffer(hts, OVRSwapTextureSet.SIZEOF));
        texturesPerEyeCount = textureSetOne.getTextureCount();
        System.out.println("texturesPerEyeCount="+texturesPerEyeCount);

        // use same texture for both eye, see Viewport below
        PointerBuffer colorTexturePB = BufferUtils.createPointerBuffer(2);
        colorTexturePB.put(0, textureSetOne.getPointer());
        colorTexturePB.put(1, textureSetOne.getPointer());

        // create FrameBuffers for Oculus SDK generated textures
        textures = new OVRGLTexture[2][1];
        fbuffers = new FrameBuffer[2][1];
        long hTextures = textureSetOne.getTextures();
        for (int eye = 0; eye < 2; eye++) {
            OVRGLTexture texture = new OVRGLTexture(MemoryUtil.memByteBuffer(hTextures + (eye * OVRGLTexture.SIZEOF), OVRGLTexture.SIZEOF));
            textures[eye][0] = texture;
            System.out.println("textureId="+texture.getOGLTexId()+", W="+texture.getOGLHeaderTextureSizeW()+", "+texture.getOGLHeaderTextureSizeH()+", texturePointer="+texture.getPointer());
            fbuffers[eye][0] = new FrameBuffer(texture.getOGLHeaderTextureSizeW(), texture.getOGLHeaderTextureSizeH(), texture.getOGLTexId());        //Texture size might not be what we asked for
        }

        // eye viewports
        OVRRecti viewport[] = new OVRRecti[2]; //should not matter which texture we measure, but they might be different to what was requested.
        for (int eye = 0; eye < 2; eye++) {
            viewport[eye] = new OVRRecti();
            // viewport[eye].setPosX(0);
            viewport[eye].setPosX(eye * textures[eye][0].getOGLHeaderTextureSizeW());
            viewport[eye].setPosY(0);
            viewport[eye].setSizeW(textures[eye][0].getOGLHeaderTextureSizeW());
            viewport[eye].setSizeH(textures[eye][0].getOGLHeaderTextureSizeH());
        }
        glViewport(0, 0, textures[0][0].getOGLHeaderTextureSizeW(), textures[0][0].getOGLHeaderTextureSizeH());       //TODO required???
        
        //Layers
        layer0 = new OVRLayerEyeFov();
        layer0.setHeaderType(ovrLayerType_EyeFov);
        layer0.setHeaderFlags(ovrLayerFlag_TextureOriginAtBottomLeft);
        for (int eye = 0; eye < 2; eye++) {
            layer0.setColorTexture(colorTexturePB);
            layer0.setViewport(viewport[eye].buffer(), eye);
            layer0.setFov(fovPorts[eye].buffer(), eye);
            // we update pose only when we have it in the render loop
        }
        layers = BufferUtils.createPointerBuffer(1);
        layers.put(0, layer0.getPointer());
        

        // Create mirror texture and an FBO used to copy mirror texture to back buffer
        PointerBuffer outMirrorTexture = BufferUtils.createPointerBuffer(1);
        OVRGL.ovrHmd_CreateMirrorTextureGL(hmd, GL_RGBA, windowW, windowH, outMirrorTexture);
        long hMT = outMirrorTexture.get();
        
        OVRGLTexture texture = new OVRGLTexture(MemoryUtil.memByteBuffer(hMT, OVRGLTexture.SIZEOF));
        mirrorTextureId = texture.getOGLTexId();
        
        // Configure the mirror read buffer
        mirrorFBId = ARBFramebufferObject.glGenFramebuffers();
        ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, mirrorFBId);
        ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_READ_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mirrorTextureId, 0);
        ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, ARBFramebufferObject.GL_DEPTH_ATTACHMENT, ARBFramebufferObject.GL_RENDERBUFFER, 0);
        ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, 0);
        

        // scene prep
        TextureLoader loader = new TextureLoader();
        try {
            floorTexture = loader.getTexture("floor512512.png");
            wallTexture = loader.getTexture("panel512512.png");
            ceilingTexture = loader.getTexture("ceiling512512.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //fps
        fpsCounter.scheduleAtFixedRate(fpsJob, 0, fpsReportingPeriodSeconds, TimeUnit.SECONDS); 
    }
    
    float rotate = 0.0f;
    private void loop() {
        // step 10 - animator loop
        System.out.println("step 10 - animator loop");
        int currentTPEIndex = -1;
        while (glfwWindowShouldClose(window) == GL_FALSE) {     //TODO make this independent of glfw 
            //TODO move object creation out of loop!
            OVRFrameTiming ftiming = new OVRFrameTiming(); 
            ovrHmd_GetFrameTiming(hmd, 0, ftiming.buffer());
            OVRTrackingState hmdState = new OVRTrackingState();
            ovrHmd_GetTrackingState(hmd, ftiming.getDisplayMidpointSeconds(), hmdState.buffer());
 
            //get head pose
            OVRPosef headPose = new OVRPosef();
            hmdState.getHeadPoseThePose(headPose.buffer());

            //build view offsets struct
            ByteBuffer hmdToEyeViewOffset = BufferUtils.createByteBuffer(2 * OVRVector3f.SIZEOF);
            eyeRenderDesc[ovrEye_Left].getHmdToEyeViewOffset(hmdToEyeViewOffset);
            hmdToEyeViewOffset.position(OVRVector3f.SIZEOF);
            eyeRenderDesc[ovrEye_Right].getHmdToEyeViewOffset(hmdToEyeViewOffset);
            hmdToEyeViewOffset.position(0);
            
            //calc eye poses
            ByteBuffer outEyePoses = BufferUtils.createByteBuffer(OVRPosef.SIZEOF * 2);
            OVRUtil.ovr_CalcEyePoses(headPose.buffer(), hmdToEyeViewOffset, outEyePoses);
            
           //Retrieve the eye poses
            outEyePoses.limit(OVRPosef.SIZEOF);
            eyePoses[ovrEye_Left] = new OVRPosef(outEyePoses.slice().order(outEyePoses.order()));
            outEyePoses.position(OVRPosef.SIZEOF);
            outEyePoses.limit(2 * OVRPosef.SIZEOF);
            eyePoses[ovrEye_Right] = new OVRPosef(outEyePoses.slice().order(outEyePoses.order()));

            currentTPEIndex += 1;
            currentTPEIndex %= texturesPerEyeCount;
            for (int eyeIndex = 0; eyeIndex < ovrEye_Count; eyeIndex++) {
                //int eye = eyeRenderOrder[eyeIndex];       //TODO optimization once stable
                int eye = eyeIndex;
                OVRPosef eyePose = eyePoses[eye];
                layer0.setRenderPose(eyePose.buffer(), eye);
//                System.out.println("eye="+eye+", currentTPEIndex="+currentTPEIndex);
                textureSetOne.setCurrentIndex(currentTPEIndex);
//                textureSet[eye].setCurrentIndex(currentTPEIndex);
              
                fbuffers[currentTPEIndex][0].activate();
             
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glLoadMatrixf(projections[eye].buffer());

                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                Vector3f center = Vector3f.UNIT_Y.mult(eyeHeight);
                Vector3f eyeLoc = new Vector3f(eyeRenderDesc[eye].getHmdToEyeViewOffsetX(), eyeHeight, 0.0f);
                MatrixStack.MODELVIEW.set(Matrix4f.lookat(eyeLoc, center, Vector3f.UNIT_Y));
                
                MatrixStack mv = MatrixStack.MODELVIEW;
                mv.push();
                {
                    mv.preTranslate(new Vector3f(eyePose.getPositionX(), eyePose.getPositionY(), eyePose.getPositionZ()).mult(-1));
                    mv.preRotate(new Quaternion(eyePose.getOrientationX(), eyePose.getOrientationY(), eyePose.getOrientationZ(), eyePose.getOrientationW()).inverse());
                    mv.translate(new Vector3f(0, eyeHeight, 0));
                    modelviewDFB.clear();
                    MatrixStack.MODELVIEW.top().fillFloatBuffer(modelviewDFB, true);
                    modelviewDFB.rewind();
                    glLoadMatrixf(modelviewDFB);
                
                    // tiles on floor
                    glEnable(GL_TEXTURE_2D);

                    floorTexture.bind();
                    glTranslatef(0.0f, -eyeHeight, 0.0f);       //TODO eyeheight must be determinable from somewhere
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

                    glTranslatef(0.0f, eyeHeight, 0.0f);
                    glDisable(GL_TEXTURE_2D);
                }
                mv.pop();
                //fbuffers[eye][currentTPEIndex].deactivate();      //TODO we do this once outside of loop for now...
         //       System.out.println("1.5 error: "+glGetError());
            }
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
            glfwPollEvents();
            
            //System.out.println("\n================= SUBMIT\n");
            int result = ovrHmd_SubmitFrame(hmd, 0, null, layers);
            if (result == ovrSuccess_NotVisible) {
                System.out.println("TODO not vis!!");
            } else if (result != ovrSuccess) {
                System.out.println("TODO failed submit");
            }
                
                
            // Blit mirror texture to back buffer
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, mirrorFBId);
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, 0);
            // GLint w = mirrorTexture->OGL.Header.TextureSize.w;
            // GLint h = mirrorTexture->OGL.Header.TextureSize.h;
            ARBFramebufferObject.glBlitFramebuffer(0, windowH, windowW, 0, 0, 0, windowW, windowH, GL_COLOR_BUFFER_BIT, GL_NEAREST);
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, 0);
            glfwSwapBuffers(window);

//            System.out.println("5 error: "+glGetError());
            frames.incrementAndGet();
        }
    }

    public Matrix4f toMatrix4f(OVRMatrix4f m) {
        return new org.saintandreas.math.Matrix4f(m.buffer().asFloatBuffer()).transpose();
    }

    public static void main(String[] args) {
        new RiftClient0600().run();
    }
}