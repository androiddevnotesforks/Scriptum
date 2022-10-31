package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.test.prod.RunPrivate

class MainViewModelImpl : ViewModel(),
    MainViewModel {

    override var previousPage: MainPage? = null

    override val currentPage: MutableLiveData<MainPage> = MutableLiveData(MainPage.NOTES)

    override val isFabPage: Boolean get() = currentPage.value == MainPage.NOTES

    override fun changePage(page: MainPage) {
        previousPage = currentPage.value
        currentPage.postValue(page)
    }

    //region cleanup

    /**
     * Key for detect application start and pageTo == [pageFrom] inside [onSelectItem].
     */
    @RunPrivate var isFirstStart: Boolean = true

    @RunPrivate var pageFrom: MainPage = START_PAGE

    override val isStartPage: Boolean get() = pageFrom == START_PAGE

    //    override fun onSetup(bundle: Bundle?) {
    //        if (bundle != null) {
    //            isFirstStart = bundle.getBoolean(Intent.FIRST_START)
    //
    //            val pageOrdinal = bundle.getInt(Intent.PAGE_CURRENT)
    //            pageFrom = MainPage.values().getOrNull(pageOrdinal) ?: START_PAGE
    //        }
    //
    //        callback?.setupNavigation(pageFrom.getMenuId())
    //        callback?.setupInsets()
    //
    //        if (bundle != null) {
    //            callback?.changeFabVisible(pageFrom.isStartPage(), withGap = false)
    //        }
    //    }
    //
    //    private fun MainPage.getMenuId(): Int = let {
    //        return@let when (it) {
    //            MainPage.RANK -> R.id.item_page_rank
    //            MainPage.NOTES -> R.id.item_page_notes
    //            MainPage.BIN -> R.id.item_page_bin
    //        }
    //    }
    //
    //    override fun onSelectItem(@IdRes itemId: Int) {
    //        val pageTo = itemId.getPageById() ?: return
    //
    //        if (!isFirstStart && pageTo == pageFrom) {
    //            callback?.scrollTop(pageTo)
    //        } else {
    //            isFirstStart = false
    //
    //            callback?.apply {
    //                changeFabVisible(pageTo.isStartPage(), withGap = false)
    //                showPage(pageFrom, pageTo)
    //            }
    //        }
    //
    //        pageFrom = pageTo
    //    }
    //
    //    private fun Int.getPageById(): MainPage? = let {
    //        return@let when (it) {
    //            R.id.item_page_rank -> MainPage.RANK
    //            R.id.item_page_notes -> MainPage.NOTES
    //            R.id.item_page_bin -> MainPage.BIN
    //            else -> null
    //        }
    //    }
    //
    //    private fun MainPage.isStartPage() = this == START_PAGE

    //endregion

    companion object {
        private val START_PAGE = MainPage.NOTES
    }
}