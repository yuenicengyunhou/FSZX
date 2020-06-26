package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.Chat;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Chat> chats;
    private LayoutInflater inflater;

    public ChatAdapter(Context context,List<Chat>  list) {
        this.inflater = LayoutInflater.from(context);
        this.chats = list;
    }

    public void refreshData(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.item_chat, parent, false);
            return    new MemberHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_chat_mine, parent, false);
            return new ChatHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).isMe;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder  holder, int position) {
        Chat chat = chats.get(position);
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            MemberHolder memberHolder = (MemberHolder) holder;
            memberHolder.tvMsg.setHtml(chat.message);
            memberHolder.tvName.setText(chat.name);
        } else {
            ChatHolder chatHolder = (ChatHolder) holder;
            chatHolder.mineMsg.setText(chat.message);
            chatHolder.mineName.setText(chat.name);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MemberHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        HtmlTextView tvMsg;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.chat_name);
            tvMsg = itemView.findViewById(R.id.chat_msg);
        }
    }
    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView mineName;
        TextView mineMsg;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            mineName = itemView.findViewById(R.id.mine_name);
            mineMsg = itemView.findViewById(R.id.mine_msg);
        }
    }
}
