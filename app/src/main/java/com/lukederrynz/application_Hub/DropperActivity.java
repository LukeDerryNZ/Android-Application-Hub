package com.lukederrynz.application_Hub;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lukederrynz.android_test.R;
import com.lukederrynz.application_Hub.OpenGL.MyGLSurfaceView;


public class DropperActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOpenGL();

        setContentView(glSurfaceView);

    }

    //
    private void initOpenGL() {

        // Create an OpenGL surface view
        glSurfaceView = new MyGLSurfaceView(this);
        setContentView(glSurfaceView);

    }









}
