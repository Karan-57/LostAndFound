package com.example.lostandfound;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends BaseAdapter {

    public interface OnItemResolveListener {
        void onItemResolve(ItemModel item);
    }

    private final Context context;
    private final List<ItemModel> itemList;
    private final LayoutInflater inflater;
    private boolean showResolveButton = false;
    private OnItemResolveListener resolveListener;

    public ItemAdapter(Context context, List<ItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setShowResolveButton(boolean showResolveButton, OnItemResolveListener listener) {
        this.showResolveButton = showResolveButton;
        this.resolveListener = listener;
    }

    @Override
    public int getCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_post_card, parent, false);
            holder = new ViewHolder();
            holder.tvCardLocation = convertView.findViewById(R.id.tvCardLocation);
            holder.tvCardDate = convertView.findViewById(R.id.tvCardDate);
            holder.tvCardDescription = convertView.findViewById(R.id.tvCardDescription);
            holder.ivCardImage = convertView.findViewById(R.id.ivCardImage);
            holder.btnCardCall = convertView.findViewById(R.id.btnCardCall);
            holder.btnCardResolve = convertView.findViewById(R.id.btnCardResolve);
            holder.tvCardUser = convertView.findViewById(R.id.tvCardUser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemModel item = itemList.get(position);
        holder.tvCardLocation.setText(item.getLocation());
        holder.tvCardDate.setText(item.getDate());
        holder.tvCardDescription.setText(item.getDescription());

        String postedBy = item.getPostedBy();
        if (postedBy != null && !postedBy.trim().isEmpty()) {
            holder.tvCardUser.setText("Posted by: " + postedBy);
        } else {
            holder.tvCardUser.setText("Posted by: Student");
        }

        boolean imageSet = false;
        if (item.getImageUri() != null) {
            try {
                holder.ivCardImage.setImageURI(item.getImageUri());
                if (holder.ivCardImage.getDrawable() != null) {
                    imageSet = true;
                }
            } catch (Exception e) {
                imageSet = false;
            }
        }
        if (!imageSet) {
            if (item.getImageResId() != 0) {
                holder.ivCardImage.setImageResource(item.getImageResId());
            } else {
                holder.ivCardImage.setImageResource(R.drawable.campus_bg);
            }
        }

        if (showResolveButton) {
            holder.btnCardResolve.setVisibility(View.VISIBLE);
            holder.btnCardResolve.setOnClickListener(v -> {
                if (resolveListener != null) {
                    resolveListener.onItemResolve(item);
                }
            });
        } else {
            holder.btnCardResolve.setVisibility(View.GONE);
        }

        String phone = item.getPhone();
        if (phone != null && !phone.isEmpty()) {
            holder.btnCardCall.setVisibility(View.VISIBLE);
            holder.btnCardCall.setOnClickListener(v -> {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone));
                context.startActivity(dialIntent);
            });
        } else {
            holder.btnCardCall.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvCardLocation;
        TextView tvCardDate;
        TextView tvCardDescription;
        TextView tvCardUser;
        ImageView ivCardImage;
        ImageButton btnCardCall;
        ImageButton btnCardResolve;
    }
}
