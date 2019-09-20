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

package eu.insertcode.portfolio.data.model

import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.model.Project.Type
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Created by maartendegoede on 12/10/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class ProjectTest {

    @Test
    fun test_indicatorColor() {
        assertEquals(Type.APP.color, R.color.indicatorType_app)
        assertEquals(Type.WEB.color, R.color.indicatorType_web)
        assertEquals(Type.GAME.color, R.color.indicatorType_game)
        assertEquals(Type.WATCH.color, R.color.indicatorType_watch)
        assertEquals(Type.OTHER.color, R.color.indicatorType_other)
    }

    @Test
    fun test_indicatorIcon() {
        assertEquals(Type.APP.icon, R.drawable.ic_type_app)
        assertEquals(Type.WEB.icon, R.drawable.ic_type_web)
        assertEquals(Type.GAME.icon, R.drawable.ic_type_game)
        assertEquals(Type.WATCH.icon, R.drawable.ic_type_watch)
        assertEquals(Type.OTHER.icon, R.drawable.ic_type_other)
    }

}