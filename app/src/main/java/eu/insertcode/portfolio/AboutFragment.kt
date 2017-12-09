package eu.insertcode.portfolio

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
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
            fun setupActionBar(v: Toolbar, @StringRes title: Int)
        }
    }

    private lateinit var listener: AboutFragmentListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AboutFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("${context.toString()} must implement AboutFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_about, container, false)

        v.about_mail_btn.setOnClickListener {
            //TODO: Auto-generated code. Replace.
            Snackbar.make(it, "Replace with our own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        listener.setupActionBar(v.about_toolbar, R.string.title_fragment_about)

        return v
    }
}