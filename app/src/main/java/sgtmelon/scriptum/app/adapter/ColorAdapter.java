package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Dimension;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.data.ColorData;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.intf.ItemListener;
import sgtmelon.scriptum.office.utils.AppUtils;
import sgtmelon.scriptum.office.utils.PrefUtils;

public final class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private final Context context;
    private final LayoutInflater inflater;

    private final int count;

    @IdRes private final int[] fillColor, strokeColor, checkColor;
    @Dimension private final int strokeDimen;

    private int check;
    private boolean[] visible;

    private ItemListener.ClickListener  clickListener;

    public ColorAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        count = ColorData.INSTANCE.getSize();

        switch (new PrefUtils(context).getTheme()) {
            case ThemeDef.light:
                fillColor = ColorData.INSTANCE.getLight();
                strokeColor = ColorData.INSTANCE.getDark();
                checkColor = ColorData.INSTANCE.getDark();
                break;
            default:
            case ThemeDef.dark:
                fillColor = ColorData.INSTANCE.getDark();
                strokeColor = ColorData.INSTANCE.getDark();
                checkColor = ColorData.INSTANCE.getLight();
                break;
        }

        strokeDimen = AppUtils.INSTANCE.getDimen(context, 1);
    }

    public void setCheck(int check) {
        this.check = check;

        visible = new boolean[getItemCount()];
        visible[check] = true;
    }

    public void setClickListener(ItemListener.ClickListener  clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_color, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        final int fillColor = ContextCompat.getColor(context, this.fillColor[position]);
        final int strokeColor = ContextCompat.getColor(context, this.strokeColor[position]);
        final int checkColor = ContextCompat.getColor(context, this.checkColor[position]);

        if (holder.clBackground.getBackground() instanceof GradientDrawable) {
            final GradientDrawable drawable = (GradientDrawable) holder.clBackground.getBackground();
            drawable.setColor(fillColor);
            drawable.setStroke(strokeDimen, strokeColor);
        }

        holder.clCheck.setColorFilter(checkColor);

        // TODO: 02.12.2018 без использования анимации xml, другой вид анимации
        if (visible[position]) {                            //Если отметка видна
            if (this.check == position) {                   //Если текущая позиция совпадает с выбранным цветом
                holder.clCheck.setVisibility(View.VISIBLE);
            } else {
                visible[position] = false;                  //Делаем отметку невидимой с анимацией
                holder.clCheck.startAnimation(holder.alphaOut);
            }
        } else {
            holder.clCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    final class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            Animation.AnimationListener {

        private final View clBackground;
        private final ImageView clCheck;
        private final View clClick;

        private final Animation alphaIn, alphaOut;

        ColorHolder(View view) {
            super(view);

            clBackground = itemView.findViewById(R.id.background_view);
            clCheck = itemView.findViewById(R.id.check_image);
            clClick = itemView.findViewById(R.id.click_view);

            clClick.setOnClickListener(this);

            alphaIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            alphaOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);

            alphaIn.setAnimationListener(this);
            alphaOut.setAnimationListener(this);
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
                clCheck.startAnimation(alphaIn);    //Показываем новую
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
            clClick.setEnabled(false);

            if (animation == alphaIn) {
                clCheck.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            clClick.setEnabled(true);

            if (animation == alphaOut) {
                clCheck.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

}