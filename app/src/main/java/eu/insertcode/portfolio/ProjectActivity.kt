package eu.insertcode.portfolio

import android.os.Bundle
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
import org.json.JSONObject

class ProjectActivity : AppCompatActivity() {
    companion object {
        val EXTRA_ITEM = "project_item"
    }

    private var projectImages: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        //get intent extras
        val item: ProjectItem =
                if (intent.extras == null) {
                    finish()
                    return
                } else
                    ProjectItem(JSONObject(intent.extras.getString(EXTRA_ITEM)))

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
        if (!item.images.isEmpty()) {
            var first = true
            for (img in item.images) {
                addProjectImage(adapter, img, first)
                first = false
            }
        }

        // setup viewpager indicator
        if (item.images.size > 1)
            findViewById<TabLayout>(R.id.project_image_indicator).setupWithViewPager(projectImages)

        // Add text content
        projectTitle.text = item.title
        projectCopyright.text = String.format(resources.getString(R.string.str_copyrightSymbol), item.copyright)
        projectFullDescription.text = Utils.fromHtmlCompat(item.fullDescription)

        if (item.date == null) projectDate.visibility = View.GONE
        projectDate.text = item.date

        //Add tags
        item.tags.indices.forEach {
            TagUtils.addProjectTag(item.tags[it], it, this, projectTags)
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
        if (projectImages == null || projectImages?.currentItem == 0) {
            supportFinishAfterTransition()
            return
        }
        projectImages?.setCurrentItem(0, true)
    }
}
