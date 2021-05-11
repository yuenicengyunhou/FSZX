package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.databinding.ItemQuizBinding;
import com.itsv.FSZHZX.model.QuestionSort;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.util.ArrayList;

public class QuizSortAdapter extends RecyclerView.Adapter<QuizSortAdapter.Holder> {

    private ArrayList<QuestionSort> list;
    private LayoutInflater inflater;

    public QuizSortAdapter(ArrayList<QuestionSort> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizBinding binding = ItemQuizBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        QuestionSort questionSort = list.get(position);
        String typeName = questionSort.getTypeName();
        String typeTime = questionSort.getTypeTime();
        String id = questionSort.getId();
        holder.binding.tvTime.setText(typeTime);
        holder.binding.tvQuiz.setText(typeName);
        holder.binding.tvStart.setOnClickListener(v -> {
            ToastUtils.showSingleToast(id);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(ArrayList<QuestionSort> list) {
        if (null == list||list.isEmpty()) {
            return;
        }
        this.list = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{

        //        public Holder(@NonNull View itemView) {
//            super(itemView);
//        }
        ItemQuizBinding binding;


        public Holder(@NonNull View itemView,ItemQuizBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}
