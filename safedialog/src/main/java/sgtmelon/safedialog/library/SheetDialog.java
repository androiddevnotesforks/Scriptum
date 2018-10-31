package sgtmelon.safedialog.library;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import sgtmelon.safedialog.office.annot.DialogAnn;


public final class SheetDialog extends BottomSheetDialogFragment implements
        NavigationView.OnNavigationItemSelectedListener {

    @LayoutRes
    private int layout;
    @IdRes
    private int navigation;

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;
    private DialogInterface.OnDismissListener dismissListener;

    public void setArguments(@LayoutRes int layout, @IdRes int navigation) {
        Bundle arg = new Bundle();

        arg.putInt(DialogAnn.INIT, layout);
        arg.putInt(DialogAnn.VALUE, navigation);

        setArguments(arg);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            layout = savedInstanceState.getInt(DialogAnn.INIT);
            navigation = savedInstanceState.getInt(DialogAnn.VALUE);
        } else if (arg != null) {
            layout = arg.getInt(DialogAnn.INIT);
            navigation = arg.getInt(DialogAnn.VALUE);
        }

        View view = inflater.inflate(layout, container, false);
        if (view == null) {
            throw new NullPointerException("Id error, layout view is null");
        }

        NavigationView navigationView = view.findViewById(navigation);
        if (navigationView != null) navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DialogAnn.INIT, layout);
        outState.putInt(DialogAnn.VALUE, navigation);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        dismissListener.onDismiss(dialog);
    }

    public void setNavigationItemSelectedListener(
            NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener) {
        this.navigationItemSelectedListener = navigationItemSelectedListener;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationItemSelectedListener.onNavigationItemSelected(menuItem);
        return false;
    }

}
