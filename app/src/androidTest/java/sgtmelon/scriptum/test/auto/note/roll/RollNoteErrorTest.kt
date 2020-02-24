package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test fix of old errors for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteErrorTest : ParentUiTest() {

    /**
     * Тут проверяются сразу две ошибки связанные друг с другом (необходимо делать по порядку):
     *
     * 1. В режиме редактирования списка нужно удалить текст с пункта и
     *    сохранить (способ сохранения не важен).
     *
     *    Причина:
     *    Использование библиотеки DiffUtil. Она не знала, что делать с пунктами, у которых не
     *    присвоен id.
     *
     *    Происходит:
     *    ~ Пункт медленно удаляется.
     *    ~ Если пункт в середине, то происходит баг.
     *
     * 2. Записывается не правильное количество пунктов (можно видеть в карточке на главном экране).
     *
     *    Причина:
     *    Пустые пункты удалялись из списка модели после того, как происходил подсчёт выполненых
     *    пунктов.
     *
     *    Происходит:
     *    После выполнения действий из первого бага.
     *
     */
    @Test fun removeEmptyItemsAfterChangeDone() {
        var item = data.createRoll()

        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(item) {
                            enterPanel { repeat(times = 4) { onAdd(data.uniqueString) } }
                            onEnterText()

                            controlPanel { onSave() }
                            onAssertAll()

                            item = noteItem
                            onPressBack()
                        }
                    }

                    onAssertItem(item)
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
    @Test fun restoreChanges() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickCheck()
                        controlPanel { onEdit() }
                        toolbar { onClickBack() }

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