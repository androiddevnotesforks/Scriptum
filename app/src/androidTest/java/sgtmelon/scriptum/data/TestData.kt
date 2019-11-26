package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.extension.getTime
import sgtmelon.scriptum.basic.extension.getFutureTime
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.converter.AlarmConverter
import sgtmelon.scriptum.room.converter.NoteConverter
import sgtmelon.scriptum.room.converter.RankConverter
import sgtmelon.scriptum.room.converter.RollConverter
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity
import java.util.UUID.randomUUID
import kotlin.random.Random

class TestData(override val context: Context, private val iPreferenceRepo: IPreferenceRepo) :
        IRoomWork {

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()
    private val rankConverter = RankConverter()
    private val alarmConverter = AlarmConverter()

    val uniqueString get() = randomUUID().toString().substring(0, 16)

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = uniqueString
            text = uniqueString.repeat(n = (1 until 10).random())
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = uniqueString
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val rollList: ArrayList<RollEntity>
        get() = ArrayList<RollEntity>().apply {
            (0 until (1 until 6).random()).forEach {
                add(rollEntity.apply {
                    position = it
                    text = "$it | $text"
                })
            }
        }

    val rollEntity: RollEntity
        get() = RollEntity().apply {
            isCheck = Random.nextBoolean()
            text = uniqueString
        }

    val rankEntity: RankEntity get() = RankEntity(name = uniqueString)


    fun createText() = NoteItem.getCreate(iPreferenceRepo.defaultColor, NoteType.TEXT)

    fun createRoll() = NoteItem.getCreate(iPreferenceRepo.defaultColor, NoteType.ROLL)


    fun insertRank(entity: RankEntity = rankEntity): RankItem {
        inRoom { entity.id = iRankDao.insert(entity) }

        return rankConverter.toItem(entity)
    }

    fun insertRankForNotes(): RankItem {
        val noteItem = insertNote()

        val rankItem = insertRank(rankEntity.apply {
            noteId.add(noteItem.id)
        })

        inRoom {
            iNoteDao.update(noteConverter.toEntity(noteItem.apply {
                rankId = rankItem.id
                rankPs = rankItem.position
            }))
        }

        return rankItem
    }

    fun insertRankForBin(): RankItem {
        val noteItem = insertNoteToBin()

        val rankItem = insertRank(rankEntity.apply {
            noteId.add(noteItem.id)
        })

        inRoom {
            iNoteDao.update(noteConverter.toEntity(noteItem.apply {
                rankId = rankItem.id
                rankPs = rankItem.position
            }))
        }

        return rankItem
    }

    fun insertText(entity: NoteEntity = textNote): NoteItem {
        if (entity.type != NoteType.TEXT) throw IllegalAccessException("Wrong note type")

        inRoom { entity.id = iNoteDao.insert(entity) }

        return noteConverter.toItem(entity)
    }

    fun insertTextToBin(note: NoteEntity = textNote) = insertText(note.apply { isBin = true })

    fun insertRoll(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList): NoteItem {
        if (note.type != NoteType.ROLL) throw IllegalAccessException("Wrong note type")

        inRoom {
            note.id = iNoteDao.insert(note)
            list.forEach {
                it.noteId = note.id
                iRollDao.insert(it)
            }
        }

        // TODO REFACTOR
        val item = noteConverter.toItem(note, rollConverter.toItem(list)).apply {
            updateComplete()
        }

        inRoom { iNoteDao.update(noteConverter.toEntity(item)) }

        return item
    }

    fun insertRollToBin(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList) =
            insertRoll(note.apply { isBin = true }, list)


    fun insertNote(): NoteItem = if (Random.nextBoolean()) insertText() else insertRoll()

    fun insertNoteToBin(): NoteItem =
            if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()

    fun insertNotification(noteItem: NoteItem = insertNote(),
                           date: String = getFutureTime()): NoteItem {
        noteItem.alarmDate = date

        inRoom { noteItem.alarmId = iAlarmDao.insert(alarmConverter.toEntity(noteItem)) }

        return noteItem
    }


    fun fillRank(count: Int = 10) = ArrayList<RankItem>().apply {
        (0 until count).forEach {
            add(insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()
            }))
        }
    }

    fun fillRankRelation(count: Int = 10) = ArrayList<RankItem>().apply {
        (0 until count).forEach {
            val noteCount = (0 until 5).random()
            val noteList = ArrayList<NoteItem>().apply {
                repeat(noteCount) { add(insertNote()) }
            }

            val rankEntity = insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()

                noteList.map { it.id }.forEach { noteId.add(it) }
            })

            add(rankEntity)

            inRoom {
                noteList.forEach {
                    iNoteDao.update(noteConverter.toEntity(it.apply {
                        rankId = rankEntity.id
                        rankPs = rankEntity.position
                    }))
                }
            }
        }
    }

    fun fillNotes(count: Int = 10) = ArrayList<NoteItem>().apply {
        repeat(count) { add(insertNote()) }
    }

    fun fillBin(count: Int = 10) = ArrayList<NoteItem>().apply {
        repeat(count) { add(insertNoteToBin()) }
    }

    fun fillNotification(count: Int = 10) = repeat(count) {
        insertNotification(date = getFutureTime())
    }


    fun clear() = apply { inRoom { clearAllTables() } }

}