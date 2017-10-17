package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.CategoryItem

@SuppressLint("ViewConstructor")
/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Category : LinearLayout {

    private val title: TextView

    constructor(item: CategoryItem, ctx: Context) : this(item, ctx, null)
    constructor(item: CategoryItem, ctx: Context, attrs: AttributeSet?) : this(item, ctx, attrs, 0)
    constructor(item: CategoryItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        View.inflate(ctx, R.layout.item_category, this)

        title = findViewById(R.id.category_title)
        title.text = item.title
    }
}