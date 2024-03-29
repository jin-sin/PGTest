package kr.innisfree.playgreen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.BitmapWidthResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ALARM_CATEGORY;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.ToforUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NetworkData> itemArray;
    private AdapterItemClickListener adapterItemClickListener;

    public AlarmAdapter() {
    }

    public AlarmAdapter(Context context, AdapterItemClickListener listener) {
        this.context = context;
        this.adapterItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (itemArray == null) return 0;
        return itemArray.size();
    }

    public void setItemArray(ArrayList<NetworkData> array) {
        if (itemArray == null || itemArray.size() == 0) {
            this.itemArray = array;
        } else {
            itemArray.addAll(array);
        }
        notifyDataSetChanged();
    }

    public NetworkData getItem(int position) {
        if (itemArray == null || itemArray.size() <= position) return null;
        return itemArray.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (itemArray == null || itemArray.size() <= position) return;
        NetworkData item = itemArray.get(position);

        //TODO 관리자가 보낸 푸시인경우
        if (TextUtil.isNull(item.INFO_LINK)) {
            Picasso.with(context).load(R.drawable.img_alert_admin_icon).into(holder.imgProfile);
        } else {
            if (!TextUtil.isNull(item.MEMB_IMG)) {
                Picasso.with(context).load(item.MEMB_IMG).error(R.drawable.img_user_null2)
                        .transform(new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_30))).into(holder.imgProfile);
            } else {
                Picasso.with(context).load(R.drawable.img_user_null2).into(holder.imgProfile);
            }
        }

        /** 프로필 뱃지 */
        boolean isSuperGreener = false, isBestGreener = false;
        if (!TextUtils.isEmpty(item.SUPERGREENER_YN) && item.SUPERGREENER_YN.equals(Definitions.YN.YES))
            isSuperGreener = true;
        if (!TextUtils.isEmpty(item.BESTGREENER_YN) && item.BESTGREENER_YN.equals(Definitions.YN.YES))
            isBestGreener = true;
        if (isSuperGreener || isBestGreener) {
            holder.imgBadge.setVisibility(View.VISIBLE);
            if (isSuperGreener && isBestGreener)
                holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
            else if (isSuperGreener)
                holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
            else if (isBestGreener)
                holder.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
        } else {
            holder.imgBadge.setVisibility(View.GONE);
        }

        /** 내소식인경우 팔로우, 댓글, 좋아요, 베스트픽선정 알림 수신 */
        if (!TextUtil.isNull(item.INFO_CATEGORY)) {
            if (item.INFO_CATEGORY.equals(ALARM_CATEGORY.FOLLOW)) {
                holder.btnFollow.setVisibility(View.VISIBLE);
                holder.layoutThumb.setVisibility(View.GONE);
                //holder.imgThumbnail.setVisibility(View.GONE);
                if (!TextUtil.isNull(item.FOLLOWING_YN) && item.FOLLOWING_YN.equals(YN.YES))
                    holder.btnFollow.setSelected(true);
                else
                    holder.btnFollow.setSelected(false);
            } else {
                holder.btnFollow.setVisibility(View.GONE);
                holder.layoutThumb.setVisibility(View.VISIBLE);
                //holder.imgThumbnail.setVisibility(View.VISIBLE);
//                if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
//                    holder.imgPlay.setVisibility(View.VISIBLE);
//                    Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42))).into(holder.imgThumbnail);
//                } else if (!TextUtil.isNull(item.TIMELINE_IMG)) {
//                    holder.imgPlay.setVisibility(View.GONE);
//                    Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42))).into(holder.imgThumbnail);
//                }
                if (!TextUtil.isNull(item.TIMELINE_MP4_SCENE)) {
                    holder.imgPlay.setVisibility(View.VISIBLE);
//                    Picasso.with(context).load(item.TIMELINE_MP4_SCENE).transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42))).into(holder.imgThumbnail);
                }
                if (!TextUtil.isNull(item.TIMELINE_IMG)) {
                    holder.imgPlay.setVisibility(View.GONE);
                    Picasso.with(context).load(item.TIMELINE_IMG).transform(new BitmapWidthResize(context.getResources().getDimensionPixelOffset(R.dimen.dp_42))).into(holder.imgThumbnail);
                }
            }
            makeMessage(holder, item);
        }
    }

    public void makeMessage(final ViewHolder holder, NetworkData item) {
        String ago = "";
        StringBuffer message = new StringBuffer();
        int agoResId;

        message.append(item.INFO_TEXT);

        switch (item.TERM_UNIT) {
            case "D":
                agoResId = R.string.str_unit_day;
                break;
            case "W":
                agoResId = R.string.str_unit_week;
                break;
            case "H":
                agoResId = R.string.str_unit_hour;
                break;
            case "M":
                agoResId = R.string.str_unit_minute;
                break;
            case "S":
                agoResId = R.string.str_unit_second;
                break;
            default:
                agoResId = -1;
                break;
        }

        if (agoResId > 0)
            ago = context.getString(agoResId, item.TERM);

        message.append(" " + ago);
//        SpannableString span = new SpannableString(message);
//        if (!TextUtil.isNull(item.MEMB_NAME)) {
//            span.setSpan(new StyleSpan(Typeface.BOLD), 0, item.MEMB_NAME.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            span.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View v) {
//                    holder.imgProfile.performClick();
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                }
//            }, 0, item.MEMB_NAME.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        }
//        if (!TextUtil.isNull(ago)) {
//            int i = message.indexOf(ago);
//            span.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), i, i + ago.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        holder.txtMessage.setText(message.toString());
        holder.txtMessage.setMovementMethod(LinkMovementMethod.getInstance());

//		switch (item.INFO_CATEGORY) {
//			case ALARM_CATEGORY.FOLLOW:
//				messageResId = R.string.str_alarm_mine_follow_message;
//				break;
//			case ALARM_CATEGORY.LIKE:
//				messageResId = R.string.str_alarm_mine_like_message;
//				break;
//			case ALARM_CATEGORY.REGIST_COMMENT:
//				messageResId = R.string.str_alarm_mine_comment_message;
//				break;
//			case ALARM_CATEGORY.BESTPIC0K_SELECTION:
//				messageResId = R.string.str_alarm_mine_bestpick_message;
//				break;
//			default:
//				messageResId = -1;
//		}
//		if (messageResId > 0)
//			message = context.getString(messageResId, item.MEMB_NAME, ago);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout layoutItem;
        public TextView txtMessage;
        public Button btnFollow;
        public ImageView imgThumbnail, imgProfile, imgBadge, imgPlay;
        public FrameLayout layoutThumb;

        public ViewHolder(View v) {
            super(v);
            layoutThumb = (FrameLayout) v.findViewById(R.id.layout_thumb);
            imgBadge = (ImageView) v.findViewById(R.id.img_icon_badge);
            layoutItem = (LinearLayout) v.findViewById(R.id.layout_item_country);
            imgProfile = (ImageView) v.findViewById(R.id.img_item_profile);
            txtMessage = (TextView) v.findViewById(R.id.txt_item_message);
            imgThumbnail = (ImageView) v.findViewById(R.id.img_item_thumb);
            btnFollow = (Button) v.findViewById(R.id.btn_item_follow);
            imgPlay = (ImageView) v.findViewById(R.id.img_item_play);

            imgThumbnail.setOnClickListener(this);
            btnFollow.setOnClickListener(this);
            imgProfile.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (adapterItemClickListener != null)
                adapterItemClickListener.onAdapterItemClick(v, getLayoutPosition());
        }
    }
}
