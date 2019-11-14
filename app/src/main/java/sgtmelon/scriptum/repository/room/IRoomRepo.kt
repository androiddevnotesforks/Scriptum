package sgtmelon.scriptum.repository.room

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communicate with [RoomRepo]
 */
interface IRoomRepo {

    fun getNoteModelList(bin: Boolean): MutableList<NoteModel>

    /**
     * Have hide notes in list or not
     */
    fun isListHide(bin: Boolean): Boolean

    suspend fun clearBin()

    suspend fun deleteNote(noteModel: NoteModel)

    suspend fun restoreNote(noteModel: NoteModel)

    /**
     * Delete of note from data base and clear categories
     */
    suspend fun clearNote(noteModel: NoteModel)


    /**
     * Return null if note not found in DataBase or [NoteEntity.id] == [NoteData.Default.ID]
     */
    fun getNoteModel(id: Long): NoteModel?


    fun convertToRoll(noteModel: NoteModel)

    fun convertToText(noteModel: NoteModel)

    // TODO to another repo
    suspend fun getCopyText(noteEntity: NoteEntity):String

    fun saveTextNote(noteModel: NoteModel, isCreate: Boolean)

    fun saveRollNote(noteModel: NoteModel, isCreate: Boolean)


    fun updateRollCheck(noteEntity: NoteEntity, rollEntity: RollEntity)

    fun updateRollCheck(noteEntity: NoteEntity, check: Boolean)

    fun updateNote(noteEntity: NoteEntity)

}