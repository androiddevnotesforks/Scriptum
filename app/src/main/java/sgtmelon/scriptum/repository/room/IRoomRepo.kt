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
     * Полное удаление заметки из базы данных и очистка категорий от неё
     */
    suspend fun clearNote(noteModel: NoteModel)

    fun getRankIdVisibleList(): List<Long>

    fun isRankEmpty(): Boolean

    /**
     * Return null if note not found in DataBase or [NoteEntity.id] == [NoteData.Default.ID]
     */
    fun getNoteModel(id: Long): NoteModel?

    /**
     * @return Список с именами всех категорий
     */
    fun getRankDialogItemArray(): Array<String>

    fun convertToRoll(noteModel: NoteModel)

    fun convertToText(noteModel: NoteModel)

    fun getRollListString(noteEntity: NoteEntity): String

    fun getRankIdList(): List<Long>

    fun saveTextNote(noteModel: NoteModel, isCreate: Boolean)

    fun saveRollNote(noteModel: NoteModel, isCreate: Boolean)


    /**
     * Обновление конкретного пункта списка
     */
    fun updateRollCheck(noteEntity: NoteEntity, rollEntity: RollEntity)

    /**
     * Обновление всех пунктов списка
     *
     * @param check состояние для всех пунктов
     */
    fun updateRollCheck(noteEntity: NoteEntity, check: Boolean)

    fun updateNote(noteEntity: NoteEntity)

}