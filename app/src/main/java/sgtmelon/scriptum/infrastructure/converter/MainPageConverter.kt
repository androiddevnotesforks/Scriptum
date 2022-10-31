package sgtmelon.scriptum.infrastructure.converter

import android.view.MenuItem
import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.MainPage

// TODO add tests
class MainPageConverter {

    @IdRes
    fun convert(page: MainPage?): Int {
        return when (page) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
            else -> -1
        }
    }

    // TODO add null check (record null result)
    fun convert(item: MenuItem): MainPage? {
        return when (item.itemId) {
            R.id.item_page_rank -> MainPage.RANK
            R.id.item_page_notes -> MainPage.NOTES
            R.id.item_page_bin -> MainPage.BIN
            else -> null
        }
    }
}