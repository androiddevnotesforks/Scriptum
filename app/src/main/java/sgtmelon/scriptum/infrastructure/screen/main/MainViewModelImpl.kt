package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.key.MainPage

class MainViewModelImpl : ViewModel(),
    MainViewModel {

    override var previousPage: MainPage? = null

    override val currentPage: MutableLiveData<MainPage> = MutableLiveData(MainPage.NOTES)

    override val isFabPage: Boolean get() = currentPage.value == MainPage.NOTES

    override fun changePage(page: MainPage) {
        previousPage = currentPage.value
        currentPage.postValue(page)
    }
}