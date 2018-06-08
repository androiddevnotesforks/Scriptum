package sgtmelon.handynotes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import sgtmelon.handynotes.database.DataBaseDescription;
import sgtmelon.handynotes.database.converter.ConverterBool;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.service.Help;

@Dao
@TypeConverters({ConverterBool.class})
public abstract class DaoNote extends DataBaseDescription {

    @Insert
    public abstract long insertNote(ItemNote itemNote);

    @Query("SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    public abstract ItemNote getNote(int id);

    @Query("SELECT * FROM NOTE_TABLE WHERE NT_BIN = :ntBin ORDER BY :sortKeys")
    public abstract List<ItemNote> getNote(int ntBin, String sortKeys);

    @Update
    public abstract void updateNote(ItemNote itemNote);

    @Query("UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :ntChange, NT_BIN = :ntBin " +
            "WHERE NT_ID = :ntId")
    public abstract void updateNote(int ntId, String ntChange, boolean ntBin);

    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :ntStatus " +
            "WHERE NT_ID = :ntId")
    public abstract void updateNote(int ntId, boolean ntStatus);

}
