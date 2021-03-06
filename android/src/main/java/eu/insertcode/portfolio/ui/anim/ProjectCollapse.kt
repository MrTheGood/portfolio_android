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

package eu.insertcode.portfolio.ui.anim

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.TransitionSet
import eu.insertcode.portfolio.ui.anim.custom.ElevationTransition

/**
 * Created by maartendegoede on 06/11/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class ProjectCollapse : TransitionSet() {
    init {
        ordering = ORDERING_SEQUENTIAL
        interpolator = AccelerateDecelerateInterpolator()

        addTransition(ChangeBounds().apply {
            duration = 250
        })

        addTransition(ElevationTransition().apply {
            duration = 50
        })
    }
}