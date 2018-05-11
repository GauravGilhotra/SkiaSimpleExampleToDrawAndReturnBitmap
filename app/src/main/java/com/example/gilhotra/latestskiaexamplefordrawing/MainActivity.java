package com.example.gilhotra.latestskiaexamplefordrawing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    private SkiaDrawView fMainView;
    public static Bitmap bb;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Load Skia and then the app shared object in this order
            System.loadLibrary("skia");
            System.loadLibrary("hello_skia_ndk");

        } catch (UnsatisfiedLinkError e) {
            Log.d("HelloSkia", "Link Error: " + e);
            return;
        }

        // Makes and sets a SkiaDrawView as the only thing seen in this activity
        mContext = this;
        fMainView = new SkiaDrawView(this);
        setContentView(fMainView);
    }

    public void newActivity() {
        Intent intent = new Intent(this, BitmapActivity.class);
        startActivity(intent);
    }


    private class SkiaDrawView extends GLSurfaceView implements GLSurfaceView.Renderer {
        Bitmap fSkiaBitmap;
        int g = 1;
        public SkiaDrawView(Context ctx) {
            super(ctx);
            setEGLContextClientVersion(2);

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(this);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh)
        {
            // Create a bitmap for skia to draw into
            fSkiaBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }

        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Set the background frame color
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        public void onDrawFrame(GL10 unused) {
            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            drawIntoBitmap(fSkiaBitmap, SystemClock.elapsedRealtime());
            if(g == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bb = fSkiaBitmap;
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            Log.d("HelloSkia", "thread interruption Error: " + e);
                        }
                        newActivity();
                    }
                });
                g++;
            }

        }

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private native void drawIntoBitmap(Bitmap image, long elapsedTime);
}