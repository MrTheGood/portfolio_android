package eu.insertcode.portfolio.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.SubcategoryItem

/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Subcategory : LinearLayout {
    private val title: TextView
    private val layout: LinearLayout

    constructor(item: SubcategoryItem, ctx: Context) : this(item, ctx, null)
    constructor(item: SubcategoryItem, ctx: Context, attrs: AttributeSet?) : this(item, ctx, attrs, 0)
    constructor(item: SubcategoryItem, ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        View.inflate(ctx, R.layout.item_subcategory, this)

        layout = findViewById(R.id.subcategory_content)
        title = findViewById(R.id.subcategory_title)
        title.text = item.title
        title.setOnClickListener({
            if (layout.visibility == View.GONE)
                layout.visibility = View.VISIBLE
            else
                layout.visibility = View.GONE
        })
    }
}