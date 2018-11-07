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

package eu.insertcode.portfolio.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import eu.insertcode.portfolio.databinding.FragmentPortfolioBinding
import eu.insertcode.portfolio.ui.anim.PortfolioExit
import eu.insertcode.portfolio.util.isNetworkAvailable

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class PortfolioFragment : Fragment() {
    private lateinit var portfolioViewModel: PortfolioViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        portfolioViewModel = ViewModelProviders.of(this)[PortfolioViewModel::class.java]
        val binding = FragmentPortfolioBinding.inflate(inflater, container, false).apply {
            viewModel = portfolioViewModel
            isNetworkAvailable = requireContext().isNetworkAvailable()
            setLifecycleOwner(this@PortfolioFragment)
        }

        val portfolioAdapter = PortfolioAdapter()
        binding.projectsRecycler.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = portfolioAdapter
        }
        subscribeUi(portfolioAdapter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitTransition = PortfolioExit()
    }

    private fun subscribeUi(adapter: PortfolioAdapter) {
        portfolioViewModel.projects.observe(viewLifecycleOwner, Observer { resource ->
            val projects = resource?.data?.projects
            if (projects != null) adapter.submitList(projects)
        })
    }
}