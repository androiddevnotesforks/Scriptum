package sgtmelon.scriptum.office.intf

interface MenuIntf {

    /**
     * Интерфейс для меню внутри заметки
     */
    interface Note {

        interface DeleteMenuClick {
            fun onMenuRestoreClick()

            fun onMenuRestoreOpenClick()

            fun onMenuClearClick()

            fun onMenuDeleteClick()
        }

        interface NoteMenuClick { // TODO: 10.12.2018 добавить длинное нажатие на кнопку undo/redo - для возвращение в один из концов
            /**
             * При успешном сохранении возвращает - true
             * [modeChange] - Надо ли менять режим редактирования,
             * [showToast]  - Показывать уведомление о том, что заметка пуста
             */
            fun onMenuSaveClick(modeChange: Boolean, showToast: Boolean): Boolean

            fun onUndoClick()

            fun onRedoClick()

            fun onMenuRankClick()

            fun onMenuColorClick()

            /**
             * [editMode] - Установка режима редактирования
             */
            fun onMenuEditClick(editMode: Boolean)

            fun onMenuCheckClick()

            fun onMenuBindClick()

            fun onMenuConvertClick()
        }

    }

}