package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.MainPage

interface MainViewModel {

    val previousPage: MainPage?

    val currentPage: LiveData<MainPage>

    val isFabPage: Boolean

    fun changePage(page: MainPage)

}