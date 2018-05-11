
#include <math.h>
#include <jni.h>
#include <android/bitmap.h>
#include <skia/include/gpu/GrContext.h>
#include "skia/include/gpu/gl/GrGLInterface.h"
#include <SkImageGenerator.h>

// C++ code to demonstrate string stream method
// to convert number to string.
#include<iostream>
#include <sstream>  // for string streams
#include <string>  // for string

#include "SkCanvas.h"
#include "SkGraphics.h"
#include "SkSurface.h"
#include "SkString.h"
#include "SkTime.h"

/**
 * Draws something into the given bitmap
 * @param  env
 * @param  thiz
 * @param  dstBitmap   The bitmap to place the results of skia into
 * @param  elapsedTime The number of milliseconds since the app was started*/

extern "C"
JNIEXPORT void JNICALL Java_com_example_gilhotra_latestskiaexamplefordrawing_MainActivity_drawIntoBitmap(JNIEnv* env, jobject thiz, jobject dstBitmap, jlong elapsedTime)
{
    AndroidBitmapInfo dstInfo;
    GrContext* grContext;
    void* dstPixels;
    AndroidBitmap_getInfo(env, dstBitmap, &dstInfo);
    AndroidBitmap_lockPixels(env, dstBitmap, &dstPixels);

    SkImageInfo info = SkImageInfo::MakeN32Premul(dstInfo.width, dstInfo.height);

    // Create a surface from the given bitmap
    sk_sp<SkSurface> surface(SkSurface::MakeRasterDirect(info, dstPixels, dstInfo.stride));
    SkCanvas* canvas = surface->getCanvas();



    const GrGLInterface* fInterface = GrGLCreateNativeInterface();
    grContext = GrContext::Create(kOpenGL_GrBackend, (GrBackendContext) fInterface);
    sk_sp<SkSurface> newSurface(SkSurface::MakeRenderTarget(grContext, SkBudgeted::kNo, info, 8 /*MSAA*/, NULL));
    SkCanvas* newCanvas = newSurface->getCanvas();


    // Clear the canvas with a white color
    canvas->drawColor(SK_ColorWHITE);
    // Setup a SkPaint for drawing our text


    SkPaint paint;
    paint.setColor(SK_ColorBLACK); // This is a solid black color for our text
    paint.setTextSize(SkIntToScalar(30)); // Sets the text size to 30 pixels
    paint.setAntiAlias(true); // We turn on anti-aliasing so that the text to looks good.

    SkPaint pr;
    SkPath path;
    pr.setColor(SK_ColorRED);
    pr.setAntiAlias(true);
    pr.setStyle(SkPaint::kStroke_Style);
    pr.setStrokeCap(SkPaint::Cap::kRound_Cap);
    pr.setStrokeJoin(SkPaint::Join::kRound_Join);
    pr.setStrokeWidth(10);
    
    // increased path length 
    for(int z = 0; z < 5000; z++) {
        path.moveTo(100, 100);
        path.lineTo(400, 100);
    }
    newCanvas->drawPath(path,pr);

    std::ostringstream ss;
    ss << path.countPoints();
    std::string geek = ss.str();
    newCanvas->drawString(geek.c_str(), 50, 50, paint);

    newSurface->draw(canvas, 0, 0, &paint);

    // Unlock the dst's pixels
    AndroidBitmap_unlockPixels(env, dstBitmap);
}
