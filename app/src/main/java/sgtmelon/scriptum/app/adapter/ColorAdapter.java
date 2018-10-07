package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Arrays;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.intf.ItemIntf;

public final class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private final Context context;

    private final boolean[] visible;

    @IdRes
    private final int[] icons, colors;

    private int check;
    private ItemIntf.Click click;

    public ColorAdapter(Context context, int check, int[] icons, int[] colors) {
        this.context = context;
        this.check = check;

        this.icons = icons;
        this.colors = colors;

        visible = new boolean[getItemCount()];
        Arrays.fill(visible, false);
        visible[check] = true;
    }

    public void setClick(ItemIntf.Click click) {
        this.click = click;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_color, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        Drawable bg = ContextCompat.getDrawable(context, icons[position]);
        int cl = ContextCompat.getColor(context, colors[position]);

        holder.clBackground.setBackground(bg);
        holder.clCheck.setColorFilter(cl);

        if (visible[position]) {                            //Если отметка видна
            if (this.check == position) {                   //Если текущая позиция совпадает с выбранным цветом
                holder.clCheck.setVisibility(View.VISIBLE);
            } else {
                visible[position] = false;                  //Делаем отметку невидимой с анимацией
                holder.clCheck.startAnimation(holder.alphaOut);
            }
        } else holder.clCheck.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return icons.length;
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

            alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
            alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);

            alphaIn.setAnimationListener(this);
            alphaOut.setAnimationListener(this);
        }

        @Override
        public void onClick(View view) {
            int oldCheck = check;                   //Сохраняем старую позицию
            int newCheck = getAdapterPosition();    //Получаем новую

            click.onItemClick(view, newCheck);

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
