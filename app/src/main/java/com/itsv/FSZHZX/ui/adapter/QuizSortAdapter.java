package com.itsv.FSZHZX.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.databinding.ItemQuizBinding;
import com.itsv.FSZHZX.model.QuestionSort;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizSortAdapter extends RecyclerView.Adapter<QuizSortAdapter.Holder> {

    private ArrayList<QuestionSort> list;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

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
        holder.binding.tvTime.setText(formattTime(typeTime));
        holder.binding.tvQuiz.setText(typeName);
        holder.binding.tvStart.setOnClickListener(v -> {
            if (null != onItemClickListener) {
                onItemClickListener.onClick(id);
            }
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

    @SuppressLint("SimpleDateFormat")
    private String formattTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(time);
            if (null != date) {
                return  format.format(date);
            }
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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

    public interface OnItemClickListener{
        void onClick(String id);
    }
}
