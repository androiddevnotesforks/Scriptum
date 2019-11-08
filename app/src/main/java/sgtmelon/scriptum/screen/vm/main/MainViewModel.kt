package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IMainViewModel

/**
 * ViewModel for [MainActivity]
 */
class MainViewModel(application: Application) : ParentViewModel<IMainActivity>(application),
        IMainViewModel {

    /**
     * Key for detect application start and pageTo == [pageFrom] inside [onSelectItem]
     */
    private var firstStart: Boolean = true

    private var pageFrom: MainPage = START_PAGE

    override fun onSetup(bundle: Bundle?) {
        bundle?.let {
            firstStart = it.getBoolean(FIRST_START)
            pageFrom = MainPage.values()[it.getInt(PAGE_CURRENT)]
        }

        callback?.setupNavigation(pageFrom.getMenuId())

        bundle?.let { callback?.setFabState(state = pageFrom == MainPage.NOTES) }
    }


    override fun onSaveData(bundle: Bundle) = bundle.let {
        it.putBoolean(FIRST_START, firstStart)
        it.putInt(PAGE_CURRENT, pageFrom.ordinal)
    }

    override fun onSelectItem(@IdRes itemId: Int) {
        val pageTo = itemId.getPageById() ?: return

        if (!firstStart && pageTo == pageFrom) {
            callback?.scrollTop(pageTo)
        } else {
            if (firstStart) firstStart = false

            callback?.apply {
                setFabState(pageTo.isStartPage())
                showPage(pageFrom, pageTo)
            }
        }

        pageFrom = pageTo
    }

    /**
     * Change FAB state consider [pageFrom].
     */
    override fun onFabStateChange(state: Boolean) {
        callback?.setFabState(pageFrom.isStartPage() && state)
    }

    override fun onResultAddDialog(menuItem: MenuItem) {
        val noteType = NoteData.getTypeById(menuItem.itemId) ?: return
        callback?.startNoteActivity(noteType)
    }


    override fun onReceiveUnbindNote(id: Long) {
        if (pageFrom.isStartPage()) callback?.onCancelNoteBind(id)
    }

    override fun onReceiveUpdateAlarm(id: Long) {
        if (pageFrom.isStartPage()) callback?.onUpdateAlarm(id)
    }


    private fun MainPage.getMenuId(): Int = let {
        return@let when (it) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
        }
    }

    private fun Int.getPageById(): MainPage? = let {
        return@let when (it) {
            R.id.item_page_rank -> MainPage.RANK
            R.id.item_page_notes -> MainPage.NOTES
            R.id.item_page_bin -> MainPage.BIN
            else -> null
        }
    }

    private fun MainPage.isStartPage() = this == START_PAGE

    private companion object {
        val START_PAGE = MainPage.NOTES

        const val PREFIX = "MAIN"

        const val FIRST_START = "${PREFIX}_FIRST_START"
        const val PAGE_CURRENT = "${PREFIX}_PAGE_CURRENT"
    }

}