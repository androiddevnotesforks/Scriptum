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
        final Bundle bundle = new Bundle();

        bundle.putInt(DialogAnn.INIT, layout);
        bundle.putInt(DialogAnn.VALUE, navigation);

        setArguments(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        if (savedInstanceState != null) {
            layout = savedInstanceState.getInt(DialogAnn.INIT);
            navigation = savedInstanceState.getInt(DialogAnn.VALUE);
        } else if (bundle != null) {
            layout = bundle.getInt(DialogAnn.INIT);
            navigation = bundle.getInt(DialogAnn.VALUE);
        }

        final View view = inflater.inflate(layout, container, false);
        if (view == null) {
            throw new NullPointerException("Id error, layout view is null");
        }

        final NavigationView navigationView = view.findViewById(navigation);
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
