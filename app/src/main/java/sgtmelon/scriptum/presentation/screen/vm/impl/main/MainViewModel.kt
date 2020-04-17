package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IMainViewModel

/**
 * ViewModel for [MainActivity].
 */
class MainViewModel(application: Application) : ParentViewModel<IMainActivity>(application),
        IMainViewModel {

    private lateinit var interactor: IMainInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: IMainInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


    /**
     * Key for detect application start and pageTo == [pageFrom] inside [onSelectItem].
     */
    @VisibleForTesting
    var firstStart: Boolean = true

    @VisibleForTesting
    var pageFrom: MainPage = START_PAGE

    override fun onSetup(bundle: Bundle?) {
        if (bundle == null) {
            /**
             * Work with alarm and notification coroutines need do here.
             * Because [SplashActivity] will be quickly destroyed.
             */
            viewModelScope.launch {
                interactor.tidyUpAlarm()
                bindInteractor.notifyNoteBind(callback)
                bindInteractor.notifyInfoBind(callback)
            }
        } else {
            firstStart = bundle.getBoolean(FIRST_START)
            pageFrom = MainPage.values().getOrNull(bundle.getInt(PAGE_CURRENT)) ?: START_PAGE
        }

        callback?.setupNavigation(pageFrom.getMenuId())

        if (bundle != null) {
            callback?.setFabState(pageFrom.isStartPage())
        }
    }

    private fun MainPage.getMenuId(): Int = let {
        return@let when (it) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putBoolean(FIRST_START, firstStart)
        putInt(PAGE_CURRENT, pageFrom.ordinal)
    }

    override fun onSelectItem(@IdRes itemId: Int) {
        val pageTo = itemId.getPageById() ?: return

        if (!firstStart && pageTo == pageFrom) {
            callback?.scrollTop(pageTo)
        } else {
            firstStart = false

            callback?.apply {
                setFabState(pageTo.isStartPage())
                showPage(pageFrom, pageTo)
            }
        }

        pageFrom = pageTo
    }

    private fun Int.getPageById(): MainPage? = let {
        return@let when (it) {
            R.id.item_page_rank -> MainPage.RANK
            R.id.item_page_notes -> MainPage.NOTES
            R.id.item_page_bin -> MainPage.BIN
            else -> null
        }
    }

    /**
     * Change FAB state consider on [pageFrom].
     */
    override fun onFabStateChange(state: Boolean) {
        callback?.setFabState(pageFrom.isStartPage() && state)
    }

    override fun onResultAddDialog(@IdRes itemId: Int) {
        callback?.startNoteActivity(noteType = getTypeById(itemId) ?: return)
    }

    private fun getTypeById(@IdRes itemId: Int): NoteType? = when (itemId) {
        R.id.item_add_text -> NoteType.TEXT
        R.id.item_add_roll -> NoteType.ROLL
        else -> null
    }


    override fun onReceiveUnbindNote(id: Long) {
        callback?.onReceiveUnbindNote(id)
    }

    override fun onReceiveUpdateAlarm(id: Long) {
        if (!pageFrom.isStartPage()) callback?.onReceiveUpdateAlarm(id)
    }


    private fun MainPage.isStartPage() = this == START_PAGE

    companion object {
        private val START_PAGE = MainPage.NOTES

        private const val PREFIX = "MAIN"

        @VisibleForTesting
        const val FIRST_START = "${PREFIX}_FIRST_START"

        @VisibleForTesting
        const val PAGE_CURRENT = "${PREFIX}_PAGE_CURRENT"
    }

}