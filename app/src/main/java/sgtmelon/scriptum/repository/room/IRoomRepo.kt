package sgtmelon.scriptum.repository.room

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Интерфейс для общения с [RoomRepo]
 *
 * @author SerjantArbuz
 */
interface IRoomRepo {

    fun getNoteModelList(bin: Boolean): MutableList<NoteModel>

    suspend fun clearBin()

    suspend fun deleteNote(noteEntity: NoteEntity)

    suspend fun restoreNote(noteEntity: NoteEntity)

    /**
     * Полное удаление заметки из базы данных и очистка категорий от неё
     */
    suspend fun clearNote(noteEntity: NoteEntity)

    fun getRankIdVisibleList(): List<Long>

    fun getRankCount(): Boolean

    /**
     * @throws NullPointerException когда заметка не сохранена в базу данных и [NoteEntity.id] == [NoteData.Default.ID]
     */
    fun getNoteModel(id: Long): NoteModel

    /**
     * @return Список с именами всех категорий
     */
    fun getRankNameList(): List<String>

    /**
     * @return Булевый массив с true - для категорий, принадлежащих заметке
     */
    fun getRankCheckArray(noteEntity: NoteEntity): BooleanArray

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.TEXT]
     */
    fun convertToRoll(noteModel: NoteModel): NoteModel

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun convertToText(noteModel: NoteModel): NoteModel

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun getRollListString(noteEntity: NoteEntity): String

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun getRollStatusString(noteEntity: NoteEntity): String

    fun getRankIdList(): List<Long>

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.TEXT]
     */
    fun saveTextNote(noteModel: NoteModel, isCreate: Boolean): NoteModel

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun saveRollNote(noteModel: NoteModel, isCreate: Boolean): NoteModel


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