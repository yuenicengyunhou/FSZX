package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.NameModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<NameModel.DataBean> beans;
    private LayoutInflater inflater;
    private OnMyItemClickListener onItemClickListener;
    private Context context;

    public ContactAdapter(Context context, List<NameModel.DataBean> list) {
        this.beans = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void refreshData(List<NameModel.DataBean> list) {
        this.beans = list;
        notifyDataSetChanged();
    }
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = beans.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override

    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View  view = inflater.inflate(R.layout.item_contacts, viewGroup, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int i) {
        NameModel.DataBean dataBean = beans.get(i);
        String name = dataBean.getName();
        String avatar = dataBean.getAvatar();
        String letters = dataBean.getSortLetters();
        if (i <= 0) {
            holder.tv_letter.setVisibility(View.VISIBLE);
            holder.tv_letter.setText(letters);
        } else {
            if (dataBean.getSortLetters().equals(beans.get(i - 1).getSortLetters())) {
                holder.tv_letter.setVisibility(View.GONE);
            } else {
                holder.tv_letter.setVisibility(View.VISIBLE);
                holder.tv_letter.setText(letters);
            }
        }
        holder.tv_name.setText(name);
        Glide.with(context).load(avatar).placeholder(context.getResources().getDrawable(R.mipmap.head2)).into(holder.ivHead);
        holder.relativeLayout.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.itemClick(holder.relativeLayout, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView tv_letter;
        TextView tv_name;
        RelativeLayout relativeLayout;
        RoundedImageView ivHead;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            tv_letter = itemView.findViewById(R.id.tv_letter);
            tv_name = itemView.findViewById(R.id.item_tv_name);
            relativeLayout = itemView.findViewById(R.id.relative);
            ivHead = itemView.findViewById(R.id.img);
        }
    }


    public interface OnMyItemClickListener {
        void itemClick(View view, int position);
    }


}
