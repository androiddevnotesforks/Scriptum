package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface MainViewModel {

    val defaultColor: Color

    val previousPage: MainPage?

    val currentPage: LiveData<MainPage>

    val isFabPage: Boolean

    fun changePage(page: MainPage)

}