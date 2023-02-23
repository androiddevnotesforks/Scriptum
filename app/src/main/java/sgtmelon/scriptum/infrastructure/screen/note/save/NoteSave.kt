package sgtmelon.scriptum.infrastructure.screen.note.save

interface NoteSave {

    fun skipPauseSave()

    fun changeAutoSaveWork(isWork: Boolean)
}