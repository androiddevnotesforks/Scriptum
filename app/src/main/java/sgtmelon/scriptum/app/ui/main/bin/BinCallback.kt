package sgtmelon.scriptum.app.ui.main.bin

import sgtmelon.scriptum.app.model.NoteRepo

interface BinCallback {

    fun bind()

    fun notifyMenuClearBin()

    fun scrollTop()

    fun notifyDataSetChanged(list: MutableList<NoteRepo>)

    fun notifyItemRemoved(list: MutableList<NoteRepo>, p: Int)

}