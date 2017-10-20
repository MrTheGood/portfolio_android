package eu.insertcode.portfolio

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.Utils
import org.json.JSONObject

class ProjectActivity : AppCompatActivity() {
    companion object {
        val EXTRA_ITEM = "project_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        postponeEnterTransition()
        Handler().post {
            startPostponedEnterTransition()
        }

        val projectTitle = findViewById<TextView>(R.id.project_title)
        val projectImage = findViewById<ImageView>(R.id.project_image)
        val projectTags = findViewById<LinearLayout>(R.id.project_tags)
        val projectCopyright = findViewById<TextView>(R.id.project_copyright)
        val projectFullDescription = findViewById<TextView>(R.id.project_fullDescription)
        val projectDate = findViewById<TextView>(R.id.project_date)
//        val projectContributors = findViewById<View?>(R.id.) TODO

        val item: ProjectItem = if (savedInstanceState == null) {
            if (intent.extras == null) {
                finish()
                return
            } else
                ProjectItem(JSONObject(intent.extras.getString(EXTRA_ITEM)))
        } else
            ProjectItem(JSONObject(savedInstanceState.getString(EXTRA_ITEM)))

        projectTitle.text = item.title
        Utils.putImageInView(this, item.img, projectImage)
        projectCopyright.text = String.format(resources.getString(R.string.str_copyrightSymbol), item.copyright)
        projectFullDescription.text = Utils.fromHtmlCompat(item.fullDescription)

        if (item.date == null) projectDate.visibility = View.GONE
        projectDate.text = item.date

        item.tags.indices.forEach {
            addProjectTag(item.tags[it], it, projectTags)
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    private fun addProjectTag(tag: String, i: Int, projectTags: LinearLayout) {
        LayoutInflater.from(this).inflate(R.layout.item_project_tag, projectTags)
        projectTags.getChildAt(i).findViewById<TextView>(R.id.project_tag_text).text = tag
    }
}
