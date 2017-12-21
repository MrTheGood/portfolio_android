package eu.insertcode.portfolio

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import eu.insertcode.portfolio.adapters.ProjectImagesPagerAdapter
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.utils.TagUtils
import eu.insertcode.portfolio.utils.Utils
import kotlinx.android.synthetic.main.fragment_project.view.*

/**
 * Created by maarten on 2017-12-09.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class ProjectFragment : Fragment() {
    companion object {
        val EXTRA_PROJECT = "extra_project"

        fun newInstance(project: ProjectItem): ProjectFragment {
            val fragment = ProjectFragment()

            val args = Bundle()
            args.putSerializable(EXTRA_PROJECT, project)
            fragment.arguments = args

            return fragment
        }

        interface ProjectFragmentListener {
            fun setupActionBar(v: Toolbar, title: String)
        }
    }

    private var projectImages: ViewPager? = null
    private lateinit var listener: ProjectFragmentListener


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ProjectFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("${context.toString()} must implement AboutFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_project, container, false)

        //Get arguments
        val project: ProjectItem =
                if (arguments == null) {
                    return null
                } else {
                    arguments?.getSerializable(EXTRA_PROJECT) as ProjectItem
                }

        //initialize views
        val projectToolbar = v.project_toolbar
        projectImages = v.project_image
        val projectTags = v.project_tags
        val projectCopyright = v.project_copyright
        val projectFullDescription = v.project_fullDescription
        val projectDate = v.project_date


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
            v.project_image_indicator.setupWithViewPager(projectImages)

        // Add text content
        listener.setupActionBar(projectToolbar, project.title)
        projectCopyright.text = String.format(resources.getString(R.string.str_copyrightSymbol), project.copyright)
        projectFullDescription.text = Utils.fromHtmlCompat(project.fullDescription)

        if (project.date == null) projectDate.visibility = View.GONE
        projectDate.text = project.date

        //Add tags
        project.tags.indices.forEach {
            TagUtils.addProjectTag(project.tags[it], it, context!!, projectTags)
        }


        return v
    }

    private fun addProjectImage(adapter: ProjectImagesPagerAdapter, img: String, doAddTransitionName: Boolean) {
        val view = ImageView(context)
        view.scaleType = ImageView.ScaleType.FIT_CENTER
        view.adjustViewBounds = true
        if (doAddTransitionName)
            view.transitionName = resources.getString(R.string.trans_projectImage)
        adapter.addView(view)
        Utils.putImageInView(context!!, img, view)
    }
}
