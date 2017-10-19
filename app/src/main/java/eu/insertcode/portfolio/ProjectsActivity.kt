package eu.insertcode.portfolio

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import eu.insertcode.portfolio.adapters.CategoriesPagerAdapter
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.Item
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.data.SubcategoryItem
import eu.insertcode.portfolio.widgets.Category
import eu.insertcode.portfolio.widgets.Project
import eu.insertcode.portfolio.widgets.Subcategory


class ProjectsActivity : AppCompatActivity() {
    companion object {
        var items: List<Item>? = null
    }
    private val pageAdapter = CategoriesPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        val viewPager: ViewPager = findViewById(R.id.projects_root)
        viewPager.adapter = pageAdapter
        val tabLayout: TabLayout = findViewById(R.id.projects_tabLayout)
        tabLayout.setupWithViewPager(viewPager)

        val items = items ?: return
        loadProjects(viewPager, items)
        viewPager.currentItem = 0
    }

    private fun loadProjects(parent: ViewGroup, items: List<Item>) {
        for (item in items.iterator()) {
            when (item) {
                is CategoryItem -> {
                    val category = Category(this)
                    pageAdapter.addView(category, item.title)
                    loadProjects(category.findViewById(R.id.category_content), item.items)
                }
                is SubcategoryItem -> {
                    val category = Subcategory(item, this)
                    parent.addView(category)
                    loadProjects(category.findViewById(R.id.subcategory_content), item.items)
                }
                is ProjectItem -> parent.addView(Project(item, this))
            }
        }
    }
}