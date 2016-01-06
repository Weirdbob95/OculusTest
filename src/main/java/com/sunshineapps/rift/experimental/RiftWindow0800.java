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
import static org.lwjgl.ovr.OVR.ovrHmd_None;
import static org.lwjgl.ovr.OVR.ovrLayerFlag_TextureOriginAtBottomLeft;
import static org.lwjgl.ovr.OVR.ovrLayerType_EyeFov;
import static org.lwjgl.ovr.OVR.ovrPerfHud_LatencyTiming;
import static org.lwjgl.ovr.OVR.ovrPerfHud_Off;
import static org.lwjgl.ovr.OVR.ovrPerfHud_RenderTiming;
import static org.lwjgl.ovr.OVR.ovrTrue;
import static org.lwjgl.ovr.OVR.ovr_Create;
import static org.lwjgl.ovr.OVR.ovr_Destroy;
import static org.lwjgl.ovr.OVR.ovr_DestroySwapTextureSet;
import static org.lwjgl.ovr.OVR.ovr_GetFloat;
import static org.lwjgl.ovr.OVR.ovr_GetFovTextureSize;
import static org.lwjgl.ovr.OVR.ovr_GetHmdDesc;
import static org.lwjgl.ovr.OVR.ovr_GetPredictedDisplayTime;
import static org.lwjgl.ovr.OVR.ovr_GetRenderDesc;
import static org.lwjgl.ovr.OVR.ovr_GetTrackingState;
import static org.lwjgl.ovr.OVR.ovr_GetVersionString;
import static org.lwjgl.ovr.OVR.ovr_Initialize;
import static org.lwjgl.ovr.OVR.ovr_RecenterPose;
import static org.lwjgl.ovr.OVR.ovr_SetInt;
import static org.lwjgl.ovr.OVR.ovr_Shutdown;
import static org.lwjgl.ovr.OVR.ovr_SubmitFrame;
import static org.lwjgl.ovr.OVRErrorCode.ovrSuccess;
import static org.lwjgl.ovr.OVRErrorCode.ovrSuccess_NotVisible;
import static org.lwjgl.ovr.OVRKeys.OVR_KEY_EYE_HEIGHT;
import static org.lwjgl.ovr.OVRUtil.ovr_Detect;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL;
import org.lwjgl.ovr.OVRDetectResult;
import org.lwjgl.ovr.OVREyeRenderDesc;
import org.lwjgl.ovr.OVRFovPort;
import org.lwjgl.ovr.OVRGL;
import org.lwjgl.ovr.OVRGLTexture;
import org.lwjgl.ovr.OVRGraphicsLuid;
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

public final class RiftWindow0800 {
    private final ClientCallback callback;
    public static final boolean displayMirror = true;
    
    private long hmd;
    private OVRHmdDesc hmdDesc;
    private int resolutionW;                            //pixels rift
    private int resolutionH;
    private float canvasRatio;
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


