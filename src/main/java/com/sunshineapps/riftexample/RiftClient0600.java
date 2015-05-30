package com.sunshineapps.riftexample;

import static org.lwjgl.ovr.OVR.ovrEye_Left;
import static org.lwjgl.ovr.OVR.ovrHmd_Create;
import static org.lwjgl.ovr.OVR.ovrHmd_CreateDebug;
import static org.lwjgl.ovr.OVR.ovrHmd_DK2;
import static org.lwjgl.ovr.OVR.ovrHmd_Detect;
import static org.lwjgl.ovr.OVR.ovrSuccess;
import static org.lwjgl.ovr.OVR.ovr_GetVersionString;
import static org.lwjgl.ovr.OVR.ovr_Initialize;
import static org.lwjgl.ovr.OVR.ovr_Shutdown;
import static org.lwjgl.system.MemoryUtil.memDecodeASCII;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.ovr.OVRFovPort;
import org.lwjgl.ovr.OVRHmdDesc;
import org.lwjgl.ovr.OVRInitParams;
import org.lwjgl.ovr.OVRLogCallback;
import org.lwjgl.ovr.OVRSizei;

public final class RiftClient0600 {
    private final boolean useDebugHMD = true;
 //   private final int riftWidth = 1920; // DK2
 //   private final int riftHeight = 1080; // DK2

    private OVRHmdDesc hmd;
    private int resolutionW;
    private int resolutionH;

//    private static long window;

    private void run() {
        System.out.println(""+System.getProperty("java.version"));
        
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
        System.out.println("ovrHmd_Detect = " + ovrHmd_Detect());
        
        // step 2 - hmd create
        System.out.println("step 2 - hmd create");
        hmd = new OVRHmdDesc();
        if (ovrHmd_Create(0, hmd.buffer()) != ovrSuccess) {
            if (!useDebugHMD) {
                System.out.println("debug disabled");
                return;
            }
            System.out.println("create failed, try debug");
            if (ovrHmd_CreateDebug(ovrHmd_DK2, hmd.buffer()) != 0) {
                System.out.println("debug failed, quit");
                return;
            }
        }
        System.out.println("Serial Number: "+hmd.getSerialNumberString());

        // step 3 - hmd size queries
        System.out.println("step 3 - hmd sizes");
        resolutionW = hmd.getResolutionW();
        resolutionH = hmd.getResolutionH();
        System.out.println("resolution W=" + resolutionW + " H=" + resolutionH);
        
        //pov
        ByteBuffer leftDefaultPOV = BufferUtils.createByteBuffer(OVRFovPort.SIZEOF);
        hmd.getDefaultEyeFov(leftDefaultPOV, ovrEye_Left);
        
        OVRSizei dim = new OVRSizei(leftDefaultPOV);
        System.out.println("fov left="+dim.getW() +","+ dim.getH());
        
//        float pixelsPerDisplayPixel = 1.0f;
//        ByteBuffer GFTSResult = BufferUtils.createByteBuffer(OVRSizei.SIZEOF); 
//        ovrHmd_GetFovTextureSize(hmd.getPointer(), ovrEye_Left, leftDefaultPOV, pixelsPerDisplayPixel, GFTSResult);
//        OVRSizei dim = new OVRSizei(GFTSResult);
//        System.out.println("fov texture size="+dim.getW() +","+ dim.getH());
//        
        
        
        

        // ovrHmd_ConfigureTracking(hmd, supportedTrackingCaps, requiredTrackingCaps)

        // window

        // glfwSetErrorCallback(ErrorCallback.Util.getDefault());
//        if (glfwInit() != GL_TRUE) {
//            throw new IllegalStateException("Unable to initialize GLFW");
//        }
//        window = glfwCreateWindow(riftWidth, riftHeight, "Hello World!", NULL, NULL);
//        System.out.println("use rift debug mode");
//
//        glfwMakeContextCurrent(window);
//        glfwShowWindow(window);
//        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
//        GLContext.createFromCurrent();
//        glClearColor(.42f, .67f, .87f, 1f);
//
//        while (glfwWindowShouldClose(window) == GL_FALSE) {
//
//        }

  //       ovrHmd_Destroy(hmd.getPointer());
         ovr_Shutdown();
    }

    public static void main(String[] args) {
        new RiftClient0600().run();
    }
}