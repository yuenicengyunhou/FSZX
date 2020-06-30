package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private String[] titles;
    private List<Drawable> icons;
    private LayoutInflater inflater;

    private OnFunctionClickListener onFunctionClickListener;

    public HomeAdapter(Context context, String[] titles, List<Drawable> icons) {
        this.titles = titles;
        this.icons = icons;
        inflater = LayoutInflater.from(context);
    }

    public void setOnFunctionClickListener(OnFunctionClickListener onFunctionClickListener) {
        this.onFunctionClickListener = onFunctionClickListener;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        String s = titles[position];
        holder.homeText.setText(s);
        Drawable drawable = icons.get(position);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        holder.homeText.setCompoundDrawables(null,drawable,null,null);
        holder.homeText.setOnClickListener(v -> {
            if (null != onFunctionClickListener) {
                onFunctionClickListener.onClick(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class HomeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.home_text)
        TextView homeText;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnFunctionClickListener{
        void onClick(String title);
    }
}
