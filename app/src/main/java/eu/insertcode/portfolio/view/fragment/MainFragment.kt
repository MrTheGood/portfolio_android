/*
 *    Copyright 2018 Maarten de Goede
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.insertcode.portfolio.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.view.adapter.CategoriesPagerAdapter
import eu.insertcode.portfolio.view.view.Category
import eu.insertcode.portfolio.view.view.Project
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.io.Serializable

/**
 * Created by MrTheGood on 2017-12-21.
 * Copyright © 2017 MrTheGood. All rights reserved.
 */
class MainFragment : Fragment(), ViewPager.OnPageChangeListener {
    companion object {
        private const val CATEGORIES = "CATEGORIES_ARGS"
        fun newInstance(categories: List<CategoryItem>): MainFragment {
            val fragment = MainFragment()

            val args = Bundle()
            args.putSerializable(CATEGORIES, categories as Serializable)
            fragment.arguments = args

            return fragment
        }

        interface MainFragmentListener {
            fun setupActionBar(v: Toolbar, title: String)
            fun updateMenuDrawer(position: Int)
        }
    }

    private lateinit var listener: MainFragmentListener
    private val pageAdapter = CategoriesPagerAdapter()
    private var categories: List<CategoryItem> = emptyList()
    private lateinit var layout: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement MainFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        layout = inflater.inflate(R.layout.fragment_main, container, false)

        @Suppress("UNCHECKED_CAST")
        categories = arguments!!.getSerializable(CATEGORIES) as List<CategoryItem>

        listener.setupActionBar(layout.toolbar, getString(R.string.title_activity_main))

        layout.projects_root.adapter = pageAdapter
        layout.projects_root.addOnPageChangeListener(this)
        layout.projects_tabLayout.setupWithViewPager(layout.projects_root)
        loadProjects(layout.projects_root, categories)
        layout.projects_root.currentItem = 0

        onPageSelected(0)

        return layout
    }


    private fun loadProjects(parent: ViewGroup, items: List<Any>) {
        if (context == null) return
        val context = context!!
        items.iterator().forEach {
            when (it) {
                is CategoryItem -> {
                    val category = Category(context)
                    pageAdapter.addView(category, it.title)
                    loadProjects(category.findViewById(R.id.category_content), it.projects)
                }
                is ProjectItem -> parent.addView(Project(it, context))
            }
        }
    }

    fun getCurrentItem() = layout.projects_root.currentItem

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        listener.updateMenuDrawer(position)
    }
}
