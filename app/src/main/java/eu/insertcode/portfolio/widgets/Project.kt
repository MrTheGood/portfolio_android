package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.transition.TransitionManager
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import eu.insertcode.portfolio.MainActivity
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.YoutubeVideoActivity
import eu.insertcode.portfolio.adapters.ProjectImagesPagerAdapter
import eu.insertcode.portfolio.data.MediaItem
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.BitmapUtil
import eu.insertcode.portfolio.utils.TagUtils
import eu.insertcode.portfolio.utils.Utils
import kotlinx.android.synthetic.main.item_project.view.*

@SuppressLint("ViewConstructor")
/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Project : FrameLayout {

    private val projectImages: ViewPager
    private val projectTitle: TextView
    private val projectShortDescription: TextView
    private val projectFullDescription: TextView
    private val projectDate: TextView
    private val projectTags: LinearLayout

    private var expanded = false

    constructor(project: ProjectItem, ctx: Context) : this(project, ctx, null)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?) : this(project, ctx, attrs, 0)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        val v = View.inflate(ctx, R.layout.item_project, this)

        projectImages = v.project_image
        projectTitle = v.project_title
        projectShortDescription = v.project_shortDescription
        projectFullDescription = v.project_fullDescription
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
        projectFullDescription.text = Utils.fromHtmlCompat(project.fullDescription)

        if (project.date == null) projectDate.visibility = View.GONE
        projectDate.text = project.date

        project.tags.indices.forEach {
            TagUtils.addProjectTag(project.tags[it], it, context, projectTags)
        }

        setOnClickListener({
            TransitionManager.beginDelayedTransition(this.parent as ViewGroup)
            if (expanded) {
                projectShortDescription.visibility = View.VISIBLE
                projectFullDescription.visibility = View.GONE
                expanded = !expanded
            } else {
                projectShortDescription.visibility = View.GONE
                projectFullDescription.visibility = View.VISIBLE
                expanded = !expanded
            }
        })
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

            Utils.putImageInView(context, img.image, view)
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