package com.lukederrynz.application_Hub.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by Luke on 6/09/2017.
 */

// Inner class implementing surfaceView
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    // Touch variables
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

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

    @Override public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from touch screen & other input controls.
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                // Reverse direction of rotation above the mid-line
                dx = (y > getHeight() / 2) ? dx * -1 : dx;
                // Reverse direction of rotation to left of mid-line
                dy = (x < getWidth() / 2) ? dy * -1 : dy;
                mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
