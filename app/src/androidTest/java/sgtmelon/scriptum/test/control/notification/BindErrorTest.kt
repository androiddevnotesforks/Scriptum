package sgtmelon.scriptum.test.control.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test fix of old errors for bind in status bar.
 */
@RunWith(AndroidJUnit4::class)
class BindErrorTest : ParentNotificationTest() {

    /**
     * При изменениях основной модели (noteItem) в read mode они не применяются к
     * модели восстановления (restoreItem).
     *
     * Причина:
     * Ошибка происходила из-за того, что при изменениях в read mode они не применялись для
     * модели восстановления (restoreItem).
     *
     * Важно:
     * Наобходимо проверить все изменения, которые можно сделать в read mode.
     *
     * Получение бага:
     * ~ Действие в read mode -> переход к редактированию -> нажать на крестик.
     *
     * Пример:
     * ~ Зайти в список -> тыкнуть на отметку пункта -> изменить -> крестик (отменить).
     * ~ Зайти в текст -> тыкнуть на bind -> изменить -> крестик (отменить).
     */
    @Test fun textNoteUnbindOnRestore() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        onOpen { noteItem.isStatus = false }
                        controlPanel { onEdit() }
                        onEnterText(data.uniqueString)
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    /**
     * При изменениях основной модели (noteItem) в read mode они не применяются к
     * модели восстановления (restoreItem).
     *
     * Причина:
     * Ошибка происходила из-за того, что при изменениях в read mode они не применялись для
     * модели восстановления (restoreItem).
     *
     * Важно:
     * Наобходимо проверить все изменения, которые можно сделать в read mode.
     *
     * Получение бага:
     * ~ Действие в read mode -> переход к редактированию -> нажать на крестик.
     *
     * Пример:
     * ~ Зайти в список -> тыкнуть на отметку пункта -> изменить -> крестик (отменить).
     * ~ Зайти в текст -> тыкнуть на bind -> изменить -> крестик (отменить).
     */
    @Test fun rollNoteUnbindOnRestore() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onOpen { noteItem.isStatus = false }
                        controlPanel { onEdit() }
                        enterPanel { onAdd(data.uniqueString) }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }


}