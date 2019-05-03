package sgtmelon.scriptum.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.model.data.ColorData;
import sgtmelon.scriptum.model.item.ColorItem;
import sgtmelon.scriptum.office.intf.ItemListener;
import sgtmelon.scriptum.office.utils.AppUtils;
import sgtmelon.scriptum.office.utils.Preference;

/**
 * Адаптер списка цветов приложения для [ColorDialog]
 */
public final class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private final Context context;
    private final LayoutInflater inflater;

    private final List<ColorItem> colorList;

    @Dimension private final int strokeDimen;
    private final ItemListener.ClickListener clickListener;
    private int check;
    private boolean[] visible;

    public ColorAdapter(Context context, ItemListener.ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;

        inflater = LayoutInflater.from(context);

        colorList = ColorData.INSTANCE.getColorList(new Preference(context).getTheme());
        strokeDimen = AppUtils.INSTANCE.getDimen(context, 1);
    }

    public void setCheck(int check) {
        this.check = check;

        visible = new boolean[getItemCount()];
        visible[check] = true;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_color, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        final ColorItem colorItem = colorList.get(position);
        final int fillColor = ContextCompat.getColor(context, colorItem.getFill());
        final int strokeColor = ContextCompat.getColor(context, colorItem.getStroke());
        final int checkColor = ContextCompat.getColor(context, colorItem.getCheck());

        if (holder.backgroundView.getBackground() instanceof GradientDrawable) {
            final GradientDrawable drawable = (GradientDrawable) holder.backgroundView.getBackground();
            drawable.setColor(fillColor);
            drawable.setStroke(strokeDimen, strokeColor);
        }

        holder.checkImage.setColorFilter(checkColor);

        if (visible[position]) {                            //Если отметка видна
            if (this.check == position) {                   //Если текущая позиция совпадает с выбранным цветом
                holder.checkImage.setVisibility(View.VISIBLE);
            } else {
                visible[position] = false;                  //Делаем отметку невидимой с анимацией
                holder.hideCheck();
            }
        } else {
            holder.checkImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    // TODO !! вынести в отдельный класс

    final class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ViewGroup parentContainer;

        private final View backgroundView;
        private final ImageView checkImage;
        private final View clickView;

        ColorHolder(View view) {
            super(view);

            parentContainer = itemView.findViewById(R.id.color_parent_container);

            backgroundView = itemView.findViewById(R.id.color_background_view);
            checkImage = itemView.findViewById(R.id.color_check_image);
            clickView = itemView.findViewById(R.id.color_click_view);

            clickView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int oldCheck = check;                   //Сохраняем старую позицию
            final int newCheck = getAdapterPosition();    //Получаем новую

            clickListener.onItemClick(v, newCheck);

            if (oldCheck != newCheck) {             //Если выбранный цвет не совпадает с тем, на который нажали
                check = newCheck;                   //Присваиваем новую позицию
                visible[check] = true;

                notifyItemChanged(oldCheck);        //Скрываем старую отметку
                showCheck();
            }
        }

        void showCheck() {
            final Fade fade = new Fade();
            fade.setDuration(200);
            fade.addTarget(checkImage);

            TransitionManager.beginDelayedTransition(parentContainer, fade);
            checkImage.setVisibility(View.VISIBLE);
        }

        void hideCheck() {
            final Fade fade = new Fade();
            fade.setDuration(200);
            fade.addTarget(checkImage);

            TransitionManager.beginDelayedTransition(parentContainer, fade);
            checkImage.setVisibility(View.GONE);
        }

    }

}