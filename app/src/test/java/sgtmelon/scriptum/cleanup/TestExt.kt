package sgtmelon.scriptum.cleanup

import io.mockk.MockKVerificationScope
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

fun MockKVerificationScope.verifyDeepCopy(
    item: NoteItem.Text,
    id: Long? = null,
    create: String? = null,
    change: String? = null,
    name: String? = null,
    text: String? = null,
    color: Int? = null,
    rankId: Long? = null,
    rankPs: Int? = null,
    isBin: Boolean? = null,
    isStatus: Boolean? = null,
    alarmId: Long? = null,
    alarmDate: String? = null
) {
    if (id == null) item.id
    if (create == null) item.create
    if (change == null) item.change
    if (name == null) item.name
    if (text == null) item.text
    if (color == null) item.color
    if (rankId == null) item.rankId
    if (rankPs == null) item.rankPs
    if (isBin == null) item.isBin
    if (isStatus == null) item.isStatus
    if (alarmId == null) item.alarmId
    if (alarmDate == null) item.alarmDate

    item.deepCopy(
        any(), any(), any(), any(), any(), any(),
        any(), any(), any(), any(), any(), any()
    )
}

fun MockKVerificationScope.verifyDeepCopy(
    item: NoteItem.Roll,
    id: Long? = null,
    create: String? = null,
    change: String? = null,
    name: String? = null,
    text: String? = null,
    color: Int? = null,
    rankId: Long? = null,
    rankPs: Int? = null,
    isBin: Boolean? = null,
    isStatus: Boolean? = null,
    alarmId: Long? = null,
    alarmDate: String? = null,
    isVisible: Boolean? = null,
    list: MutableList<RollItem>? = null
) {
    if (id == null) item.id
    if (create == null) item.create
    if (change == null) item.change
    if (name == null) item.name
    if (text == null) item.text
    if (color == null) item.color
    if (rankId == null) item.rankId
    if (rankPs == null) item.rankPs
    if (isBin == null) item.isBin
    if (isStatus == null) item.isStatus
    if (alarmId == null) item.alarmId
    if (alarmDate == null) item.alarmDate

    // TODO check tests with this code (it will fail in deep copy) (may be it related with public values?)
    //    if (isVisible == null) item.isVisible
    //    if (list == null) item.list

    item.deepCopy(
        any(), any(), any(), any(), any(), any(), any(),
        any(), any(), any(), any(), any(), any(), any()
    )
}
