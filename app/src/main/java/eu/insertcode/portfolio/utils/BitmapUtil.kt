package eu.insertcode.portfolio.utils

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

/**
 * Created by maartendegoede on 19/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
@Suppress("unused", "MemberVisibilityCanPrivate")
class BitmapUtil {
    companion object {
        private val DEFAULT_BLUR_BITMAP_SCALE = 0.4f
        private val DEFAULT_BLUR_RADIUS = 7.5f

        fun getRoundedCornerBitmap(ctx: Context, input: Bitmap, pixels: Int, width: Int, height: Int) = getRoundedCornerBitmap(ctx, input, pixels, width, height, false, false, false, false)

        //TODO: Remove rounded corners on big images
        fun getRoundedCornerBitmap(ctx: Context, input: Bitmap, pixels: Int, width: Int, height: Int, TL: Boolean, TR: Boolean, BL: Boolean, BR: Boolean): Bitmap {
            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val densityMultiplier = ctx.resources.displayMetrics.density

            val color = 0xff424242
            val paint = Paint()
            val rect = Rect(0, 0, width, height)
            val rectF = RectF(rect)

            val roundPx = pixels * densityMultiplier

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color.toInt()
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            val h = height.toFloat()
            val w = width.toFloat()
            if (TL) canvas.drawRect(0f, h / 2, w / 2, h, paint)
            if (TR) canvas.drawRect(w / 2, 0f, w, h / 2, paint)
            if (BL) canvas.drawRect(0f, 0f, w / 2, h / 2, paint)
            if (BR) canvas.drawRect(w / 2, h / 2, w, h, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(input, 0f, 0f, paint)

            return output
        }

        fun getBlurredBitmap(ctx: Context, img: Bitmap) = getBlurredBitmap(ctx, img, DEFAULT_BLUR_BITMAP_SCALE, DEFAULT_BLUR_RADIUS)
        fun getBlurredBitmap(ctx: Context, img: Bitmap, bitmapScale: Float, blurRadius: Float): Bitmap {
            val width = Math.round(img.width * bitmapScale)
            val height = Math.round(img.height * bitmapScale)

            val inputBitmap = Bitmap.createScaledBitmap(img, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val renderScript = RenderScript.create(ctx)
            val intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            val tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)
            intrinsicBlur.setRadius(blurRadius)
            intrinsicBlur.setInput(tmpIn)
            intrinsicBlur.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        }

        fun getTintedBitmap(ctx: Context, bitmap: Bitmap, @ColorRes tintColor: Int): Bitmap {
            val drawableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(drawableBitmap)
            val paint = Paint()
            paint.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(ctx, tintColor), PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(drawableBitmap, 0f, 0f, paint)

            return drawableBitmap
        }
    }
}