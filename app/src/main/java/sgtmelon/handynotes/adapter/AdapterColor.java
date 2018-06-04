package sgtmelon.handynotes.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Arrays;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.service.Help;

public class AdapterColor extends RecyclerView.Adapter<AdapterColor.ColorHolder> {

    //region Variables
    private final Context context;
    private final LayoutInflater inflater;

    private final Drawable[] iconDrawable;
    private final Drawable[] checkDrawable;

    private int checkPosition;
    private final boolean[] checkVisible;
    //endregion

    public AdapterColor(Context context, int checkPosition) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        iconDrawable = Help.Icon.getColorIconDrawable(context);
        checkDrawable = Help.Icon.getColorCheckDrawable(context);

        this.checkPosition = checkPosition;

        checkVisible = new boolean[getItemCount()];
        Arrays.fill(checkVisible, false);
        checkVisible[checkPosition] = true;
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_color, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        if (checkVisible[position]) {                                   //Если отметка видна
            if (checkPosition == position) {                            //Если текущая позиция совпадает с выбранным цветом
                holder.clCheck.setVisibility(View.VISIBLE);
            } else {
                checkVisible[position] = false;                         //Делаем отметку невидимой с анимацией
                holder.clCheck.startAnimation(holder.alphaOut);
            }
        } else holder.clCheck.setVisibility(View.INVISIBLE);

        holder.clBackground.setBackground(iconDrawable[position]);
        holder.clCheck.setImageDrawable(checkDrawable[position]);
    }

    @Override
    public int getItemCount() {
        return Help.Icon.colors.length;
    }

    class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Animation.AnimationListener {

        private final ImageView clClick, clCheck, clBackground;
        private final Animation alphaIn, alphaOut;

        ColorHolder(View itemView) {
            super(itemView);

            clClick = itemView.findViewById(R.id.iView_itemColor_click);
            clCheck = itemView.findViewById(R.id.iView_itemColor_check);
            clBackground = itemView.findViewById(R.id.iView_itemColor_background);

            clClick.setOnClickListener(this);

            alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
            alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);

            alphaIn.setAnimationListener(this);
            alphaOut.setAnimationListener(this);
        }

        @Override
        public void onClick(View view) {
            int oldCheckPosition = checkPosition;           //Сохраняем старую позицию
            int newCheckPosition = getAdapterPosition();    //Получаем новую

            if (oldCheckPosition != newCheckPosition) {     //Если выбранный цвет не совпадает с тем, на который нажали
                checkPosition = newCheckPosition;           //Присваиваем новую позицию
                checkVisible[checkPosition] = true;

                notifyItemChanged(oldCheckPosition);        //Скрываем старую отметку
                clCheck.startAnimation(alphaIn);         //Показываем новую
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
