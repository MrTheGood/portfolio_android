/*
 *    Copyright 2018 Maarten de Goede
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.insertcode.portfolio.ui.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.isError
import eu.insertcode.portfolio.data.isLoading
import eu.insertcode.portfolio.data.isNull
import eu.insertcode.portfolio.data.isSuccess
import eu.insertcode.portfolio.util.isLandscapeOrientation
import eu.insertcode.portfolio.util.isNetworkAvailable
import eu.insertcode.portfolio.util.visibleIf
import kotlinx.android.synthetic.main.fragment_projects.*

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class ProjectsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_projects, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectsAdapter = ProjectsAdapter()
        projectsRecycler.apply {
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(context)
            if (isLandscapeOrientation()) {
                addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            }
        }


        val viewModel = ViewModelProviders.of(this)[ProjectsViewModel::class.java]
        viewModel.projects.observe(this, Observer { projects ->
            projectsAdapter.submitList(projects.data?.projects)

            projectsRecycler.visibleIf(projects.isSuccess)
            projectsLoading.visibleIf(projects.isLoading)

            projectsError.visibleIf(projects.isError || projects.isNull)
            if (requireContext().isNetworkAvailable()) {
                projectsError_image.setImageResource(R.drawable.ic_error)
                projectsError_text.text = getString(R.string.error_unknown)
            } else {
                projectsError_image.setImageResource(R.drawable.ic_wifi_off)
                projectsError_text.text = getString(R.string.error_noConnection)

            }
        })

        viewModel.retry()
        projectsError.setOnClickListener { viewModel.retry() }

    }
}