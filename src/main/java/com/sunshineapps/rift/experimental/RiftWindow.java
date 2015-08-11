package com.sunshineapps.rift.experimental;

import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.GL_SPOT_CUTOFF;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glLightf;
import static org.lwjgl.opengl.GL11.glLightfv;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
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
import static org.lwjgl.ovr.OVR.ovrHmd_GetFloat;
import static org.lwjgl.ovr.OVR.ovrHmd_GetFovTextureSize;
import static org.lwjgl.ovr.OVR.ovrHmd_GetFrameTiming;
import static org.lwjgl.ovr.OVR.ovrHmd_GetRenderDesc;
import static org.lwjgl.ovr.OVR.ovrHmd_GetTrackingState;
import static org.lwjgl.ovr.OVR.ovrHmd_RecenterPose;
import static org.lwjgl.ovr.OVR.ovrHmd_SetInt;
import static org.lwjgl.ovr.OVR.ovrHmd_SubmitFrame;
import static org.lwjgl.ovr.OVR.ovrLayerFlag_TextureOriginAtBottomLeft;
import static org.lwjgl.ovr.OVR.ovrLayerType_EyeFov;
import static org.lwjgl.ovr.OVR.ovrPerfHud_LatencyTiming;
import static org.lwjgl.ovr.OVR.ovrPerfHud_Off;
import static org.lwjgl.ovr.OVR.ovrPerfHud_RenderTiming;
import static org.lwjgl.ovr.OVR.ovrTrackingCap_MagYawCorrection;
import static org.lwjgl.ovr.OVR.ovrTrackingCap_Orientation;
import static org.lwjgl.ovr.OVR.ovrTrackingCap_Position;
import static org.lwjgl.ovr.OVR.ovr_GetVersionString;
import static org.lwjgl.ovr.OVR.ovr_Initialize;
import static org.lwjgl.ovr.OVR.ovr_Shutdown;
import static org.lwjgl.ovr.OVRErrorCode.ovrSuccess;
import static org.lwjgl.ovr.OVRErrorCode.ovrSuccess_NotVisible;
import static org.lwjgl.ovr.OVRKeys.OVR_KEY_EYE_HEIGHT;
import static org.lwjgl.system.MemoryUtil.memDecodeASCII;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL;
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
import org.lwjgl.ovr.OVRRecti;
import org.lwjgl.ovr.OVRSizei;
import org.lwjgl.ovr.OVRSwapTextureSet;
import org.lwjgl.ovr.OVRTrackingState;
import org.lwjgl.ovr.OVRUtil;
import org.lwjgl.ovr.OVRVector3f;
import org.lwjgl.system.MemoryUtil;

import com.sunshineapps.riftexample.thirdparty.FrameBuffer;

public final class RiftWindow {
    private final ClientCallback callback;
    public static final boolean displayMirror = true;
    
    private long hmd;
    private OVRHmdDesc hmdDesc;
    private int resolutionW;                            //pixels rift
    private int resolutionH;
    private float canvasRatio;
    private final int[] eyeRenderOrder = new int[2];   //performance tip from developer guide for screens like DK2
    private final OVRMatrix4f[] projections = new OVRMatrix4f[2];
    private final OVRFovPort fovPorts[] = new OVRFovPort[2];
    private final OVRPosef eyePoses[] = new OVRPosef[2];
    private final OVREyeRenderDesc eyeRenderDesc[] = new OVREyeRenderDesc[2];
    //private final OVRSwapTextureSet textureSet[] = new OVRSwapTextureSet[2];
    private OVRSwapTextureSet textureSetOne;
    private int texturesPerEyeCount;
    private OVRGLTexture textures[][];        //[eye][texturesPerEye]
    private PointerBuffer layers;
    private OVRLayerEyeFov layer0;
    private int textureW;
    private int textureH;
    private Vector3f playerEyePos;
    private int currentTPEIndex;
    private int perfHUD;
    
