package kr.innisfree.playgreen.fragment.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.dto.NetworkData;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-03-22.
 */
public class BestPickUIMaker {

	private Context context;
	private View view;
	private View.OnClickListener onClickListener;
	private ArrayList<NetworkData> dataArray;
	private ArrayList<View> itemViewArray;

	private GridLayout layoutBestPickContent;
	private LinearLayout layoutOpenMore;
	private TextView txtOpenMore;

	public BestPickUIMaker(Context context,  View view, View.OnClickListener listener){
		this.view = view;
		this.context = context;
		this.onClickListener = listener;
		init();
	}

	public void init(){
		layoutBestPickContent = (GridLayout)view.findViewById(R.id.layout_bestpick_content);
		layoutOpenMore = (LinearLayout)view.findViewById(R.id.layout_bestpick_more);
		txtOpenMore = (TextView)view.findViewById(R.id.txt_bestpick_more);
		layoutOpenMore.setOnClickListener(onClickListener);
	}

	public void setData(ArrayList<NetworkData> datas){
		this.dataArray = datas;
		makeBestPickArray();
	}

	public void makeBestPickArray(){
		if(dataArray==null || dataArray.size() == 0)	return;

		itemViewArray = new ArrayList<View>();
		for(NetworkData data: dataArray){
			View itemView = LayoutInflater.from(context).inflate(R.layout.view_pick_best_item, null);
			ViewHolder vh = new ViewHolder(itemView);
			if(!TextUtil.isNull(data.TIMELINE_IMG)){
				Picasso.with(context).load(data.TIMELINE_IMG).into(vh.imgContent);
			}
			if(!TextUtil.isNull(data.MEMB_IMG)){
				Picasso.with(context).load(data.MEMB_IMG).skipMemoryCache().transform(
						new BitmapCircleResize(context, context.getResources().getDimensionPixelOffset(R.dimen.dp_35))).error(R.drawable.img_user_null2).into(vh.imgProfile);
			}

			/** 프로필 뱃지 */
			boolean isSuperGreener = false, isBestGreener = false;
			if(!TextUtils.isEmpty(data.SUPERGREENER_YN) && data.SUPERGREENER_YN.equals(Definitions.YN.YES))
				isSuperGreener = true;
			if(!TextUtils.isEmpty(data.BESTGREENER_YN) && data.BESTGREENER_YN.equals(Definitions.YN.YES))
				isBestGreener = true;
			if(isSuperGreener || isBestGreener){
				vh.imgBadge.setVisibility(View.VISIBLE);
				if(isSuperGreener && isBestGreener)
					vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
				else if(isSuperGreener)
					vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
				else if(isBestGreener)
					vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
			}else{
				vh.imgBadge.setVisibility(View.GONE);
			}

			if(!TextUtil.isNull(data.MEMB_NAME))
				vh.txtName.setText(data.MEMB_NAME);
			vh.txtDate.setText(PlaygreenManager.getTimeStampToDate(data.REG_DT, false));

			vh.imgProfile.setTag(data);
			vh.imgProfile.setOnClickListener(onClickListener);

			itemView.setId(R.id.id_bestpick_item);
			itemView.setTag(data);
			itemView.setOnClickListener(onClickListener);
			itemViewArray.add(itemView);
		}

		initializeView();
	}

	public void initializeView(){
		currentPage = 0;
		totalPage = dataArray.size()/4;
		if(totalPage == 0){
			layoutOpenMore.setVisibility(View.GONE);
		}
		layoutBestPickContent.removeAllViews();
		openMore(false);
	}

	private int currentPage, totalPage;
	public void openMore(boolean isMore){
		if(currentPage>0 && currentPage == totalPage){
			initializeView();
			return;
		}

		if(isMore) currentPage+=1;
		int startIndex = currentPage * 4;

		//JYLog.D("cur:" + currentPage +", tot: " + totalPage, new Throwable());
		for(int i = startIndex; i< startIndex+4; i++ ){
			if(itemViewArray.size() <= i) break;
			layoutBestPickContent.addView(itemViewArray.get(i));
		}
		if(currentPage < totalPage){
			txtOpenMore.setText(R.string.str_open_more);
			txtOpenMore.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_unfold1,0);
		}else{
			txtOpenMore.setText(R.string.str_close);
			txtOpenMore.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_fold1,0);
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView imgContent, imgProfile, imgBadge;
		public TextView txtName, txtDate;

		public ViewHolder(View v) {
			super(v);
			imgContent = (ImageView)v.findViewById(R.id.img_bestpick_item);
			imgProfile = (ImageView)v.findViewById(R.id.img_bestpick_profile_item);
			imgBadge = (ImageView)v.findViewById(R.id.img_bestpick_badge_item);
			txtName = (TextView)v.findViewById(R.id.txt_bestpick_name_item);
			txtDate = (TextView)v.findViewById(R.id.txt_bestpick_date_item);
		}
	}

}
