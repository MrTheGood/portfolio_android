package eu.insertcode.portfolio

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        val layout: LinearLayout = findViewById(R.id.projects_root)

        val items = items ?: return
        loadProjects(layout, items)
    }

    private fun loadProjects(parent: ViewGroup, items: List<Item>) {
        for (item in items.iterator()) {
            when (item) {
                is CategoryItem -> {
                    val category = Category(item, this)
                    parent.addView(category)
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