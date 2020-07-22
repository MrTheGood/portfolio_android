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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.eventsDispatcherOnMain
import eu.insertcode.portfolio.BR
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.databinding.FragmentPortfolioBinding
import eu.insertcode.portfolio.main.viewmodels.portfolio.PortfolioViewModel
import eu.insertcode.portfolio.ui.anim.PortfolioExit

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
class PortfolioFragment : MvvmFragment<FragmentPortfolioBinding, PortfolioViewModel>(), PortfolioViewModel.EventsListener {

    override val layoutId = R.layout.fragment_portfolio
    override val viewModelClass = PortfolioViewModel::class.java
    override val viewModelVariableId = BR.viewModel

    override fun viewModelFactory() = createViewModelFactory { PortfolioViewModel(eventsDispatcherOnMain()) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        viewModel.eventsDispatcher.bind(
                lifecycleOwner = this,
                listener = this
        )
        viewModel.configure()

        val portfolioAdapter = PortfolioAdapter(viewLifecycleOwner, viewModel::onProjectItemTapped)
        binding.projectsRecycler.adapter = portfolioAdapter
        viewModel.viewState.ld().observe(viewLifecycleOwner, Observer {
            portfolioAdapter.timelineItemViewStates = it?.timelineItemViewStates ?: emptyList()
        })
        viewModel.aboutViewState.ld().observe(viewLifecycleOwner, Observer {
            portfolioAdapter.aboutViewState = it
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.screenPresented()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitTransition = PortfolioExit()
    }


    override fun navigateToProject(projectId: String) {
        val direction = PortfolioFragmentDirections.actionProjectDetail(projectId)
        view?.findNavController()?.navigate(direction)
    }
}