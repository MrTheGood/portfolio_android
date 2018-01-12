package eu.insertcode.portfolio.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import eu.insertcode.portfolio.R

/**
 * Created by maarten on 2018-01-08.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class VideoButtonView : FrameLayout {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.video_button, this)
    }
}