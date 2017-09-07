package com.lukederrynz.application_Hub.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Luke on 6/09/2017.
 */

// Inner class implementing surfaceView
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGLES 2.0 context
        setEGLContextClientVersion(2);
        // Create a renderer
        mRenderer = new MyGLRenderer();
        // Set the renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Only draw when we have to
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
