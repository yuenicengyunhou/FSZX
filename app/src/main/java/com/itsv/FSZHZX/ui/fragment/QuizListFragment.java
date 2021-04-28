package com.itsv.FSZHZX.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.listener.MyListener;
import com.itsv.FSZHZX.listener.TabListener;
import com.itsv.FSZHZX.model.QuizModel;
import com.itsv.FSZHZX.ui.adapter.QuizListAdapter;
import com.itsv.FSZHZX.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class QuizListFragment extends Fragment {
    private String params = "{}";
    private List<QuizModel.DataBean> beanList;
    private QuizListAdapter adapter;
    private TabListener tabListener;

    public void setTabListener(TabListener tabListener) {
        this.tabListener = tabListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_list, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            params = arguments.getString("quiz");
        }
        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        beanList = new ArrayList<>();
        adapter = new QuizListAdapter(getContext(), beanList);
        recycler.setAdapter(adapter);
        adapter.setListener((position, bean) -> {
            if (null != tabListener) {
                tabListener.onChangeFragment(position, 1);
            }
        });
    }

    public void setListData(List<QuizModel.DataBean> list) {
        adapter.update(list);
    }
}
