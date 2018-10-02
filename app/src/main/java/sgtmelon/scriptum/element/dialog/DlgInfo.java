package sgtmelon.scriptum.element.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.blank.BlankDlg;

public final class DlgInfo extends BlankDlg implements View.OnClickListener {

    private View.OnClickListener logoClick;

    private int click = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt(DefDlg.VALUE);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_about, null);

        ImageView logo = view.findViewById(R.id.logo_image);
        logo.setOnClickListener(this);

        return new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDlg.VALUE, click);
    }

    public void setLogoClick(View.OnClickListener logoClick) {
        this.logoClick = logoClick;
    }

    @Override
    public void onClick(View view) {
        if (++click == context.getResources().getInteger(R.integer.pref_logo_click_value)) {
            click = 0;
            logoClick.onClick(view);
            getDialog().cancel();
        }
    }

}
