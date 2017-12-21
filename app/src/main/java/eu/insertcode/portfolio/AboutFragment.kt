package eu.insertcode.portfolio

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.transition.Slide
import android.support.transition.TransitionSet
import android.support.v4.app.Fragment
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_about.view.*

/**
 * Created by maarten on 2017-12-03.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class AboutFragment : Fragment() {
    companion object {
        fun newInstance() = AboutFragment()

        interface AboutFragmentListener {
            fun setupActionBar(v: Toolbar, title: String)
        }
    }

    private lateinit var listener: AboutFragmentListener
    private lateinit var layout: ViewGroup

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AboutFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("${context.toString()} must implement AboutFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        layout = inflater.inflate(R.layout.fragment_about, container, false) as ViewGroup

        layout.about_mail_btn.setOnClickListener {
            //TODO: Auto-generated code. Replace.
            Snackbar.make(it, "Replace with our own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        listener.setupActionBar(layout.about_toolbar, getString(R.string.title_fragment_about))


        //Setup FAB show animation.
        layout.about_mail_btn.visibility = View.INVISIBLE
        Handler().postDelayed({ layout.about_mail_btn.show() }, resources.getInteger(R.integer.config_enterAnimTime).toLong() / 2)


        return layout
    }


    //region Transition
    private val transitionEnter: TransitionSet
        get() {
            val appBarSlide = Slide(Gravity.TOP)
            appBarSlide.mode = Slide.MODE_IN
            appBarSlide.addTarget(R.id.about_app_bar)

            val contentSlide = Slide(Gravity.BOTTOM)
            contentSlide.mode = Slide.MODE_IN
            contentSlide.addTarget(R.id.about_content)

            return TransitionSet()
                    .addTransition(appBarSlide)
                    .addTransition(contentSlide)
                    .setDuration(resources.getInteger(R.integer.config_enterAnimTime).toLong())
                    .setInterpolator(LinearOutSlowInInterpolator())
        }

    override fun getEnterTransition() = transitionEnter

    override fun getReturnTransition() = transitionEnter

    override fun getExitTransition(): Any? {
        val slide = Slide(Gravity.BOTTOM)
        slide.mode = Slide.MODE_OUT
        slide.duration = resources.getInteger(R.integer.config_exitAnimTime).toLong()
        slide.interpolator = FastOutLinearInInterpolator()
        slide.addTarget(layout)

        return slide
    }
    //endregion Transition

}