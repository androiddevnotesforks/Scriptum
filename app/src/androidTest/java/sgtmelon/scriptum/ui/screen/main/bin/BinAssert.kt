package sgtmelon.scriptum.ui.screen.main.bin

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class BinAssert : BasicMatch() {

    fun onDisplayContent() { // TODO(Отображение списка/информации от bool) (объединить методы)
        onDisplayToolbar(R.id.toolbar, R.string.title_bin)

//        doesNotDisplay(R.id.clear_item) //TODO исправить (может и быть)
    }

    fun onDisplayList() = onDisplay(R.id.bin_recycler)

    fun doesNotDisplayList() = doesNotDisplay(R.id.bin_recycler)

    fun onDisplayInfo() {
        onDisplay(R.id.info_title_text, R.string.info_bin_title)
        onDisplay(R.id.info_details_text, R.string.info_bin_details)
    }

    fun doesNotDisplayInfo() {
        doesNotDisplay(R.id.info_title_text, R.string.info_bin_title)
        doesNotDisplay(R.id.info_details_text, R.string.info_bin_details)
    }

}