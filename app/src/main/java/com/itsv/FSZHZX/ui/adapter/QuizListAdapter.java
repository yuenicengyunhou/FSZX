package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.listener.MyListener;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.util.List;


public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.Holder> {

    private List<QuizModel.DataBean> list;
    private final LayoutInflater inflater;
    private MyListener<QuizModel.DataBean> listener;


    public QuizListAdapter(Context context,List<QuizModel.DataBean> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void update(List<QuizModel.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(MyListener<QuizModel.DataBean> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_quiz, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        QuizModel.DataBean dataBean = list.get(position);
        holder.textView.setText(dataBean.getQuestionTitle());
        holder.parent.setOnClickListener(v -> {
            if (dataBean.isAnswered()) {
                ToastUtils.showSingleToast("此题已答过");
            }else {
                if (null != listener) {
                    listener.onItemClick(position, dataBean);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView parent;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_quiz);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
