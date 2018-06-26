package sgtmelon.handynotes.office.intf.menu;

public interface MenuNoteClick {

    interface NoteClick {
        //Возвращает true - если сохранение прошло удачно, иначе - false
        boolean onMenuSaveClick(boolean changeEditMode);

        void onMenuRankClick();

        void onMenuColorClick();

        void onMenuEditClick(boolean editMode);

        void onMenuBindClick();

        void onMenuConvertClick();
    }

    interface RollClick {
        void onMenuCheckClick();
    }

    interface DeleteClick {
        void onMenuRestoreClick();

        void onMenuDeleteForeverClick();

        void onMenuDeleteClick();
    }

}
