package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
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
    private val projectDescription: TextView
    private val expandProject: ImageView

    constructor(item: ProjectItem, ctx: Context) : this(item, ctx, null)
    constructor(item: ProjectItem, ctx: Context, attrs: AttributeSet?) : this(item, ctx, attrs, 0)
    constructor(item: ProjectItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        View.inflate(ctx, R.layout.item_project, this)

        projectImage = findViewById(R.id.project_image)
        projectTitle = findViewById(R.id.project_title)
        projectDescription = findViewById(R.id.project_description)
        expandProject = findViewById(R.id.expand_project)

        if (item.img != null)  {
            val imageUrl = resources.getString(R.string.url_images_prefix) + item.img
            Glide.with(this).asBitmap().load(imageUrl).into(projectImage)
        }
        projectTitle.text = item.title
        projectDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(item.shortDescription, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(item.shortDescription)
        }

        expandProject.setOnClickListener({
            Log.d("TODO", "TODO")
            //TODO: expandProject listener
            //TODO: put this listener on the whole project instead of only the button.
        })
    }
}