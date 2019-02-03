package sgtmelon.safedialog.library.color;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
import sgtmelon.safedialog.R;
import sgtmelon.safedialog.office.intf.ColorIntf;

public final class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private final Context context;
    private final int count;
    @Dimension private final int strokeDimen;

    private final LayoutInflater inflater;

    @IdRes private int[] fillColor, strokeColor, checkColor;
    private int check;
    private boolean[] visible;

    private ColorIntf.ClickListener clickListener;

    public ColorAdapter(Context context, int count) {
        this.context = context;
        this.count = count;

        inflater = LayoutInflater.from(context);

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        strokeDimen = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
    }

    public void setFillColor(int[] fillColor) {
        this.fillColor = fillColor;
    }

    public void setStrokeColor(int[] strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setCheckColor(int[] checkColor) {
        this.checkColor = checkColor;
    }

    public void setCheck(int check) {
        this.check = check;

        visible = new boolean[getItemCount()];
        visible[check] = true;
    }

    public void setClickListener(ColorIntf.ClickListener clickListener) {
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

            clickListener.onColorClick(newCheck);

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