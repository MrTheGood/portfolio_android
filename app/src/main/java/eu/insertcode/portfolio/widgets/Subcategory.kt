package eu.insertcode.portfolio.widgets

import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.SubcategoryItem

/**
 * Created by maartendegoede on 17/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class Subcategory : ConstraintLayout {
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
            if (layout.visibillity == View.GONE)
                layout.visibillity = View.VISIBLE
            else
                layout.visibillity = View.GONE
        })
    }
}