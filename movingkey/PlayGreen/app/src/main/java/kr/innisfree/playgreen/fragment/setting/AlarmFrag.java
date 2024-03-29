package kr.innisfree.playgreen.fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.ParentFrag;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.adapter.AlarmAdapter;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.dialog.FollowCancelDialog;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;
import kr.innisfree.playgreen.listener.RecyclerOnScrollListener;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class AlarmFrag extends ParentFrag implements AdapterItemClickListener {

    private NetworkData selectItem;
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private LinearLayout layoutEmpty;
    private TextView txtEmpty;

    private RecyclerOnScrollListener recyclerOnScrollListener;
    private int page;
    private NetworkData paging;

    public static AlarmFrag newInstance() {
        return new AlarmFrag();
    }

    public AlarmFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_alarm, null);
        setCutOffBackgroundTouch(view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlarmAdapter(getContext(), this);

        layoutEmpty = (LinearLayout) view.findViewById(R.id.empty_view);
        txtEmpty = (TextView) view.findViewById(R.id.txt_empty_message);

        page = 1;
        netFunc.requestMyAlarm(page);

//		if (paging == null || page == 0)	page = 1;
        recyclerOnScrollListener = new RecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (paging != null && paging.TOTAL_PAGE > page) {
                    page += 1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            netFunc.requestMyAlarm(page);
                        }
                    }, 300);
                }
            }
        };

        recyclerOnScrollListener.setLinearLayoutManager((LinearLayoutManager) recyclerView.getLayoutManager());
        recyclerView.addOnScrollListener(recyclerOnScrollListener);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAdapterItemClick(View view, int position) {
        Intent intent = null;
        selectItem = adapter.getItem(position);
        if (selectItem == null) return;
        switch (view.getId()) {
            case R.id.img_item_profile:
                if (!TextUtil.isNull(selectItem.INFO_LINK)) {
                    intent = new Intent(getActivity(), ProfileDetailAct.class);
                    intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, selectItem.MEMB_ID);
                    Util.moveActivity(getActivity(), intent, -1, -1, false, false);
                }
                break;
            case R.id.img_item_thumb:
                intent = new Intent(getActivity(), TimelineDetailAct.class);
                intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
                Util.moveActivity(getActivity(), intent, -1, -1, false, false);
                break;
            case R.id.btn_item_follow:
                if (!TextUtil.isNull(selectItem.FOLLOWING_YN) && selectItem.FOLLOWING_YN.equals(Definitions.YN.YES)) {
                    FollowCancelDialog dialog = new FollowCancelDialog();
                    dialog.setupDialog(selectItem, new DialogListener() {
                        @Override
                        public void onSendDlgData(DialogData dialogData) {
                            if (dialogData != null && dialogData.dialogButtonType == Definitions.DIALOG_SELECT.CONFIRM) {
                                netFunc.requestFollow(selectItem.MEMB_ID, selectItem.FOLLOWING_YN);
                            }
                        }
                    });
                    ((ParentAct) getActivity()).dialogShow(dialog, "cancel dialog");
                } else {
                    netFunc.requestFollow(selectItem.MEMB_ID, selectItem.FOLLOWING_YN);
                }
                //netFunc.requestFollow(selectItem.MEMB_ID, selectItem.FOLLOWING_YN);
                break;
        }
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case APIKEY.ALARM_MINE:
                paging = json.PAGING;
                adapter.setItemArray(json.LIST);
                if (adapter.getItemCount() == 0) {
                    layoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    layoutEmpty.setVisibility(View.GONE);
                }
                if (recyclerOnScrollListener != null)
                    recyclerOnScrollListener.setLoading(true);
                netFunc.requestAlarmCount();
                break;
            case APIKEY.FRIEND_FOLLOW:
            case APIKEY.FRIEND_UNFOLLOW:
                if (selectItem != null && !TextUtil.isNull(selectItem.FOLLOWING_YN) && selectItem.FOLLOWING_YN.equals(YN.NO)) {
                    selectItem.FOLLOWING_YN = YN.YES;
                } else {
                    selectItem.FOLLOWING_YN = YN.NO;
                }
                adapter.notifyDataSetChanged();
                break;
            case APIKEY.INFO_COUNT:
                Definitions.ALARM_COUNT = json.INFO_COUNT;
                break;
        }
    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
    }

    @Override
    public void onNetworkStart(int idx) {
        super.onNetworkStart(idx);
    }
}
