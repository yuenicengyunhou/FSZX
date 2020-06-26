package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {

    private List<Profile> contents;
    private final LayoutInflater inflater;
    private int TYPE_HEAD = 0;
    private OnMyItemClickListener onMyItemClickListener;


    public ProfileAdapter(List<Profile> contents, Context context) {
        this.contents = contents;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return 1;
        }

    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        this.onMyItemClickListener = onMyItemClickListener;
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
//        if (viewType == TYPE_HEAD) {
//            view = inflater.inflate(R.layout.profile_head, parent, false);
//        } else {
            view = inflater.inflate(R.layout.profile_item, parent, false);
//        }
        return new ProfileHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
//        if (getItemViewType(position) == TYPE_HEAD) {
//            holder.tvHead.setText(contents.get(1).getContent());
//            holder.imageView.setVisibility(View.INVISIBLE);
//        } else {
            Profile profile = contents.get(position);
            String word = profile.getWord();
            String content = profile.getContent();
            holder.textView.setText(word);
            holder.tvHead.setText(content);
            if (word.equals("姓名") || word.equals("生日") || word.equals("性别")) {
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.INVISIBLE);
            }

//        }
        holder.relativeLayout.setOnClickListener(view -> {
            if (null != onMyItemClickListener) {
                onMyItemClickListener.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ProfileHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView tvHead;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ProfileHolder(@NonNull View itemView, int viewType) {
            super(itemView);
//            if (viewType == TYPE_HEAD) {
//                textView = itemView.findViewById(R.id.tv_touxiang);
//                tvHead = itemView.findViewById(R.id.tv_head);
//                imageView = itemView.findViewById(R.id.iv_arrow);
//                relativeLayout = itemView.findViewById(R.id.relative_head);
//            } else {
                textView = itemView.findViewById(R.id.tv_xingming);
                tvHead = itemView.findViewById(R.id.tv_name2);
                imageView = itemView.findViewById(R.id.iv_arrow2);
                relativeLayout = itemView.findViewById(R.id.relative_item);
//            }
        }
    }

    public interface OnMyItemClickListener {
        void itemClick(int position);
    }

}
