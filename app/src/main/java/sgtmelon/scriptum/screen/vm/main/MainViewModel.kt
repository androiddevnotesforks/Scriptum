package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.callback.main.IMainActivity
import sgtmelon.scriptum.screen.callback.main.IMainViewModel
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel for [MainActivity]
 *
 * @author SerjantArbuz
 */
class MainViewModel(application: Application) : ParentViewModel<IMainActivity>(application),
        IMainViewModel {

    private var firstStart: Boolean = true
    private var pageFrom: MainPage = MainPage.NOTES

    override fun onSetupData(bundle: Bundle?) {
        bundle?.let { pageFrom = MainPage.values()[it.getInt(PAGE_CURRENT)] }

        callback?.setupNavigation(pageItemId[pageFrom.ordinal])

        bundle?.let { callback?.changeFabState(state = pageFrom == MainPage.NOTES) }
    }

    override fun onSaveData(bundle: Bundle) = bundle.putInt(PAGE_CURRENT, pageFrom.ordinal)

    override fun onSelectItem(@IdRes itemId: Int): Boolean {
        val pageTo = itemId.getPageById()

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

        return true
    }

    override fun onResultAddDialog(menuItem: MenuItem) {
        val noteType = NoteData.getTypeById(menuItem.itemId) ?: return
        callback?.startActivity(NoteActivity.getInstance(context, noteType))
    }

    override fun onReceiveUnbindNote(id: Long) {
        if (pageFrom == MainPage.NOTES) callback?.onCancelNoteBind(id)
    }

    companion object {
        private const val PAGE_CURRENT = "INSTANCE_MAIN_PAGE_CURRENT"

        private val pageItemId =
                intArrayOf(R.id.item_page_rank, R.id.item_page_notes, R.id.item_page_bin)

        private fun Int.getPageById(): MainPage = when (this) {
            R.id.item_page_rank -> MainPage.RANK
            R.id.item_page_notes -> MainPage.NOTES
            R.id.item_page_bin -> MainPage.BIN
            else -> throw NoSuchFieldException("Id doesn't match any of page")
        }
    }

}