package sgtmelon.scriptum.model

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.UnbindReceiver
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.ui.SplashActivity

/**
 * Model for [BindControl]
 */
class BindModel(private val noteModel: NoteModel) {

    /**
     * Constructor for load rollList separately
     */
    constructor(noteEntity: NoteEntity, rollList: MutableList<RollEntity>) :
            this(NoteModel(noteEntity, rollList))

    val id = noteModel.noteEntity.id.toInt()

    val isStatus = noteModel.noteEntity.isStatus

    fun isVisible(rankIdVisibleList: List<Long>) =
            noteModel.noteEntity.isVisible(rankIdVisibleList)

    val icon: Int = when (noteModel.noteEntity.type) {
        NoteType.TEXT -> R.drawable.notif_bind_text
        NoteType.ROLL -> R.drawable.notif_bind_roll
    }

    fun getTitle(context: Context): String = noteModel.getStatusTitle(context)

    val text: String = when (noteModel.noteEntity.type) {
        NoteType.TEXT -> noteModel.noteEntity.text
        NoteType.ROLL -> noteModel.rollList.toStatusText()
    }

    fun getColor(context: Context) =
            context.getAppSimpleColor(noteModel.noteEntity.color, ColorShade.DARK)

    fun getContentIntent(context: Context): PendingIntent? = TaskStackBuilder.create(context)
            .addNextIntent(SplashActivity.getBindInstance(context, noteModel.noteEntity))
            .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

    fun getUnbindIntent(context: Context) = UnbindReceiver[context, noteModel.noteEntity]



    private fun NoteModel.getStatusTitle(context: Context): String = with(noteEntity) {
        (if (type == NoteType.ROLL) "$text | " else "")
                .plus(if (name.isEmpty()) context.getString(R.string.hint_text_name) else name)
    }

    private fun List<RollEntity>.toStatusText() = joinToString(separator = "\n") {
        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
    }

}