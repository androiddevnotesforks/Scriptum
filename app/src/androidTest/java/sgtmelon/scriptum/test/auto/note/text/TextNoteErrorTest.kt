package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test fix of old errors for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteErrorTest : ParentUiTest() {

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
    @Test fun restoreChanges() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onBind().onEdit() }
                        toolbar { onClickBack() }

                        controlPanel {
                            onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                            onEdit()
                        }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

}