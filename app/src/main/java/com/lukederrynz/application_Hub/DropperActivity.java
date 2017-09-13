package com.lukederrynz.application_Hub;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lukederrynz.android_test.R;
import com.lukederrynz.application_Hub.OpenGL.MyGLSurfaceView;


public class DropperActivity extends AppCompatActivity {

    public GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOpenGL();

        setContentView(glSurfaceView);

    }


    /**
     * Initialize OpenGLES View
     */
    private void initOpenGL() {

        // Create an OpenGL surface view
        glSurfaceView = new MyGLSurfaceView(this);
        setContentView(R.layout.activity_dropper);
    }


    @Override protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }


    @Override protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }





}
