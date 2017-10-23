package eu.insertcode.portfolio.adapters

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by maartendegoede on 18/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class ProjectImagesPagerAdapter : PagerAdapter() {
    private val views = ArrayList<View>()

    override fun getItemPosition(item: Any): Int {
        val index = views.indexOf(item)
        return if (index == -1)
            POSITION_NONE
        else
            index
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = views[position]
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) =
            container.removeView(views[position])

    override fun getCount(): Int = views.size

    override fun isViewFromObject(view: View, item: Any) = view == item

    fun addView(v: View) = addView(v, views.size)
    private fun addView(v: View, position: Int): Int {
        views.add(position, v)
        notifyDataSetChanged()
        return position
    }
}