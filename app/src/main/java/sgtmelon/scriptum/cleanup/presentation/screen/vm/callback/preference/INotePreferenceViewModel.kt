package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

interface INotePreferenceViewModel {

    fun onClickSort()

    fun onResultNoteSort(value: Int)

    fun onClickNoteColor()

    fun onResultNoteColor(value: Int)

    fun onClickSaveTime()

    fun onResultSaveTime(value: Int)

}