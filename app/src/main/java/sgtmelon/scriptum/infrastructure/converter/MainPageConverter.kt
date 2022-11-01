package sgtmelon.scriptum.infrastructure.converter

import android.view.MenuItem
import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.exception.converter.ConverterException
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.utils.record

class MainPageConverter {

    @IdRes
    fun convert(page: MainPage?): Int {
        val id = when (page) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
            else -> INVALID_ID
        }

        if (id == INVALID_ID) {
            ConverterException(desc = "mainPage to itemId").record()
        }

        return id
    }

    fun convert(item: MenuItem): MainPage? {
        val page = when (item.itemId) {
            R.id.item_page_rank -> MainPage.RANK
            R.id.item_page_notes -> MainPage.NOTES
            R.id.item_page_bin -> MainPage.BIN
            else -> null
        }

        if (page == null) {
            ConverterException(desc = "menuItem to mainPage").record()
        }

        return page
    }

    companion object {
        const val INVALID_ID = -1
    }
}