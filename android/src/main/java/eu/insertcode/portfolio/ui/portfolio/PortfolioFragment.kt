/*
 *    Copyright 2019 Maarten de Goede
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
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import eu.insertcode.portfolio.BR
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.databinding.FragmentPortfolioBinding
import eu.insertcode.portfolio.main.viewmodels.portfolio.PortfolioViewModel
import eu.insertcode.portfolio.ui.anim.PortfolioExit
import eu.insertcode.portfolio.util.analyticsShareApp
import eu.insertcode.portfolio.util.isNetworkAvailable
import eu.insertcode.portfolio.util.startTextShareIntent

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
class PortfolioFragment : MvvmFragment<FragmentPortfolioBinding, PortfolioViewModel>() {


    override val layoutId = R.layout.fragment_portfolio
    override val viewModelClass = PortfolioViewModel::class.java
    override val viewModelVariableId = BR.viewModel

    override fun viewModelFactory() = createViewModelFactory { PortfolioViewModel() }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.isNetworkAvailable = requireContext().isNetworkAvailable()
        viewModel.configure()

        //todo: possibly use viewModel with an EventsDispatcher
        val portfolioAdapter = PortfolioAdapter(
                viewModel::onProjectItemTapped
        )
        binding.projectsRecycler.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
            adapter = portfolioAdapter
        }
        viewModel.viewState.ld().observe(viewLifecycleOwner, Observer {
            portfolioAdapter.submitList(it?.timelineItemViewStates ?: emptyList())
        })

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_portfolio, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            if (item.itemId == R.id.menu_share) {
                startTextShareIntent(getString(R.string.string_share_app))
                analyticsShareApp()
                true
            } else super.onOptionsItemSelected(item)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitTransition = PortfolioExit()
    }
}