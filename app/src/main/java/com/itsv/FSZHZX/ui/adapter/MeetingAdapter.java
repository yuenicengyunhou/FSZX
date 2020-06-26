package com.itsv.FSZHZX.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.model.MyAttachModel;
import com.itsv.FSZHZX.model.MyMeetingModel;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyMeetingModel.DataBean> beans;
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

    private ItemClickListener itemClickListener;
    private List<MyAttachModel> attachModelList;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MeetingAdapter(List<MyMeetingModel.DataBean> list, Context context) {
        this.beans = list;
        this.context = context;
        attachModelList = new ArrayList<>();
    }

    public void refreshData(List<MyMeetingModel.DataBean> list) {
        this.beans = list;
        notifyDataSetChanged();
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
            return new NotifiHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            initFooter(viewHolder);
        } else {
            NotifiHolder holder = (NotifiHolder) viewHolder;
            MyMeetingModel.DataBean dataBean = beans.get(position);
            long mID = dataBean.getId();
            long meetingOwnerUserId = dataBean.getMeetingOwnerUserId();
            String meetingOwnerPassword = dataBean.getMeetingOwnerPassword();
            String meetingTitle = dataBean.getMeetingTitle();
            String meetingStartTime = dataBean.getMeetingStartTime();
            String meetingContent = dataBean.getMeetingContent();
            String meetingDuration = dataBean.getMeetingDuration();
            String sdkCid = dataBean.getSdkCid();
//            String sdkConferenceUrl = dataBean.getSdkConferenceUrl();
            String sdkRoomNum = dataBean.getSdkRoomNum();
//            long id = dataBean.getId();
            boolean showMember = dataBean.isShowMember();
            holder.itemNotiTitle.setText(meetingTitle);
            setBoldText(holder.itemNotiTitle);
            holder.itemNotiTopic.setText(MessageFormat.format("主题内容：{0}", meetingContent));

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

            //这里接口时间提前8个小时，暂时先手动推后8个小时
            String meetingTime = dataBean.getMeetingTime();
            holder.itemNotiTime.setText(MessageFormat.format("会议时间：{0}", meetingTime));
            holder.itemNotiEndTime.setText(MessageFormat.format("会议时长：{0}分钟", meetingDuration));
            long startDate = getStringToDate(meetingStartTime, "yyyy-MM-dd HH:mm:ss", false);
            String status = dataBean.getStatus();
            holder.itemNotiNo.setText("进入会议");
            if (status.equals("0")) {
                holder.itemNotiNo.setBackground(holder.buttonUndo);
            } else if (status.equals("1")) {
                holder.itemNotiNo.setBackground(holder.toAttend);
            } else {
                holder.itemNotiNo.setBackground(holder.buttonDone);
                holder.itemNotiNo.setText("已结束");
            }
            //参会人员
            List<MyMeetingModel.DataBean.MeetingUserListBean> meetingUserList = dataBean.getMeetingUserList();
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
                dataBean.setShowMember(!showMember);
                notifyDataSetChanged();
            });


            //附件
            List<MyMeetingModel.DataBean.FileListBean> fileList = dataBean.getFileList();
            boolean noAttach = fileList == null || fileList.isEmpty();
            if (noAttach) {
                holder.itemNotiAttach.setVisibility(View.GONE);
                holder.itemNotiAtt.setVisibility(View.GONE);
            } else {
                MyMeetingModel.DataBean.FileListBean fileListBean = fileList.get(0);
                holder.itemNotiAttach.setVisibility(View.VISIBLE);
                holder.itemNotiAtt.setVisibility(View.VISIBLE);
                String name = fileListBean == null ? "此附件参数错误" : fileListBean.getFileName();
                if (fileList.size() > 1) {
                    holder.itemNotiAttach.setText(MessageFormat.format("{0}等{1}个附件", name, fileList.size()));
                } else {
                    holder.itemNotiAttach.setText(name);
                }
            }

            //按钮点击
            holder.itemNotiNo.setOnClickListener(view -> {
                if (null != itemClickListener) {
//                    if (status.equals("2")) {
//                        Toast.makeText(context, "此会议已结束", Toast.LENGTH_SHORT).show();
                        if (isTimeValid(startDate)) {
                            itemClickListener.onItemClick(position, sdkCid, "https://bj189.scmeeting.com/", sdkRoomNum,mID,meetingOwnerUserId,meetingOwnerPassword);
                        } else {
                            Toast.makeText(context, "请在会议前15分钟内点击进入", Toast.LENGTH_SHORT).show();
                        }
                    }
//                    else {
//                        if (isTimeValid(startDate)) {
//                            itemClickListener.onItemClick(position, sdkCid, "https://bj189.scmeeting.com/", sdkRoomNum);
//                        } else {
//                            Toast.makeText(context, "请在会议前15分钟内点击进入", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
            });

            holder.itemNotiAttach.setOnClickListener(v -> {
                if (null != itemClickListener && !noAttach) {
                    String[] strings = initAttachs(fileList);
                    itemClickListener.onViewPDF(attachModelList, strings);
                }
            });
            holder.itemNotiTopic.setOnClickListener(v -> {
                if (null != itemClickListener) {
                    itemClickListener.toDetails(position, meetingContent);
                }
            });
        }
    }

    private String[] initAttachs(List<MyMeetingModel.DataBean.FileListBean> list) {
        String[] attachNames = new String[list.size()];
        attachModelList.clear();
        for (int i = 0; i < list.size(); i++) {
            MyMeetingModel.DataBean.FileListBean fileListBean = list.get(i);
            if (fileListBean != null) {
                String fileName = fileListBean.getFileName();
                MyAttachModel myAttachModel = new MyAttachModel();
                String fileId = fileListBean.getFileId();
                attachNames[i] = fileName;
                myAttachModel.fileId = fileId;
                myAttachModel.fileName = fileName;
                attachModelList.add(myAttachModel);
            } else {
                attachModelList.add(null);
                attachNames[i] = "此附件参数错误";
            }
        }
        return attachNames;
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

    @Override
    public int getItemCount() {
        return beans.size() + 1;
    }

    public class NotifiHolder extends RecyclerView.ViewHolder {
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
        @BindDrawable(R.mipmap.attended)
        Drawable toAttend;
        @BindDrawable(R.drawable.button_invalid_dark)
        Drawable noAttend;
        @BindDrawable(R.mipmap.up)
        Drawable up;
        @BindDrawable(R.mipmap.down)
        Drawable down;

        @BindView(R.id.fakeView)
        View fakeView;
        //        @BindDrawable(R.mipmap.ic_eye)
//        Drawable eye;
//
//        @BindDrawable(R.mipmap.pdf)
//        Drawable pdf;
        @BindView(R.id.itemLayout)
        RelativeLayout itemLayout;
        @BindColor(R.color.normalMeeting)
        int normalMeeting;
        @BindColor(R.color.chaweiMeeting)
        int changweiMeeting;
        @BindColor(R.color.specialMeeting)
        int specialMeeting;
        @BindColor(R.color.praiseMeeting)
        int praiseMeeting;
        @BindColor(R.color.colorTextB)
        int textColor;
        @BindColor(R.color.longMeeting)
        int longMeeting;
        @BindColor(R.color.tempMeeting)
        int tempMeeting;
        @BindView(R.id.item_noti_type)
        TextView notiType;

        @BindDrawable(R.drawable.button_done)
        Drawable buttonDone;
        @BindDrawable(R.drawable.button_undo)
        Drawable buttonUndo;


        public NotifiHolder(@NonNull View itemView) {
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

    public interface ItemClickListener {
        void onItemClick(int position, String sdkCid, String sdkConferenceUrl, String sdkRoomNum,long id,long moderatorId,String moderatorPsw);

        void onViewPDF(List<MyAttachModel> fileListBeans, String[] strings);

        void toDetails(int position, String content);
    }

    private long getStringToDate(String dateString, String pattern, boolean delay) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (delay) {
            return Objects.requireNonNull(date).getTime() + 1000 * 60 * 60 * 8;
        } else {
            return Objects.requireNonNull(date).getTime();
        }
    }

    private String longToFomatt(long time) {
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private boolean isTimeValid(long time) {
        if (time == 0) return false;
        long timeStamp = System.currentTimeMillis();
        long temp = time - timeStamp;
        if (temp < 0) {
            return true;
        } else {
            return (temp / (1000 * 60)) <= 15;
        }
    }

}
