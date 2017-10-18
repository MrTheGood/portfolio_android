package eu.insertcode.portfolio.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.SubcategoryItem

@SuppressLint("ViewConstructor")
/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Subcategory : LinearLayout {
    private val title: TextView

    constructor(item: SubcategoryItem, ctx: Context) : this(item, ctx, null)
    constructor(item: SubcategoryItem, ctx: Context, attrs: AttributeSet?) : this(item, ctx, attrs, 0)
    constructor(item: SubcategoryItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        View.inflate(ctx, R.layout.item_subcategory, this)

        title = findViewById(R.id.subcategory_title)
        title.text = item.title
    }
}