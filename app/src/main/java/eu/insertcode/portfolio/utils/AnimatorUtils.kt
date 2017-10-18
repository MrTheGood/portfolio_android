package eu.insertcode.portfolio.utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by maartendegoede on 18/10/17.
 * Copyright © 2017 AppStudio.nl. All rights reserved.
 */
class AnimatorUtils {
    companion object {
        fun expandView(v: View) {
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val targetHeight = v.measuredHeight

            v.layoutParams.height = 1 //Pre 21 cancels animations for views of 0 height
            v.visibility = View.VISIBLE
            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    v.layoutParams.height = when (interpolatedTime) {
                        1f -> ViewGroup.LayoutParams.WRAP_CONTENT
                        else -> (targetHeight * interpolatedTime).toInt()
                    }
                    v.requestLayout()
                }

                override fun willChangeBounds() = true
            }

            a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
            v.startAnimation(a)
        }

        fun collapseView(v: View) {
            val initialHeight = v.measuredHeight

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height = (initialHeight - initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds() = true
            }

            a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
            v.startAnimation(a)
        }
    }
}