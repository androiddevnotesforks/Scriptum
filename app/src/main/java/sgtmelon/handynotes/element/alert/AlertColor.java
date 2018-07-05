package sgtmelon.handynotes.element.alert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpColor;

public class AlertColor extends AlertDialog.Builder {

    private final Context context;
    private AdpColor adapter;

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

        adapter = new AdpColor(context, check);
        recyclerView.setAdapter(adapter);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        setView(recyclerView);
    }

    public int getCheck() {
        return adapter.getCheck();
    }
}
