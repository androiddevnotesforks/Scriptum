package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

class MainViewModelImpl(
    private val preferencesRepo: PreferencesRepo
) : ViewModel(),
    MainViewModel {

    // TODO tests
    override val defaultColor: Color get() = preferencesRepo.defaultColor

    override var previousPage: MainPage? = null

    override val currentPage: MutableLiveData<MainPage> = MutableLiveData(MainPage.NOTES)

    override val isFabPage: Boolean get() = currentPage.value == MainPage.NOTES

    override fun changePage(page: MainPage) {
        previousPage = currentPage.value
        currentPage.postValue(page)
    }
}