package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.GroupListModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class GroupListAdapter extends BaseExpandableListAdapter {

    private List<GroupListModel.DataBean> list;
    private Context context;

    public GroupListAdapter(List<GroupListModel.DataBean> beanList, Context context) {
        list = beanList;
        this.context = context;
    }

    public void refreshData(List<GroupListModel.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getUserList().size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getUserList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {

        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_group, viewGroup, false);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        GroupListModel.DataBean dataBean = list.get(i);
        String name = dataBean.getName();
        groupViewHolder.textView.setText(name);
        String mun = "(" + list.get(i).getUserList().size() + ")";
        groupViewHolder.tvNum.setText(mun);

        //如果是展开状态，
//        if (isExpanded){
//            groupViewHolder.parent_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.img_arrow_down));
//        }else{
//            groupViewHolder.parent_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_arrow_right));
//        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_address, viewGroup, false);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        List<GroupListModel.DataBean.UserListBean> userList = list.get(i).getUserList();
        GroupListModel.DataBean.UserListBean userListBean = userList.get(i1);
        if (userListBean != null) {
            String name = userListBean.getName();
            String avatar = userListBean.getAvatar();
            Glide.with(context).load(avatar).placeholder(context.getResources().getDrawable(R.mipmap.head2)).into(childViewHolder.ivHead);
            childViewHolder.textView.setText(name);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupViewHolder {
        TextView textView;
        TextView tvNum;
        RelativeLayout group;

        public GroupViewHolder(View view) {
            this.textView = view.findViewById(R.id.tv_group);
//            this.textView = view.findViewById(android.R.id.text1);
            this.tvNum = view.findViewById(R.id.tv_num);
            this.group = view.findViewById(R.id.group);
        }
    }

    private class ChildViewHolder {
        TextView textView;
        RoundedImageView ivHead;

        public ChildViewHolder(View view) {
            this.textView = view.findViewById(R.id.item_tv_name);
            this.ivHead = view.findViewById(R.id.img);
        }
    }
}
