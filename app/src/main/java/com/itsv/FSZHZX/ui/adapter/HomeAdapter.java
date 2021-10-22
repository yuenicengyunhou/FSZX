package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.HomeFunction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    //    private final String[] titles;
//    private final Drawable[] homeIcons;
   private List<HomeFunction> list;
    private final LayoutInflater inflater;

    private OnFunctionClickListener onFunctionClickListener;

    public HomeAdapter(Context context, List<HomeFunction> data) {
        list = data;
        inflater = LayoutInflater.from(context);
    }

    public void updateList(List<HomeFunction> list,int position) {
        this.list = list;
        notifyItemChanged(position);
    }

    public void setOnFunctionClickListener(OnFunctionClickListener onFunctionClickListener) {
        this.onFunctionClickListener = onFunctionClickListener;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_2, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        HomeFunction homeFunction = list.get(position);
        String functionName = homeFunction.getFunctionName();
        int count = homeFunction.getCount();
        if (count > 0) {
            holder.homeSpot.setVisibility(View.VISIBLE);
            holder.homeSpot.setText(String.valueOf(count));
        } else {
            holder.homeSpot.setVisibility(View.GONE);
        }
        holder.homeText.setText(functionName);
        Drawable drawable = homeFunction.getIcon();
        if (null != drawable) {
            holder.imageView.setBackground(drawable);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.homeText.setCompoundDrawables(null, drawable, null, null);
            holder.imageView.setOnClickListener(v -> {
                if (null != onFunctionClickListener) {
                    onFunctionClickListener.onClick(functionName);
                }
            });
            holder.homeText.setOnClickListener(v -> {
                if (null != onFunctionClickListener) {
                    onFunctionClickListener.onClick(functionName);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.home_text)
        TextView homeText;
        TextView homeSpot;
        ImageView imageView;
//        ImageView imageView;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            homeText=  itemView.findViewById(R.id.tv_home_text);
            homeSpot=  itemView.findViewById(R.id.tv_home_spot);
            imageView = itemView.findViewById(R.id.iv_home_icon);
//            imageView = itemView.findViewById(R.id.iv_home_icon);
        }
    }

    public interface OnFunctionClickListener{
        void onClick(String title);
    }
}
