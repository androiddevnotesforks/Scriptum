package sgtmelon.scriptum.app.screen.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.app.model.key.MainPage

/**
 * ViewModel для [MainActivity]
 */
class MainViewModel : ViewModel() {

    lateinit var callback: MainCallback

    private var firstStart: Boolean = true
    private var page: Int = MainPage.Name.NOTES.ordinal

    fun setupData(bundle: Bundle?) {
        if (bundle != null) page = bundle.getInt(PAGE_CURRENT)

        callback.setupNavigation(MainPage.id[page])
    }

    fun saveData(bundle: Bundle) = bundle.putInt(PAGE_CURRENT, page)

    fun onSelectItem(@IdRes itemId: Int): Boolean {
        val pageFrom = MainPage.Name.values()[page]
        val pageTo = MainPage.getById(itemId)

        page = pageTo.ordinal

        if (!firstStart && pageTo == pageFrom) {
            callback.scrollTop(pageTo)
        } else {
            if (firstStart) firstStart = false

            callback.changeFabState(state = pageTo == MainPage.Name.NOTES)
            callback.showPage(pageFrom, pageTo)
        }

        return true
    }

    companion object {
        private const val PAGE_CURRENT = "INSTANCE_MAIN_PAGE_CURRENT"
    }


}