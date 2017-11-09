package eu.insertcode.portfolio

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewGroup
import eu.insertcode.portfolio.adapters.CategoriesPagerAdapter
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.Item
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.widgets.Category
import eu.insertcode.portfolio.widgets.Project
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        var items: List<Item>? = null
    }

    private val pageAdapter = CategoriesPagerAdapter()

    //region initialization
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.desc_drawer_open, R.string.desc_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        projects_root.adapter = pageAdapter
        projects_tabLayout.setupWithViewPager(projects_root)
        val items = items ?: return
        loadProjects(projects_root, items)
        projects_root.currentItem = 0
    }


    private fun loadProjects(parent: ViewGroup, items: List<Item>) {
        for (item in items.iterator()) {
            when (item) {
                is CategoryItem -> {
                    val category = Category(this)
                    pageAdapter.addView(category, item.title)
                    loadProjects(category.findViewById(R.id.category_content), item.items)
                }
                is ProjectItem -> parent.addView(Project(item, this))
            }
        }
    }
    //endregion initialization


    //region user-interaction
    override fun onBackPressed() =
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // TODO: Implement navigation logic
        when (item.itemId) {
            R.id.nav_projects -> {
            }
            R.id.nav_about -> {
            }

            R.id.nav_featured -> {
            }
            R.id.nav_games -> {
            }
            R.id.nav_websites -> {
            }
            R.id.nav_apps -> {
            }
            R.id.nav_other -> {
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    //endregion user-interaction
}
