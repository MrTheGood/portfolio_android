package eu.insertcode.portfolio

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import eu.insertcode.portfolio.adapters.CategoriesPagerAdapter
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.BlurBuilder
import eu.insertcode.portfolio.widgets.Category
import eu.insertcode.portfolio.widgets.Project
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    companion object {
        var categories: List<CategoryItem> = emptyList()
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
        projects_root.addOnPageChangeListener(this)
        projects_tabLayout.setupWithViewPager(projects_root)
        loadProjects(projects_root, categories)
        projects_root.currentItem = 0

        setupMenuDrawer(nav_view.menu, categories)
        onPageSelected(0)


        //Blur drawer header
        var img = BitmapFactory.decodeResource(resources, R.drawable.drawer_header_background)
        img = BlurBuilder.blur(this, img, R.color.drawer_header_image)
        nav_view.getHeaderView(0).findViewById<ImageView>(R.id.drawer_header_image)?.setImageBitmap(img)
    }

    private fun setupMenuDrawer(menu: Menu, categories: List<CategoryItem>) {
        val menuIcons = mapOf(
                Pair("ic_projects", R.drawable.ic_projects),
                Pair("ic_games", R.drawable.ic_games),
                Pair("ic_websites", R.drawable.ic_websites),
                Pair("ic_apps", R.drawable.ic_apps)
        )

        for (i in categories.indices) {
            val category = categories[i]
            menu.add(R.id.menu_group_categories, i, Menu.NONE, category.title)
            menu.findItem(i).icon = ContextCompat.getDrawable(this, menuIcons[category.icon] ?: R.drawable.ic_other)
            menu.findItem(i).isCheckable = true
        }
    }


    private fun loadProjects(parent: ViewGroup, items: List<Any>) {
        items.iterator().forEach {
            when (it) {
                is CategoryItem -> {
                    val category = Category(this)
                    pageAdapter.addView(category, it.title)
                    loadProjects(category.findViewById(R.id.category_content), it.projects)
                }
                is ProjectItem -> parent.addView(Project(it, this))
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
        if (item.itemId == R.id.nav_about) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        categories.indices.filter { item.itemId == it }
                .forEach {
                    projects_root.currentItem = it
                }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        // position + 1 because about_me is the first item.
        nav_view.menu.getItem(position + 1).isChecked = true
    }
    //endregion user-interaction
}
