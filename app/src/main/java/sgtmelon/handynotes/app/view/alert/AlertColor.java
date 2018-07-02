package sgtmelon.handynotes.app.view.alert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterColor;

public class AlertColor extends AlertDialog.Builder {

    private final Context context;
    private AdapterColor adapterColor;

    @SuppressWarnings("unused")
    public AlertColor(@NonNull Context context, int check) {
        super(context);

        this.context = context;

        setupRecycler(check);
    }

    public AlertColor(@NonNull Context context, int check, @StyleRes int themeResId) {
        super(context, themeResId);

        this.context = context;

        setupRecycler(check);
    }

    private void setupRecycler(int check) {
        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.alert_recycler_view_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        GridLayoutManager layoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.alert_color_column_count));
        recyclerView.setLayoutManager(layoutManager);

        adapterColor = new AdapterColor(context, check);
        recyclerView.setAdapter(adapterColor);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        setView(recyclerView);
    }

    public int getCheck() {
        return adapterColor.getCheck();
    }
}
