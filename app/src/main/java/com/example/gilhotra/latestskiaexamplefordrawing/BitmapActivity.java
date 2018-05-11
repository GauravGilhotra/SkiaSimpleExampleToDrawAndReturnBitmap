package com.example.gilhotra.latestskiaexamplefordrawing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

public class BitmapActivity extends Activity {

    private SkiaDrawView fMainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fMainView = new SkiaDrawView(this);
        setContentView(fMainView);
        fMainView.postInvalidate();
    }

    private class SkiaDrawView extends View {
        public SkiaDrawView(Context ctx) {
            super(ctx);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // Present the bitmap on the screen
            canvas.drawBitmap(MainActivity.bb, 0, 0, null);
        }
    }
}
