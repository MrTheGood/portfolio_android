package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.MainActivity
import eu.insertcode.portfolio.ProjectFragment
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
    private val projectDate: TextView
    private val projectTags: LinearLayout

    constructor(project: ProjectItem, ctx: Context) : this(project, ctx, null)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?) : this(project, ctx, attrs, 0)
    constructor(project: ProjectItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        val layoutResource = if (project.layout == "item_project_large") {
            R.layout.item_project_large
        } else {
            R.layout.item_project
        }
        View.inflate(ctx, layoutResource, this)

        projectImage = findViewById(R.id.project_image)
        projectTitle = findViewById(R.id.project_title)
        projectShortDescription = findViewById(R.id.project_shortDescription)
        projectDate = findViewById(R.id.project_date)
        projectTags = findViewById(R.id.project_tags)

        if (!project.images.isEmpty())
            Utils.putImageInView(ctx, project.images[0], projectImage)

        projectTitle.text = project.title
        projectShortDescription.text = Utils.fromHtmlCompat(project.shortDescription)

        if (project.date == null) projectDate.visibility = View.GONE
        projectDate.text = project.date

        project.tags.indices.forEach {
            TagUtils.addProjectTag(project.tags[it], it, context, projectTags)
        }

        setOnClickListener({
            (context as MainActivity)
                    .getFragmentTransaction(ProjectFragment.newInstance(project))
                    .addSharedElement(projectImage, resources.getString(R.string.trans_projectImage))
                    .addSharedElement(projectTags, resources.getString(R.string.trans_projectTags))
                    .commit()
        })
    }
}