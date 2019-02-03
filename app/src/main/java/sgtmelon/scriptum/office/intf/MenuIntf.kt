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

        interface NoteMenuClick {
            /**
             * @param editModeChange - Надо ли менять режим редактирования
             * @param showToast      - Показывать уведомление о том, что заметка пуста
             * @return - Сохранение успешно при возвращении true
             */
            fun onMenuSaveClick(editModeChange: Boolean, showToast: Boolean): Boolean

            // TODO: 10.12.2018 добавить длинное нажатие на кнопку undo/redo - для возвращение в один из концов

            fun onUndoClick()

            fun onRedoClick()

            fun onMenuRankClick()

            fun onMenuColorClick()

            /**
             * @param editMode - Установка режима редактирования
             */
            fun onMenuEditClick(editMode: Boolean)

            fun onMenuCheckClick()

            fun onMenuBindClick()

            fun onMenuConvertClick()
        }

    }

    /**
     * Интерфейс для меню при долгом нажатии на заметку
     */
    interface Dialog {

        interface DeleteMenuClick {
            fun onMenuRestoreClick(p: Int)

            fun onMenuCopyClick(p: Int)

            fun onMenuClearClick(p: Int)
        }

        interface NoteMenuClick {
            fun onMenuCheckClick(p: Int)

            fun onMenuBindClick(p: Int)

            fun onMenuConvertClick(p: Int)

            fun onMenuCopyClick(p: Int)

            fun onMenuDeleteClick(p: Int)
        }

    }

}