package com.lukederrynz.application_Hub.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


/**
 * Created by Luke Derry 6/09/2017
 *
 * A view container where OpenGLES graphics can be drawn to screen.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    // Touch variables -- OBSOLETE --
    //private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    //private float mPreviousX;
    //private float mPreviousY;


    /**
     * Main view container constructor.
     *
     * @param context - Context
     */
    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGLES 2.0 context
        setEGLContextClientVersion(2);
        // Create a renderer
        mRenderer = new MyGLRenderer();
        // Set the renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        //super.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Not required?

        // Render continuously
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    /**
     * Custom TouchEvent - Sets the angle member of an MyGLRenderer instance.
     *
     * @param e - MotionEvent
     * @return true - TODO: Can add and process false if not touched this frame.
     */
    @Override public boolean onTouchEvent(MotionEvent e) {

        final float downx = e.getX();
        final float downy = getHeight() - e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Start new line
                this.queueEvent( new Runnable() {
                    @Override public void run() {
                        mRenderer.startNewLine(downx, downy);
                        //mRenderer.newLine.setStartVerts(downx, downy);
                        //mRenderer.newLine.setEndVerts(downx, downy);
                    }
                });
                break;

            case MotionEvent.ACTION_MOVE:
                // Continuously set end vertex values
                queueEvent( new Runnable() {
                    @Override public void run() {
                            mRenderer.newLine.setEndVerts(downx, downy);
                    }
                });
                return false;

            case MotionEvent.ACTION_UP:
                // Stop drawing line and store
                queueEvent(new Runnable() {
                    @Override public void run() { mRenderer.storeNewLine(); }
                });
                break;
    }

        return true;

        // -- OBSOLETE --
//        // MotionEvent reports input details from touch screen & other input controls.
//        float x = e.getX();
//        float y = e.getY();
//
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                float dx = x - mPreviousX;
//                float dy = y - mPreviousY;
//                // Reverse direction of rotation above the mid-line
//                dx = (y > getHeight() / 2) ? dx * -1 : dx;
//                // Reverse direction of rotation to left of mid-line
//                dy = (x < getWidth() / 2) ? dy * -1 : dy;
//                mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
//                requestRender();
//        }
//        mPreviousX = x;
//        mPreviousY = y;
//        return true;
    }

    @Override public void onPause() {
        mRenderer.timer.cancel();
        mRenderer.timer = null;
    }


}
