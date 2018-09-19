package sgtmelon.scriptum.app.selection;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails;
import sgtmelon.scriptum.app.model.repo.RepoNote;

public class SelNoteDetails extends ItemDetails<RepoNote> {

    private final int adapterPosition;
    private final RepoNote repoNote;

    public SelNoteDetails(int adapterPosition, RepoNote repoNote) {
        this.adapterPosition = adapterPosition;
        this.repoNote = repoNote;
    }

    @Override
    public int getPosition() {
        return adapterPosition;
    }

    @Nullable
    @Override
    public RepoNote getSelectionKey() {
        return repoNote;
    }

}
