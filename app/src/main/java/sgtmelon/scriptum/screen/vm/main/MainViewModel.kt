package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData.Filter.MAIN
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

    private var pageFrom: MainPage = MainPage.NOTES

    override fun onSetup(bundle: Bundle?) {
        bundle?.let {
            firstStart = it.getBoolean(FIRST_START)
            pageFrom = MainPage.values()[it.getInt(PAGE_CURRENT)]
        }

        callback?.setupNavigation(pageFrom.getMenuId())

        bundle?.let { callback?.changeFabState(state = pageFrom == MainPage.NOTES) }
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
                changeFabState(state = pageTo == MainPage.NOTES)
                showPage(pageFrom, pageTo)
            }
        }

        pageFrom = pageTo
    }

    override fun onResultAddDialog(menuItem: MenuItem) {
        val noteType = NoteData.getTypeById(menuItem.itemId) ?: return
        callback?.startNoteActivity(noteType)
    }

    override fun onReceiveUnbindNote(id: Long) {
        if (pageFrom == MainPage.NOTES) callback?.onCancelNoteBind(id)
    }


    private fun MainPage.getMenuId(): Int = when(this) {
        MainPage.RANK -> R.id.item_page_rank
        MainPage.NOTES -> R.id.item_page_notes
        MainPage.BIN -> R.id.item_page_bin
    }

    private fun Int.getPageById(): MainPage? = when (this) {
        R.id.item_page_rank -> MainPage.RANK
        R.id.item_page_notes -> MainPage.NOTES
        R.id.item_page_bin -> MainPage.BIN
        else -> null
    }

    private companion object {
        const val PREFIX = "MAIN"

        const val FIRST_START = "${PREFIX}_FIRST_START"
        const val PAGE_CURRENT = "${PREFIX}_PAGE_CURRENT"
    }

}