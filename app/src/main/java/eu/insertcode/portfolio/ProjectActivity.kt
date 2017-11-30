package eu.insertcode.portfolio

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.adapters.ProjectImagesPagerAdapter
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.TagUtils
import eu.insertcode.portfolio.utils.Utils

class ProjectActivity : AppCompatActivity() {
    companion object {
        val EXTRA_PROJECT = "extra_project"
    }

    private var projectImages: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        //get intent extras
        val project: ProjectItem =
                if (intent.extras == null) {
                    finish()
                    return
                } else
                    intent.extras.get(EXTRA_PROJECT) as ProjectItem

        //initialize views
        val projectTitle = findViewById<TextView>(R.id.project_title)
        projectImages = findViewById(R.id.project_image)
        val projectTags = findViewById<LinearLayout>(R.id.project_tags)
        val projectCopyright = findViewById<TextView>(R.id.project_copyright)
        val projectFullDescription = findViewById<TextView>(R.id.project_fullDescription)
        val projectDate = findViewById<TextView>(R.id.project_date)
//        val projectContributors = findViewById<View?>(R.id.) TODO: Implement Contributors

        //postpone enter transition
        postponeEnterTransition()
        projectImages?.post {
            startPostponedEnterTransition()
        }

        // Add images
        val adapter = ProjectImagesPagerAdapter()
        projectImages?.adapter = adapter
        if (!project.images.isEmpty()) {
            var first = true
            for (img in project.images) {
                addProjectImage(adapter, img, first)
                first = false
            }
        }

        // setup viewpager indicator
        if (project.images.size > 1)
            findViewById<TabLayout>(R.id.project_image_indicator).setupWithViewPager(projectImages)

        // Add text content
        projectTitle.text = project.title
        projectCopyright.text = String.format(resources.getString(R.string.str_copyrightSymbol), project.copyright)
        projectFullDescription.text = Utils.fromHtmlCompat(project.fullDescription)

        if (project.date == null) projectDate.visibility = View.GONE
        projectDate.text = project.date

        //Add tags
        project.tags.indices.forEach {
            TagUtils.addProjectTag(project.tags[it], it, this, projectTags)
        }
    }

    private fun addProjectImage(adapter: ProjectImagesPagerAdapter, img: String, doAddTransitionName: Boolean) {
        val view = ImageView(this)
        view.scaleType = ImageView.ScaleType.FIT_CENTER
        view.adjustViewBounds = true
        if (doAddTransitionName)
            view.transitionName = resources.getString(R.string.trans_projectImage)
        adapter.addView(view)
        Utils.putImageInView(this, img, view)
    }

    override fun onBackPressed() {
        if (projectImages != null && projectImages?.currentItem != 0) {
            val o: ProjectActivity = this
            projectImages?.setCurrentItem(0, true)
            projectImages?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    if (position == 0 && positionOffset in -0.01f..0.01f) {
                        Handler().post { o.supportFinishAfterTransition() }
                    }
                }

                override fun onPageSelected(position: Int) {}
            })
            return
        }
        supportFinishAfterTransition()
    }
}
