package eu.insertcode.portfolio.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide
import eu.insertcode.portfolio.R

/**
 * Created by maartendegoede on 20/10/17.
 * Copyright © 2017 AppStudio.nl. All rights reserved.
 */
class Utils {
    companion object {
        fun putImageInView(ctx: Context, img: String?, view: ImageView) {
            if (img != null) {
                val imageUrl = ctx.resources.getString(R.string.url_images_prefix) + img
                Glide.with(ctx).asBitmap().load(imageUrl).into(view)
            }
        }

        fun fromHtmlCompat(s: String): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(s)
        }
    }
}