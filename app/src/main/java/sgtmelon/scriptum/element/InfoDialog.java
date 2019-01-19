package sgtmelon.scriptum.element;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.safedialog.office.annot.DialogAnn;
import sgtmelon.safedialog.office.blank.DialogBlank;
import sgtmelon.scriptum.R;

public final class InfoDialog extends DialogBlank implements View.OnClickListener {

    private View.OnClickListener logoClick;

    private int click = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt(DialogAnn.VALUE);
        }

        final View view = LayoutInflater.from(context).inflate(R.layout.view_about, null);

        final ImageView logo = view.findViewById(R.id.logo_image);
        logo.setOnClickListener(this);

        return new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DialogAnn.VALUE, click);
    }

    public void setLogoClick(View.OnClickListener logoClick) {
        this.logoClick = logoClick;
    }

    @Override
    public void onClick(View v) {
        if (++click == context.getResources().getInteger(R.integer.pref_logo_click_value)) {
            click = 0;
            logoClick.onClick(v);
            getDialog().cancel();
        }
    }

}
