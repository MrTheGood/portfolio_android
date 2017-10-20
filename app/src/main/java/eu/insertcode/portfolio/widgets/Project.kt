package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.ProjectItem

@SuppressLint("ViewConstructor")
/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Project : ConstraintLayout {
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

        if (item.img != null) {
            val imageUrl = resources.getString(R.string.url_images_prefix) + item.img
            Glide.with(this).asBitmap().load(imageUrl).into(projectImage)
        }

        projectTitle.text = item.title
        projectShortDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(item.shortDescription, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(item.shortDescription)
        }
        if (item.date == null) projectDate.visibility = View.GONE
        projectDate.text = item.date

        item.tags.indices.forEach {
            addSubcategory(item.tags[it], it)
        }

        setOnClickListener({
            Log.d("TODO", "TODO")
            //TODO: expandProject listener
        })
    }

    private fun addSubcategory(tag: String, i: Int) {
        val v = LayoutInflater.from(context).inflate(R.layout.item_project_tag, projectTags) as LinearLayout
        v.getChildAt(i).findViewById<TextView>(R.id.project_tag_text).text = tag
    }
}