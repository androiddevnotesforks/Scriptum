package sgtmelon.scriptum.app.selection;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.app.model.repo.RepoNote;

public class SelNoteLookup extends ItemDetailsLookup<RepoNote> {

    private final RecyclerView recyclerView;

    public SelNoteLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<RepoNote> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());

        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof ViewHolderDetails) {
                return ((ViewHolderDetails) viewHolder).getDetailsNote();
            }
        }

        return null;
    }
}
