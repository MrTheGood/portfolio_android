package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.ProjectActivity
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.TagUtils
import eu.insertcode.portfolio.utils.Utils
import android.support.v4.util.Pair as AndroidPair

@SuppressLint("ViewConstructor")
/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Project : FrameLayout {

    private val projectImage: ImageView
    private val projectTitle: TextView
    private val projectShortDescription: TextView
    private val projectFullDescription: TextView? = null
    private val projectCopyright: TextView? = null
    private val projectDate: TextView
    private val projectTags: LinearLayout

    constructor(item: ProjectItem, ctx: Context) : this(item, ctx, null)
    constructor(item: ProjectItem, ctx: Context, attrs: AttributeSet?) : this(item, ctx, attrs, 0)
    constructor(item: ProjectItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        View.inflate(ctx, R.layout.item_project, this)

        projectImage = findViewById(R.id.project_image)
        projectTitle = findViewById(R.id.project_title)
        projectShortDescription = findViewById(R.id.project_shortDescription)
        projectDate = findViewById(R.id.project_date)
        projectTags = findViewById(R.id.project_tags)

        Utils.putImageInView(ctx, item.img, projectImage)

        projectTitle.text = item.title
        projectShortDescription.text = Utils.fromHtmlCompat(item.shortDescription)

        if (item.date == null) projectDate.visibility = View.GONE
        projectDate.text = item.date

        item.tags.indices.forEach {
            TagUtils.addProjectTag(item.tags[it], it, context, projectTags)
        }

        setOnClickListener({
            val intent = Intent(context, ProjectActivity::class.java)
            intent.putExtra(ProjectActivity.EXTRA_ITEM, item.o.toString())
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, projectImage, "project_image")
            context.startActivity(intent, options.toBundle())
        })
    }
}