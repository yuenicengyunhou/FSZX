package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.manis.core.entity.Participant;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PaticipantAdapter extends RecyclerView.Adapter<PaticipantAdapter.PaticipantHolder> {
    private List<Participant> participantList;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private boolean isModerator;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PaticipantAdapter(Context context, List<Participant> participantList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.participantList = participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
        notifyDataSetChanged();
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }

    @NonNull
    @Override
    public PaticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_member, parent, false);
        return new PaticipantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaticipantHolder holder, int position) {
        Participant participant = participantList.get(position);
        boolean getVideoStatus = participant.isGetVideoStatus();
        boolean getMuteMic = participant.isGetMuteMic();
        boolean host = participant.isHost();
        boolean stage = participant.isStage();
        String jid = participant.getJid();
        String nickname = participant.getNickname();
        holder.tvName.setText(nickname);

        if (getMuteMic) {
            holder.mic.setBackground(holder.micOff);
        } else {
            holder.mic.setBackground(holder.micOn);
        }

        if (getVideoStatus) {
            holder.cam.setBackground(holder.camOff);
        } else {
            holder.cam.setBackground(holder.camOn);
        }
//        if (stage) {
//            holder.tvBoss.setBackground(holder.bossOn);
//        }else {
//            holder.tvBoss.setBackground(holder.bossOff);
//        }
        if (host && stage) {
            holder.tvRole.setText("（管理员、主讲人）");
        } else {
            if (!stage&&!host) {
                holder.tvRole.setText("");
            }else {
                if (stage) {
                    holder.tvRole.setText("（主讲人）");
                }
                if (host) {
                    holder.tvRole.setText("（管理员）");
                }
            }

        }
        holder.mic.setOnClickListener(view -> {
            if ((null != itemClickListener) && isModerator) {
                itemClickListener.onMic(jid, !getMuteMic);
            } else {
                ToastUtils.showSingleToast("请先获取管理员资格");
            }
        });

        holder.cam.setOnClickListener(view -> {
            if ((null != itemClickListener) && isModerator) {
                itemClickListener.onCam(jid, !getVideoStatus);
            } else {
                ToastUtils.showSingleToast("请先获取管理员资格");
            }
        });

//        holder.tvBoss.setOnClickListener(v -> {
//            if ((null != itemClickListener) && isModerator) {
//                if (stage) {
//                    ToastUtils.showSingleToast("该成员已被指定为主讲人");
//                } else {
//                    itemClickListener.onBoss(jid, nickname);
//                }
//            } else {
//                ToastUtils.showSingleToast("请先获取管理员资格");
//            }
//        });
//        if (!isModerator) {
//            holder.fullLayout.setOnClickListener(v -> Toast.makeText(context, "请先获取管理员资格", Toast.LENGTH_SHORT).show());
//        }
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public class PaticipantHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_member)
        TextView tvName;
        @BindView(R.id.member_cam)
        TextView cam;
        @BindView(R.id.member_mic)
        TextView mic;
//        @BindView(R.id.member_boss)
//        TextView tvBoss;
        @BindView(R.id.tv_role_member)
        TextView tvRole;
        @BindDrawable(R.drawable.boss_off)
        Drawable bossOff;
        @BindDrawable(R.drawable.boss_on)
        Drawable bossOn;
        @BindDrawable(R.drawable.member_mic_off)
        Drawable micOff;
        @BindDrawable(R.drawable.member_mic_on)
        Drawable micOn;
        @BindDrawable(R.drawable.member_cam_off)
        Drawable camOff;
        @BindDrawable(R.drawable.member_cam_on)
        Drawable camOn;
        @BindView(R.id.item_partici)
        LinearLayout fullLayout;


        public PaticipantHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void onCam(String jid, boolean isOn);

        void onMic(String jid, boolean isOn);

//        void onBoss(String jid, String name);
    }
}
