package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.MainActivity
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.adapters.ProjectImagesPagerAdapter
import eu.insertcode.portfolio.data.MediaItem
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.BitmapUtil
import eu.insertcode.portfolio.utils.TagUtils
import eu.insertcode.portfolio.utils.Utils
import kotlinx.android.synthetic.main.item_project_large.view.*
import kotlinx.android.synthetic.main.video_thumbnail.view.*

@SuppressLint("ViewConstructor")
/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Project : FrameLayout {

    private val projectImages: ViewPager
    private val projectTitle: TextView
    private val projectShortDescription: TextView
    private val projectDate: TextView
    private val projectTags: LinearLayout

    constructor(project: ProjectItem, ctx: Context) : this(project, ctx, null)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?) : this(project, ctx, attrs, 0)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        val v = View.inflate(ctx, R.layout.item_project_large, this)

        projectImages = v.project_image
        projectTitle = v.project_title
        projectShortDescription = v.project_shortDescription
        projectDate = v.project_date
        projectTags = v.project_tags

        // Add images
        val adapter = ProjectImagesPagerAdapter()
        projectImages.adapter = adapter
        if (!project.images.isEmpty()) {
            for (img in project.images) {
                addProjectImage(adapter, img)
            }
        }

        // setup viewpager indicator
        if (project.images.size > 1) {
            v.project_image_indicator.setupWithViewPager(projectImages)
        } else if (project.images.isEmpty()) {
            v.project_image.visibility = View.GONE
            v.project_image_indicator.visibility = View.GONE
        }


        projectTitle.text = project.title
        projectShortDescription.text = Utils.fromHtmlCompat(project.shortDescription)

        if (project.date == null) projectDate.visibility = View.GONE
        projectDate.text = project.date

        project.tags.indices.forEach {
            TagUtils.addProjectTag(project.tags[it], it, context, projectTags)
        }

        setOnClickListener({
            //TODO: Expand view
        })
    }

    private fun addProjectImage(adapter: ProjectImagesPagerAdapter, img: MediaItem) {
        val view = if (img.hasVideo()) {
            VideoThumbnailView(context)
        } else {
            ImageView(context)
        }
        val imageView = view.thumbnail ?: view as ImageView
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.adjustViewBounds = true

        if (view is VideoThumbnailView) {
            view.playButton.setOnClickListener {
                (context as MainActivity).openVideoActivity(img.video)
            }
        }

        adapter.addView(view)
        Utils.putImageInView(context!!, img.image, imageView)


        imageView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if (v is ImageView && v.drawable != null) {
                    v.background = BitmapDrawable(resources, BitmapUtil.getBlurredBitmap(context, BitmapUtil.drawableToBitmap(v.drawable), 0.2f, 15f))
                    v.removeOnLayoutChangeListener(this)
                }
            }

        })
    }
}