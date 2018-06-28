/*
 *    Copyright 2018 Maarten de Goede
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.insertcode.portfolio.view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.MediaItem
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.BitmapUtil
import eu.insertcode.portfolio.utils.TagUtils
import eu.insertcode.portfolio.utils.loadImage
import eu.insertcode.portfolio.utils.spanHtml
import eu.insertcode.portfolio.view.activity.MainActivity
import eu.insertcode.portfolio.view.activity.YoutubeVideoActivity
import eu.insertcode.portfolio.view.adapter.ProjectImagesPagerAdapter
import kotlinx.android.synthetic.main.item_project.view.*


@SuppressLint("ViewConstructor")
/**
 * Created by MrTheGood on 17/10/17.
 * Copyright © 2017 MrTheGood. All rights reserved.
 */
class Project : FrameLayout {

    private var expanded = false

    constructor(project: ProjectItem, ctx: Context) : this(project, ctx, null)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?) : this(project, ctx, attrs, 0)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        val v = View.inflate(ctx, R.layout.item_project, this)

        // Add images
        val adapter = ProjectImagesPagerAdapter()
        project_image.adapter = adapter
        if (!project.images.isEmpty()) {
            for (img in project.images) {
                addProjectImage(adapter, img)
            }
        }

        // setup viewpager indicator
        if (project.images.size > 1) {
            v.project_image_indicator.setupWithViewPager(project_image)
        } else if (project.images.isEmpty()) {
            v.project_image.visibility = View.GONE
            v.project_image_indicator.visibility = View.GONE
        }


        project_title.text = project.title
        project_shortDescription.text = project.shortDescription.spanHtml()
        project_fullDescription.text = project.fullDescription.spanHtml()
        project_date.text = project.date

        project.tags.indices.forEach {
            TagUtils.addProjectTag(project.tags[it], it, context, project_tags)
        }

        project_expandButton.setOnClickListener {
            TransitionManager.beginDelayedTransition(this.parent as ViewGroup)
            if (expanded) {
                project_fullDescription.visibility = View.GONE
                expanded = !expanded
                project_expandButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_expand))
            } else {
                project_fullDescription.visibility = View.VISIBLE
                expanded = !expanded
                project_expandButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_collapse))
            }
        }
    }

    private fun addProjectImage(adapter: ProjectImagesPagerAdapter, img: MediaItem) {
        if (img.hasYoutubeVideo()) {

            val view = YouTubeThumbnailView(context)
            view.initialize(YoutubeVideoActivity.API_KEY, object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(view: YouTubeThumbnailView?, loader: YouTubeThumbnailLoader?) {
                    loader?.setVideo(img.youtubeVideo)
                }

                override fun onInitializationFailure(view: YouTubeThumbnailView?, errorReason: YouTubeInitializationResult?) {
                }
            })

            val button = VideoButtonView(context)
            button.setOnClickListener { (context as MainActivity).openVideoActivity(img.youtubeVideo) }

            val layout = FrameLayout(context)
            layout.addView(view)
            layout.addView(button)
            adapter.addView(layout)

        } else if (img.hasImage()) {

            val view = ImageView(context)
            view.scaleType = ImageView.ScaleType.FIT_CENTER
            view.adjustViewBounds = true
            adapter.addView(view)

            view.loadImage(img.image)
            view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    if (v is ImageView && v.drawable != null) {
                        v.background = BitmapDrawable(resources, BitmapUtil.getBlurredBitmap(context, BitmapUtil.drawableToBitmap(v.drawable), 0.2f, 15f))
                        v.removeOnLayoutChangeListener(this)
                    }
                }
            })
        }
    }
}