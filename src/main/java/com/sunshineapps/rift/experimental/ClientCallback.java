package com.sunshineapps.rift.experimental;

import org.joml.Matrix4f;

public interface ClientCallback {
    public void drawScene(final Matrix4f mat);
    public void shutdown();
    public void keyPressed(final int key);
}
