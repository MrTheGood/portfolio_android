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
 * Created by maarten on 2017-12-03.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class BlurBuilder {
    companion object {
        private val BITMAP_SCALE = 0.6f
        private val BLUR_RADIUS = 7.5f

        fun blur(ctx: Context, img: Bitmap, @ColorRes overlay: Int): Bitmap {
            val width = Math.round(img.width * BITMAP_SCALE)
            val height = Math.round(img.height * BITMAP_SCALE)

            val inputBitmap = Bitmap.createScaledBitmap(img, width, height, false)
            val outputBitmap = Bitmap.createBitmap(inputBitmap)

            val rs = RenderScript.create(ctx)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(BLUR_RADIUS)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)

            val canvas = Canvas(outputBitmap)
            val paint = Paint()
            paint.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(ctx, overlay), PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(outputBitmap, 0f, 0f, paint)

            return outputBitmap
        }
    }
}