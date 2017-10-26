package eu.insertcode.portfolio.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import eu.insertcode.portfolio.utils.BitmapUtil

/**
 * Created by maartendegoede on 19/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class ProjectImageView : AppCompatImageView {

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr)

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val drawable = drawable as BitmapDrawable? ?: return

        val b = drawable.bitmap
        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)

        val roundBitmap = BitmapUtil.getRoundedCornerBitmap(context, bitmap, 2, width, height, false, true, false, true)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)
    }
}
