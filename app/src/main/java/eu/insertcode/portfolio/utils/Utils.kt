package eu.insertcode.portfolio.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by maartendegoede on 20/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Utils {
    companion object {
        fun putImageInView(ctx: Context, img: String?, view: ImageView) {
            if (img != null) {
                Glide.with(ctx).asBitmap().load(img).into(view)
            }
        }

        fun fromHtmlCompat(s: String): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(s)
        }
    }
}