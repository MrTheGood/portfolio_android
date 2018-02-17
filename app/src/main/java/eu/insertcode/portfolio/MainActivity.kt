package eu.insertcode.portfolio

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.utils.BitmapUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by MrTheGood.
 * Copyright © 2018 MrTheGood. All rights reserved.
 */
class MainActivity : AppCompatActivity(),
        MainFragment.Companion.MainFragmentListener,
        AboutFragment.Companion.AboutFragmentListener,
        NavigationView.OnNavigationItemSelectedListener {
    companion object {
        var categories: List<CategoryItem> = emptyList()
    }

    private var currentFragment: Fragment? = null
    private lateinit var mainFragment: MainFragment

    //FIXME: If the activity gets killed while a fragment is open and the user navigates back to the activity, some problems occur.
    //          (Steps to reproduce: Turn on "Don't keep activities", open any fragment and reopen the app.)

    //region initialization
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFragment = MainFragment.newInstance(categories)
        supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment_layout, mainFragment)
                .commit()

        nav_view.setNavigationItemSelectedListener(this)
        setupMenuDrawer(nav_view.menu, categories)

        //Blur drawer header
        var img = BitmapFactory.decodeResource(resources, R.drawable.drawer_header_background)
        img = BitmapUtil.getTintedBitmap(this, img, R.color.drawer_header_image)
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

    override fun setupActionBar(v: Toolbar, title: String) {
        v.title = title
        setSupportActionBar(v)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, v,
                R.string.desc_drawer_open, R.string.desc_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }
    //endregion initialization


    //region user-interaction
    override fun onBackPressed() =
            when {
                drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
                currentFragment != null -> {
                    supportFragmentManager.beginTransaction()
                            .remove(currentFragment)
                            .commit()
                    currentFragment = null
                    updateMenuDrawer(mainFragment.getCurrentItem())
                }
                else -> super.onBackPressed()
            }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)

        if (item.itemId == R.id.nav_about) {
            currentFragment = AboutFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(fragments_layout.id, currentFragment)
                    .commit()
            nav_view.menu.getItem(0).isChecked = true
        }

        categories.indices.filter { item.itemId == it }
                .forEach {
                    if (currentFragment != null) {
                        supportFragmentManager.beginTransaction()
                                .remove(currentFragment)
                                .commit()
                        currentFragment = null
                    }
                    mainFragment.projects_root.currentItem = it
                }
        return true
    }


    override fun updateMenuDrawer(position: Int) {
        // position + 1 because about_me is the first item.
        nav_view.menu.getItem(position + 1).isChecked = true
    }

    fun openVideoActivity(video: String?) {
        if (video != null) {
            val intent = Intent(this, YoutubeVideoActivity::class.java)
            intent.putExtra(YoutubeVideoActivity.EXTRA_VIDEO, video)
            startActivity(intent)
        }
    }
    //endregion user-interaction
}
