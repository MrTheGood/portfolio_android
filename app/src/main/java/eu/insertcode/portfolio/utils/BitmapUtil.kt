package eu.insertcode.portfolio.utils

import android.content.Context
import android.graphics.*

/**
 * Created by maartendegoede on 19/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class BitmapUtil {
    companion object {

        fun getRoundedCornerBitmap(ctx: Context, input: Bitmap, pixels: Int, width: Int, height: Int) = getRoundedCornerBitmap(ctx, input, pixels, width, height, false, false, false, false)
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
    }
}