    public RiftWindow0800(final ClientCallback client) {
        callback = client;
        
        OVRDetectResult detect = OVRDetectResult.calloc();
        ovr_Detect(0, detect);
        System.out.println("OVRDetectResult.IsOculusHMDConnected = " + detect.IsOculusHMDConnected());
        System.out.println("OVRDetectResult.IsOculusServiceRunning = " + detect.IsOculusServiceRunning());
        detect.free();
        if (detect.IsOculusHMDConnected() == false) {
            return;
        }

        // step 1 - hmd init
        System.out.println("step 1 - hmd init");
        OVRLogCallback callback = new OVRLogCallback() {
            @Override
            public void invoke(long userData, int level, long message) {
                System.out.println("LibOVR [" + userData + "] [" +  level + "] " + memDecodeASCII(message));
            }
        };
        OVRInitParams initParams = OVRInitParams.calloc();
        initParams.LogCallback(callback.address());
        //initParams.Flags(ovrInit_Debug);
        if  (ovr_Initialize(initParams) != ovrSuccess) {
            System.out.println("init failed");
        }
        System.out.println("OVR SDK " + ovr_GetVersionString());
        initParams.free();
        
        // step 2 - hmd create
        System.out.println("step 2 - hmd create");
        PointerBuffer pHmd = memAllocPointer(1);
        OVRGraphicsLuid luid = OVRGraphicsLuid.calloc();
        if (ovr_Create(pHmd, luid) != ovrSuccess) {
            System.out.println("create failed, try debug");
            //debug headset is now enabled via the Oculus Configuration util . tools -> Service -> Configure
            return;
        }
        hmd = pHmd.get(0);
        memFree(pHmd);
        luid.free();

        // step 3 - hmdDesc queries
        System.out.println("step 3 - hmdDesc queries");
        hmdDesc = OVRHmdDesc.malloc();
        ovr_GetHmdDesc(hmd, hmdDesc);
        System.out.println("ovr_GetHmdDesc = " + hmdDesc.ManufacturerString() + " " + hmdDesc.ProductNameString() + " " + hmdDesc.SerialNumberString() + " " + hmdDesc.Type());
        if(hmdDesc.Type() == ovrHmd_None) {
            System.out.println("missing init");
            return;
        }
        
        resolutionW = hmdDesc.Resolution().w();
        resolutionH = hmdDesc.Resolution().h();
        canvasRatio = (float)resolutionW/resolutionH;
        System.out.println("resolution W=" + resolutionW + ", H=" + resolutionH);
        if (resolutionW == 0) {
            System.exit(0);
        }
        
        // FOV
        for (int eye = 0; eye < 2; eye++) {
            fovPorts[eye] = hmdDesc.DefaultEyeFov(eye);
            System.out.println("eye "+eye+" = "+fovPorts[eye].UpTan() +", "+ fovPorts[eye].DownTan()+", "+fovPorts[eye].LeftTan()+", "+fovPorts[eye].RightTan());
        }
        playerEyePos = new Vector3f(0.0f, -ovr_GetFloat(hmd, OVR_KEY_EYE_HEIGHT, 1.65f), 0.0f);

        // step 4 - tracking - no longer needed as of 0.8.0.0
        
        // step 5 - projections
        System.out.println("step 5 - projections");
        for (int eye = 0; eye < 2; eye++) {
            projections[eye] = OVRMatrix4f.malloc();
            OVRUtil.ovrMatrix4f_Projection(fovPorts[eye], 0.5f, 500f, OVRUtil.ovrProjection_RightHanded, projections[eye]);
        }
        
        // step 6 - render desc
        System.out.println("step 6 - render desc");
        for (int eye = 0; eye < 2; eye++) {
            eyeRenderDesc[eye] = OVREyeRenderDesc.malloc();
            ovr_GetRenderDesc(hmd, eye,  fovPorts[eye], eyeRenderDesc[eye]);
            System.out.println("ipd eye "+eye+" = "+eyeRenderDesc[eye].HmdToEyeViewOffset().x());
        }
        
        // step 7 - recenter
        System.out.println("step 7 - recenter");
        ovr_RecenterPose(hmd);
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
        
        OVRSizei leftTextureSize = OVRSizei.malloc();
        ovr_GetFovTextureSize(hmd, ovrEye_Left, fovPorts[ovrEye_Left], pixelsPerDisplayPixel, leftTextureSize);
        System.out.println("leftTextureSize W="+leftTextureSize.w() +", H="+ leftTextureSize.h());
        
        OVRSizei rightTextureSize = OVRSizei.malloc();
        ovr_GetFovTextureSize(hmd, ovrEye_Right, fovPorts[ovrEye_Right], pixelsPerDisplayPixel, rightTextureSize);
        System.out.println("rightTextureSize W="+rightTextureSize.w() +", H="+ rightTextureSize.h());
        
        textureW = (leftTextureSize.w() + rightTextureSize.w()) / 2;
        textureH = Math.max(leftTextureSize.h(), rightTextureSize.h());
        System.out.println("request textureW=" + textureW + ", textureH=" + textureH);
        leftTextureSize.free();
        rightTextureSize.free();
        
        // TextureSets - one!
        PointerBuffer textureSetPB = BufferUtils.createPointerBuffer(1);
        if (OVRGL.ovr_CreateSwapTextureSetGL(hmd, GL_RGBA, textureW*2, textureH, textureSetPB) != ovrSuccess) {      // twice width for single texture
            throw new IllegalStateException("Failed to create Swap Texture Set");
        }
        long hts = textureSetPB.get(0);
        textureSetOne = new OVRSwapTextureSet(MemoryUtil.memByteBuffer(hts, OVRSwapTextureSet.SIZEOF));
        texturesPerEyeCount = textureSetOne.TextureCount();
        System.out.println("texturesPerEyeCount="+texturesPerEyeCount);

        // use same texture for both eye, see Viewport below
        PointerBuffer colorTexturePB = BufferUtils.createPointerBuffer(2);
        colorTexturePB.put(0, textureSetOne.address());
        colorTexturePB.put(1, textureSetOne.address());

        // create FrameBuffers for Oculus SDK generated textures
        textures = new OVRGLTexture[2][1];
        fbuffers = new FrameBuffer[2][1];
        long hTextures = textureSetOne.Textures().address();
        for (int eye = 0; eye < 2; eye++) {
            OVRGLTexture texture = new OVRGLTexture(MemoryUtil.memByteBuffer(hTextures + (eye * OVRGLTexture.SIZEOF), OVRGLTexture.SIZEOF));
            textures[eye][0] = texture;
            System.out.println("textureId="+texture.OGL().TexId()+", W="+texture.OGL().Header().TextureSize().w()+", "+texture.OGL().Header().TextureSize().h()+", texturePointer="+texture.address());
            fbuffers[eye][0] = new FrameBuffer(texture.OGL().Header().TextureSize().w(), texture.OGL().Header().TextureSize().h(), texture.OGL().TexId());        //Texture size might not be what we asked for
        }

        // eye viewports
        OVRRecti viewport[] = new OVRRecti[2]; //should not matter which texture we measure, but they might be different to what was requested.
        for (int eye = 0; eye < 2; eye++) {
            viewport[eye] = OVRRecti.calloc();
            viewport[eye].Pos().x(0);
            viewport[eye].Pos().y(0);
            viewport[eye].Size().w(textures[eye][0].OGL().Header().TextureSize().w());
            viewport[eye].Size().h(textures[eye][0].OGL().Header().TextureSize().h());
        }
        
        //Layers
        layer0 = OVRLayerEyeFov.calloc();
        layer0.Header().Type(ovrLayerType_EyeFov);
        layer0.Header().Flags(ovrLayerFlag_TextureOriginAtBottomLeft);
        for (int eye = 0; eye < 2; eye++) {
            layer0.ColorTexture(colorTexturePB);
            layer0.Viewport(eye, viewport[eye]);
            layer0.Fov(eye, fovPorts[eye]);
            
            viewport[eye].free();
            // we update pose only when we have it in the render loop
        }
        layers = BufferUtils.createPointerBuffer(1);
        layers.put(0, layer0);
        
        currentTPEIndex = -1;

    }
    