    //OpenGL
    private FrameBuffer fbuffers[][];       //[eye][texturesPerEye]


    public RiftWindow(final ClientCallback client) {
        callback = client;

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
        System.out.println("OVR SDK " + ovr_GetVersionString());
        
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
        canvasRatio = (float)resolutionW/resolutionH;
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
        playerEyePos = new Vector3f(0.0f, -ovrHmd_GetFloat(hmd, OVR_KEY_EYE_HEIGHT, 1.65f), 0.0f);

        // step 4 - tracking
        System.out.println("step 4 - tracking");
        if (!usingDebug && ovrHmd_ConfigureTracking(hmd, ovrTrackingCap_Orientation | ovrTrackingCap_MagYawCorrection | ovrTrackingCap_Position, 0) != ovrSuccess) {
            throw new IllegalStateException("Unable to start the sensor");
        }
        
        // step 5 - projections
        System.out.println("step 5 - projections");
        for (int eye = 0; eye < 2; eye++) {
            projections[eye] = new OVRMatrix4f();
            OVRUtil.ovrMatrix4f_Projection(fovPorts[eye].buffer(), 0.5f, 500f, OVRUtil.ovrProjection_RightHanded, projections[eye].buffer());
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

        
    }



    public void init() {
        // step 9 - init
        System.out.println("step 9 - init");
        
        GL.createCapabilities();
        glClearColor(0.42f, 0.67f, 0.87f, 1.0f);
        
        glEnable(GL_CULL_FACE);
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
     //   glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
    //    glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Texture sizes
        float pixelsPerDisplayPixel = 1.0f;
        
        OVRSizei leftTextureSize = new OVRSizei();
        ovrHmd_GetFovTextureSize(hmd, ovrEye_Left, fovPorts[ovrEye_Left].buffer(), pixelsPerDisplayPixel, leftTextureSize.buffer());
        System.out.println("leftTextureSize W="+leftTextureSize.getW() +", H="+ leftTextureSize.getH());
        
        OVRSizei rightTextureSize = new OVRSizei();
        ovrHmd_GetFovTextureSize(hmd, ovrEye_Right, fovPorts[ovrEye_Right].buffer(), pixelsPerDisplayPixel, rightTextureSize.buffer());
        System.out.println("rightTextureSize W="+rightTextureSize.getW() +", H="+ rightTextureSize.getH());
        
        textureW = (leftTextureSize.getW() + rightTextureSize.getW()) / 2;
        textureH = Math.max(leftTextureSize.getH(), rightTextureSize.getH());
        System.out.println("request textureW=" + textureW + ", textureH=" + textureH);
        
        // TextureSets - one!
        PointerBuffer textureSetPB = BufferUtils.createPointerBuffer(1);
        if (OVRGL.ovrHmd_CreateSwapTextureSetGL(hmd, GL_RGBA, textureW*2, textureH, textureSetPB) != ovrSuccess) {      // twice width for single texture
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
            viewport[eye].setPosX(0);
            viewport[eye].setPosY(0);
            viewport[eye].setSizeW(textures[eye][0].getOGLHeaderTextureSizeW());
            viewport[eye].setSizeH(textures[eye][0].getOGLHeaderTextureSizeH());
        }
        
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
        
        currentTPEIndex = -1;

    }
    
    public int getMirrorTexture(final int windowW, final int windowH) {
        if (!displayMirror) {
            return 0;
        }
            // Create mirror texture and an FBO used to copy mirror texture to back buffer
            PointerBuffer outMirrorTexture = BufferUtils.createPointerBuffer(1);
            OVRGL.ovrHmd_CreateMirrorTextureGL(hmd, GL_RGBA, windowW, windowH, outMirrorTexture);
            long hMT = outMirrorTexture.get();
            
            OVRGLTexture texture = new OVRGLTexture(MemoryUtil.memByteBuffer(hMT, OVRGLTexture.SIZEOF));
            int mirrorTextureId = texture.getOGLTexId();
            
            // Configure the mirror read buffer
            int mirrorFBId = ARBFramebufferObject.glGenFramebuffers();
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, mirrorFBId);
            ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_READ_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mirrorTextureId, 0);
            ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, ARBFramebufferObject.GL_DEPTH_ATTACHMENT, ARBFramebufferObject.GL_RENDERBUFFER, 0);
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, 0);
            
        
        return mirrorFBId;
    }

    public void render() {
        
        Matrix4f mat = new Matrix4f();
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
  
        Matrix4f matP = new Matrix4f();
        FloatBuffer fbP = BufferUtils.createFloatBuffer(16);

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
//            System.out.println("eye="+eye+", currentTPEIndex="+currentTPEIndex);
            textureSetOne.setCurrentIndex(currentTPEIndex);
//            textureSet[eye].setCurrentIndex(currentTPEIndex);
          
            fbuffers[currentTPEIndex][0].activate();
         
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

           
            glViewport(0, 0, textureW*2, textureH);
            
            //Projection Matrix
            glMatrixMode(GL_PROJECTION);
            Matrix4f proj = new Matrix4f(projections[eye].buffer().asFloatBuffer()).transpose();
            matP.set(proj).get(fbP);
            glLoadMatrixf(fbP);

            //ModelView Matrix
            glMatrixMode(GL_MODELVIEW);
            mat.identity();

            Vector3f offsetPosition = new Vector3f(eyeRenderDesc[eye].getHmdToEyeViewOffsetX(), eyeRenderDesc[eye].getHmdToEyeViewOffsetY(), eyeRenderDesc[eye].getHmdToEyeViewOffsetZ());
            mat.translate(offsetPosition);
            
            Quaternionf orientation = new Quaternionf(eyePose.getOrientationX(), eyePose.getOrientationY(), eyePose.getOrientationZ(), eyePose.getOrientationW());
            orientation.invert();
            mat.rotate(orientation);

            Vector3f position = new Vector3f(-eyePose.getPositionX(), -eyePose.getPositionY(), -eyePose.getPositionZ());
            mat.translate(position);
         
            mat.translate(playerEyePos);    //back to 'floor' height
            

            
            callback.drawScene(mat);

            mat.get(fb);
            glLoadMatrixf(fb);            
            
            //System.out.println(server.lon+","+server.lat);
            
            //fbuffers[eye][currentTPEIndex].deactivate();      //TODO we do this once outside of loop for now...
     //       System.out.println("1.5 error: "+glGetError());
        }
        ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        //System.out.println("\n================= SUBMIT\n");
        int result = ovrHmd_SubmitFrame(hmd, 0, null, layers);
        if (result == ovrSuccess_NotVisible) {
            System.out.println("TODO not vis!!");
        } else if (result != ovrSuccess) {
            System.out.println("TODO failed submit");
        }
    }

    public int getPixelWidth() {
        return resolutionW;
    }
    
    public float getCanvasRatio() {
        return canvasRatio;
    }

    public void shutdown() {
        if (textureSetOne != null) {
            ovrHmd_DestroySwapTextureSet(hmd, textureSetOne.buffer());
        }
        ovrHmd_Destroy(hmd);
        ovr_Shutdown();
    }

    public void toggleHUD() {
        perfHUD++;
        if (perfHUD == 1) {
            ovrHmd_SetInt(hmd, "PerfHudMode", (int)ovrPerfHud_LatencyTiming);
        } else if (perfHUD == 2) {
            ovrHmd_SetInt(hmd, "PerfHudMode", (int)ovrPerfHud_RenderTiming);
        } else {
            perfHUD = 0;
            ovrHmd_SetInt(hmd, "PerfHudMode", (int)ovrPerfHud_Off);
        }
    }
}
