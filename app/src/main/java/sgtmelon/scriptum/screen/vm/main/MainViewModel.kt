package sgtmelon.scriptum.screen.vm.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.receiver.MainReceiver
import sgtmelon.scriptum.screen.callback.main.IMainActivity
import sgtmelon.scriptum.screen.view.main.MainActivity

/**
 * ViewModel для [MainActivity]
 *
 * @author SerjantArbuz
 */
class MainViewModel : ViewModel(), MainReceiver.Callback {

    lateinit var callback: IMainActivity

    private var firstStart: Boolean = true
    private var pageFrom: MainPage = MainPage.NOTES

    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) pageFrom = MainPage.values()[bundle.getInt(PAGE_CURRENT)]

        callback.setupNavigation(pageItemId[pageFrom.ordinal])

        if (bundle != null) callback.changeFabState(state = pageFrom == MainPage.NOTES)
    }

    fun onSaveData(bundle: Bundle) = bundle.putInt(PAGE_CURRENT, pageFrom.ordinal)

    fun onSelectItem(@IdRes itemId: Int): Boolean {
        val pageTo = itemId.getPageById()

        if (!firstStart && pageTo == pageFrom) {
            callback.scrollTop(pageTo)
        } else {
            if (firstStart) firstStart = false

            callback.apply {
                changeFabState(state = pageTo == MainPage.NOTES)
                showPage(pageFrom, pageTo)
            }
        }

        pageFrom = pageTo

        return true
    }

    override fun onReceiveUnbindNote(id: Long) {
        if (pageFrom == MainPage.NOTES) callback.onCancelNoteBind(id)
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