    public int getMirrorTexture(final int windowW, final int windowH) {
        if (!displayMirror) {
            return 0;
        }
            // Create mirror texture and an FBO used to copy mirror texture to back buffer
            PointerBuffer outMirrorTexture = BufferUtils.createPointerBuffer(1);
            OVRGL.ovr_CreateMirrorTextureGL(hmd, GL_RGBA, windowW, windowH, outMirrorTexture);
            long hMT = outMirrorTexture.get();
            
            OVRGLTexture texture = new OVRGLTexture(MemoryUtil.memByteBuffer(hMT, OVRGLTexture.SIZEOF));
            int mirrorTextureId = texture.OGL().TexId();
            
            // Configure the mirror read buffer
            int mirrorFBId = ARBFramebufferObject.glGenFramebuffers();
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, mirrorFBId);
            ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_READ_FRAMEBUFFER, ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mirrorTextureId, 0);
            ARBFramebufferObject.glFramebufferRenderbuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, ARBFramebufferObject.GL_DEPTH_ATTACHMENT, ARBFramebufferObject.GL_RENDERBUFFER, 0);
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_READ_FRAMEBUFFER, 0);
            
        
        return mirrorFBId;
    }

   //TODO move object creation out of loop!
    public void render() {
        
        Matrix4f mat = new Matrix4f();
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
  
        Matrix4f matP = new Matrix4f();
        FloatBuffer fbP = BufferUtils.createFloatBuffer(16);

        
        double ftiming = ovr_GetPredictedDisplayTime(hmd, 0);
        OVRTrackingState hmdState = OVRTrackingState.malloc();
        ovr_GetTrackingState(hmd, ftiming, true, hmdState);

        //get head pose
        OVRPosef headPose = hmdState.HeadPose().ThePose();
        hmdState.free();

        //build view offsets struct
        OVRVector3f.Buffer hmdToEyeViewOffsets = OVRVector3f.callocBuffer(2);
        hmdToEyeViewOffsets.put(0, eyeRenderDesc[ovrEye_Left].HmdToEyeViewOffset());
        hmdToEyeViewOffsets.put(1, eyeRenderDesc[ovrEye_Right].HmdToEyeViewOffset());
        
        //calculate eye poses
        OVRPosef.Buffer outEyePoses = OVRPosef.createBuffer(2);
        OVRUtil.ovr_CalcEyePoses(headPose, hmdToEyeViewOffsets, outEyePoses);
        eyePoses[ovrEye_Left] = outEyePoses.get(0);
        eyePoses[ovrEye_Right] = outEyePoses.get(1);

        currentTPEIndex += 1;
        currentTPEIndex %= texturesPerEyeCount;
        for (int eyeIndex = 0; eyeIndex < ovrEye_Count; eyeIndex++) {
            int eye = eyeIndex;
            OVRPosef eyePose = eyePoses[eye];
            layer0.RenderPose(eye, eyePose);
//            System.out.println("eye="+eye+", currentTPEIndex="+currentTPEIndex);
            textureSetOne.CurrentIndex(currentTPEIndex);
//            textureSet[eye].setCurrentIndex(currentTPEIndex);
          
            fbuffers[currentTPEIndex][0].activate();
         
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

           
            glViewport(0, 0, textureW*2, textureH);
            
            //Projection Matrix
            glMatrixMode(GL_PROJECTION);
            
            //check below
            //Matrix4f proj = new Matrix4f(projections[eye].buffer().asFloatBuffer()).transpose();
            Matrix4f proj = new Matrix4f(projections[eye].M()).transpose();
            matP.set(proj).get(fbP);
            glLoadMatrixf(fbP);

            //ModelView Matrix
            glMatrixMode(GL_MODELVIEW);
            mat.identity();

            Vector3f offsetPosition = new Vector3f(eyeRenderDesc[eye].HmdToEyeViewOffset().x(), eyeRenderDesc[eye].HmdToEyeViewOffset().y(), eyeRenderDesc[eye].HmdToEyeViewOffset().z());
            mat.translate(offsetPosition);
            
            Quaternionf orientation = new Quaternionf(eyePose.Orientation().x(), eyePose.Orientation().y(), eyePose.Orientation().z(), eyePose.Orientation().w());
            orientation.invert();
            mat.rotate(orientation);

            Vector3f position = new Vector3f(-eyePose.Position().x(), -eyePose.Position().y(), -eyePose.Position().z());
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
        int result = ovr_SubmitFrame(hmd, 0, null, layers);
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
        for (int eye = 0; eye < 2; eye++) {
            projections[eye].free();
        }
        for (int eye = 0; eye < 2; eye++) {
            eyeRenderDesc[eye].free();
        }
        
        if (textureSetOne != null) {
            ovr_DestroySwapTextureSet(hmd, textureSetOne);
        }
        ovr_Destroy(hmd);
        ovr_Shutdown();
    }

    public void toggleHUD() {
        perfHUD++;
        if (perfHUD == 1) {
            ovr_SetInt(hmd, "PerfHudMode", (int)ovrPerfHud_LatencyTiming);
        } else if (perfHUD == 2) {
            ovr_SetInt(hmd, "PerfHudMode", (int)ovrPerfHud_RenderTiming);
        } else {
            perfHUD = 0;
            ovr_SetInt(hmd, "PerfHudMode", (int)ovrPerfHud_Off);
        }
    }
}
