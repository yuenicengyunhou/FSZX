package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.MyAttachModel;
import com.itsv.FSZHZX.model.NotiModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotiModel.DataBean> beans;
    private final Context context;
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;

    private NotiItemClickListener itemClickListener;

    private List<MyAttachModel> attachModelList;

    public void setItemClickListener(NotiItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public NotiAdapter(List<NotiModel.DataBean> list, Context context) {
        this.beans = list;
        this.context = context;
        attachModelList = new ArrayList<>();
    }

    public void refreshData(List<NotiModel.DataBean> list) {
        this.beans = list;
        notifyDataSetChanged();
    }

    public void refreshData(int position, String isCanhui) {
        this.beans.get(position).setIsCanhui(isCanhui);
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_footer, parent, false);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_noti, parent, false);
            return new NotiHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            initFooter(viewHolder);
        } else {
            NotiHolder holder = (NotiHolder) viewHolder;
            NotiModel.DataBean dataBean = beans.get(position);
            long id = dataBean.getId();
            NotiModel.DataBean.MeetingBaseResultBean bean = dataBean.getMeetingBaseResult();
            String meetingTitle = bean.getMeetingTitle();
            String meetingEndTime = bean.getMeetingEndTime();
            String meetingStartTime = bean.getMeetingStartTime();
            String meetingContent = bean.getMeetingContent();
            String meetingDuration = bean.getMeetingDuration();
            List<NotiModel.DataBean.MeetingBaseResultBean.MeetingUserListBean> meetingUserList = bean.getMeetingUserList();
            holder.itemNotiTitle.setText(meetingTitle);
            setBoldText(holder.itemNotiTitle);
            holder.itemNotiTopic.setText(MessageFormat.format("主题内容：{0}", meetingContent));
            holder.itemNotiTime.setText(MessageFormat.format("会议时间：{0} 至 {1}", meetingStartTime, meetingEndTime));
            holder.itemNotiEndTime.setText(MessageFormat.format("会议时长：{0}分钟", meetingDuration));

            //会议类型
            String meetingTypeName = dataBean.getMeetingTypeName();
            int color;
            if (meetingTypeName == null) {
                meetingTypeName = "未知类型";
            }
            if ("常委会".equals(meetingTypeName)) {
                color = holder.changweiMeeting;
            } else if ("日常会议".equals(meetingTypeName)) {
                color = holder.normalMeeting;
            } else if ("表彰会议".equals(meetingTypeName)) {
                color = holder.praiseMeeting;
            } else if ("专门会议".equals(meetingTypeName)) {
                color = holder.specialMeeting;
            } else if ("长效会议".equals(meetingTypeName)) {
                color = holder.longMeeting;
            } else if ("临时会议".equals(meetingTypeName)) {
                color = holder.tempMeeting;
            } else {
                color = holder.textColor;
            }
            holder.notiType.setText(meetingTypeName);
            holder.notiType.setTextColor(color);
            holder.itemNotiSign.setBackgroundColor(color);

            //是否参会
            String isCanhui = dataBean.getIsCanhui();
            String content = dataBean.getContent();
            String reason = content == null ? "" : content;
            if (null == isCanhui) {
                holder.itemNotiSure.setVisibility(View.VISIBLE);
                holder.itemNotiSure.setBackground(holder.attend);
                holder.itemNotiSure.setText("确定参会");
                holder.itemNotiNo.setText("不参会");
                holder.itemNotiNo.setTextColor(holder.textBlack);
                holder.itemNotiNo.setBackground(holder.noAttend);
            } else {
                if (isCanhui.equals("1")) {
                    holder.itemNotiSure.setVisibility(View.GONE);
                    holder.itemNotiNo.setText("已选参会");
                    holder.itemNotiNo.setBackground(holder.toAttend);
                    holder.itemNotiNo.setTextColor(holder.white);
                } else {
                    holder.itemNotiSure.setVisibility(View.GONE);
                    holder.itemNotiNo.setText("已选不参会");
                    holder.itemNotiNo.setBackground(holder.invalid);
                    holder.itemNotiNo.setTextColor(holder.textBlack);
                }
            }


            //参会人员
            if (null == meetingUserList || meetingUserList.isEmpty()) {
                holder.itemNotiCount.setText("参会人员：无");
            } else {
                holder.itemNotiCount.setText(MessageFormat.format("参会人员：{0}人", meetingUserList.size()));
                String members = "";
                for (int i = 0; i < meetingUserList.size(); i++) {
                    String name = meetingUserList.get(i).getName();
                    members = MessageFormat.format("{0}{1}", members, i == meetingUserList.size() - 1 ? name : name + "，");
                }
                holder.itemNotiPaticipant.setText(members);
            }
            //显示成员
            boolean showMember = bean.isShowMember();
            if (showMember && (null != meetingUserList)) {
                holder.itemNotiPaticipant.setVisibility(View.VISIBLE);
                holder.itemNotiSpread.setBackground(holder.up);
            } else {
                holder.itemNotiPaticipant.setVisibility(View.GONE);
                holder.itemNotiSpread.setBackground(holder.down);
            }
            holder.fakeView.setOnClickListener(view -> {
                if (!showMember) {
                    holder.itemNotiSpread.setBackground(holder.up);
                } else {
                    holder.itemNotiSpread.setBackground(holder.down);
                }
                bean.setShowMember(!showMember);
                notifyDataSetChanged();
            });

            //判断会议内容是否显示完全
//            isTextFullShowed(holder.itemNotiTopic, holder.notiEye, position);

            //附件
            List<NotiModel.DataBean.MeetingBaseResultBean.FileListBean> fileList = bean.getFileList();
            boolean noAttach = fileList == null || fileList.isEmpty();
            if (noAttach  ) {
                holder.itemNotiAttach.setVisibility(View.GONE);
                holder.itemNotiAtt.setVisibility(View.GONE);
            }else {
                NotiModel.DataBean.MeetingBaseResultBean.FileListBean fileListBean = fileList.get(0);
                holder.itemNotiAttach.setVisibility(View.VISIBLE);
                holder.itemNotiAtt.setVisibility(View.VISIBLE);
                String name = fileListBean == null ? "此附件参数错误" : fileListBean.getFileName();
                if (fileList.size() > 1) {
                    holder.itemNotiAttach.setText(MessageFormat.format("{0}等{1}个附件", name, fileList.size()));
                }else{
                    holder.itemNotiAttach.setText(name);
                }
            }

//按钮点击

            holder.itemNotiSure.setOnClickListener(view -> {
                if (null != itemClickListener)
                    itemClickListener.onAttend(id, position);
            });

            holder.itemNotiNo.setOnClickListener(view -> {
                if (!"1".equals(isCanhui)) {
                    if (null != itemClickListener)
                        itemClickListener.onRefuse(id, position, reason);
                }
            });
            holder.itemNotiAttach.setOnClickListener(v -> {
                if (null != itemClickListener && !noAttach) {
                    String[] strings = initAttachs(fileList);
                    itemClickListener.onViewPDF(attachModelList, strings);
                }
            });
            holder.itemNotiTopic.setOnClickListener(v -> {
                if (null != itemClickListener) {
                    itemClickListener.toDetails(position,meetingContent);
                }
            });
        }

    }

    private void initFooter(@NonNull RecyclerView.ViewHolder holder) {
        FootViewHolder footViewHolder = (FootViewHolder) holder;
        switch (loadState) {
            case LOADING: // 正在加载
                footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                footViewHolder.tv_end.setVisibility(View.GONE);
                break;
            case LOADING_COMPLETE: // 加载完成
                footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                footViewHolder.tv_end.setVisibility(View.GONE);
                break;

            case LOADING_END: // 加载到底
                footViewHolder.pbLoading.setVisibility(View.GONE);
                footViewHolder.tvLoading.setVisibility(View.GONE);
                footViewHolder.tv_end.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

//    private List<MyAttachModel> initMyAttachList(List<NotiModel.DataBean.MeetingBaseResultBean.FileListBean> fileList) {
//        attachModelList.clear();
//        for (int i = 0; i < fileList.size(); i++) {
//            NotiModel.DataBean.MeetingBaseResultBean.FileListBean fileListBean = fileList.get(i);
//            if (fileListBean == null) break;
//            String fileId = fileListBean.getFileId();
//            String fileName = fileListBean.getFileName();
//            MyAttachModel myAttachModel = new MyAttachModel();
//            myAttachModel.fileId = fileId;
//            myAttachModel.fileName = fileName;
//            attachModelList.add(myAttachModel);
//        }
//        return attachModelList;
//    }

    private String[] initAttachs(List<NotiModel.DataBean.MeetingBaseResultBean.FileListBean> list) {
        String[] attachNames = new String[list.size()];
        attachModelList.clear();
        for (int i = 0; i < list.size(); i++) {
            NotiModel.DataBean.MeetingBaseResultBean.FileListBean fileListBean = list.get(i);
            if (fileListBean != null) {
                MyAttachModel myAttachModel = new MyAttachModel();
                String fileName = fileListBean.getFileName();
                String fileId = fileListBean.getFileId();
                attachNames[i] = fileName;
                myAttachModel.fileId = fileId;
                myAttachModel.fileName = fileName;
                attachModelList.add(myAttachModel);
            } else {
                attachNames[i] = "此附件参数错误";
                attachModelList.add(null);
            }
        }
        return attachNames;
    }

    @Override
    public int getItemCount() {
        return beans.size() + 1;
    }

    public class NotiHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_noti_sign)
        TextView itemNotiSign;
        @BindView(R.id.item_noti_title)
        TextView itemNotiTitle;
        @BindView(R.id.item_noti_time)
        TextView itemNotiTime;
        @BindView(R.id.item_noti_endTime)
        TextView itemNotiEndTime;
        @BindView(R.id.item_noti_count)
        TextView itemNotiCount;
        @BindView(R.id.item_noti_spread)
        ImageView itemNotiSpread;
        @BindView(R.id.item_noti_paticipant)
        TextView itemNotiPaticipant;
        @BindView(R.id.item_noti_topic)
        TextView itemNotiTopic;
        @BindView(R.id.item_noti_att)
        TextView itemNotiAtt;
        @BindView(R.id.item_noti_attach)
        TextView itemNotiAttach;
        @BindView(R.id.item_noti_no)
        TextView itemNotiNo;
        @BindView(R.id.item_noti_sure)
        TextView itemNotiSure;
        @BindColor(R.color.colorTextB)
        int textBlack;
        @BindDrawable(R.drawable.button_invalid)
        Drawable invalid;
        @BindColor(R.color.white)
        int white;
        @BindDrawable(R.mipmap.attended)
        Drawable toAttend;
        @BindDrawable(R.mipmap.attend)
        Drawable attend;
        @BindDrawable(R.mipmap.no_attend)
        Drawable noAttend;
        @BindDrawable(R.mipmap.up)
        Drawable up;
        @BindDrawable(R.mipmap.down)
        Drawable down;
        //        @BindDrawable(R.mipmap.ic_eye)
//        Drawable eye;
//        @BindDrawable(R.mipmap.pdf)
//        Drawable pdf;
        @BindView(R.id.itemLayout)
        RelativeLayout itemLayout;
        @BindColor(R.color.colorBgFalse)
        int red;
        @BindView(R.id.fakeView)
        View fakeView;
        @BindColor(R.color.normalMeeting)
        int normalMeeting;
        @BindColor(R.color.chaweiMeeting)
        int changweiMeeting;
        @BindColor(R.color.specialMeeting)
        int specialMeeting;
        @BindColor(R.color.praiseMeeting)
        int praiseMeeting;
        @BindColor(R.color.longMeeting)
        int longMeeting;
        @BindColor(R.color.tempMeeting)
        int tempMeeting;
        @BindColor(R.color.colorTextB)
        int textColor;
        @BindView(R.id.item_noti_type)
        TextView notiType;
//        @BindView(R.id.noti_eye)
//        ImageView notiEye;


        public NotiHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        //        LinearLayout llEnd;
        TextView tv_end;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
            tvLoading = itemView.findViewById(R.id.tv_loading);
//            llEnd = itemView.findViewById(R.id.ll_end);
            tv_end = itemView.findViewById(R.id.tv_end);
        }
    }

    /**
     * 设置footer来显示上拉加载
     **/
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    private void setBoldText(TextView textView) {
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
    }

    public interface NotiItemClickListener {
        void onAttend(long id, int position);

        void onRefuse(long id, int position, String reason);

        void onViewPDF(List<MyAttachModel> list, String[] strings);

        void toDetails(int position, String content);
    }

    private void isTextFullShowed(TextView textView, ImageView imageView, int position) {
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            Layout l = textView.getLayout();
            if (l != null) {
                int lines = l.getLineCount();
                int ellipsisCount = l.getEllipsisCount(lines - 1);
                if (lines > 2) {
                    if (ellipsisCount > 0) {
                        Log.e("WQ", "ellipsisCount==" + ellipsisCount + "   position===" + position + "  id=" + textView.getId());
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
//                    Log.e("WQ", "Text is ellipsized" + position);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            } else {
                imageView.setVisibility(View.GONE);
            }
        });
    }

}
