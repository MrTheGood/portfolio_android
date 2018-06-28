package eu.insertcode.portfolio.view.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import eu.insertcode.portfolio.R

/**
 * Created by MrTheGood on 17/10/17.
 * Copyright © 2017 MrTheGood. All rights reserved.
 */
class Category : LinearLayout {

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        View.inflate(ctx, R.layout.item_category, this)
    }
}