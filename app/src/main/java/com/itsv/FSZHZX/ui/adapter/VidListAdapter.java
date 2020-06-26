package com.itsv.FSZHZX.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsv.FSZHZX.R;
import com.just.agentweb.LogUtils;
import com.manis.core.entity.SurfaceViewHolder;
import com.manis.core.interfaces.ManisApiInterface;

import org.webrtc.TextureViewRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VidListAdapter extends RecyclerView.Adapter<VidListAdapter.VidHolder> {

    private String TAG = "RecyclerListAdapter";
    private Map<String, SurfaceViewHolder> viewList = new HashMap<>();
//    private Context context;
    private List<String> jids = null;
    private boolean isFront = true;      //视频方向，true为镜面，false为正常
    private final LayoutInflater inflater;

    public VidListAdapter(Context context) {
//        this.context = context;
        inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    public void setVideoPosition(List<String> jids) {
        this.jids = jids;
        notifyDataSetChanged();
    }

    public void updateViewList(Map<String, SurfaceViewHolder> viewList) {
        this.viewList = viewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video, parent, false);
        return new VidHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VidHolder holder, int position) {
        String jid = jids.get(position);
        SurfaceViewHolder surfaceViewHolder;
        if (jid.contains(ManisApiInterface.app.getUserEndPoint())) {
            surfaceViewHolder = viewList.get(ManisApiInterface.app.getUserEndPoint());
        } else {
            surfaceViewHolder = viewList.get(jid);
        }
        if (surfaceViewHolder == null) {
            holder.tv_video.setVisibility(View.INVISIBLE);
            return;
        }

        holder.tv_video.setVisibility(View.VISIBLE);
        if (holder.viewHolder == null) {
            if (ManisApiInterface.app.getUserJid() == surfaceViewHolder.getJid()) {
                surfaceViewHolder.initTextureView(holder.tv_video, true, false);
                surfaceViewHolder.bundleTextureView(holder.tv_video);
                holder.tv_video.setMirror(isFront);
            } else {
                surfaceViewHolder.initTextureView(holder.tv_video, false, false);
                surfaceViewHolder.bundleTextureView(holder.tv_video);
            }
        } else {
            if (holder.viewHolder != surfaceViewHolder) {
                holder.viewHolder.unbundleTextureView(holder.tv_video);
                holder.tv_video.clearImage();
                if (ManisApiInterface.app.getUserJid() == surfaceViewHolder.getJid()) {
                    surfaceViewHolder.bundleTextureView(holder.tv_video, true);
                    holder.tv_video.setMirror(isFront);
                } else {
                    surfaceViewHolder.bundleTextureView(holder.tv_video, false);
                }
            } else {
                LogUtils.i(TAG, holder.viewHolder.getJid());
            }
        }
        holder.viewHolder = surfaceViewHolder;
    }

    @Override
    public int getItemCount() {
        if ((null == jids) || jids.isEmpty()) {
            return 0;
        }
        return Math.min(jids.size(), 9);
    }
    public void setLocalMirror( boolean isFront) {
        this.isFront = isFront;
        notifyDataSetChanged();
    }

    public class VidHolder extends RecyclerView.ViewHolder {
        TextureViewRenderer tv_video;
        SurfaceViewHolder viewHolder;
        public VidHolder(@NonNull View itemView) {
            super(itemView);
            tv_video = itemView.findViewById(R.id.tv_video);
        }
    }
}
