package com.volley.network;

import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;

import java.util.HashMap;

import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-04-12.
 */
public class NetworkRequestFunction {

    private NetworkRequestUtil netUtil;

    public NetworkRequestFunction(NetworkRequestUtil netUtil) {
        this.netUtil = netUtil;
    }

    public void requestUserInfo(String membID) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken());
        params.put("MEMB_ID", membID);
        StringRequest myReq = netUtil.requestPost(APIKEY.MEMB_INFO, URLS.MEMB_INFO, params);
        NetworkController.addToRequestQueue(myReq);
    }

    /**
     * 타임라인 목록 및 상세조회
     *
     * @param category   E : 전체, F: 친구, M:특정회원
     * @param membId     category=M일때필수. 해당 회원에 대한 타임라인 리스트
     * @param page
     * @param timelineId 타임라인 상세조회시 필수 (category, page 무시)
     */
    public void requestTimeline(String category, String membId, String page, String timelineId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        if (!TextUtil.isNull(category)) params.put("CATEGORY", category);
        if (!TextUtil.isNull(membId)) params.put("MEMB_ID", membId);
        if (!TextUtil.isNull(page)) params.put("PAGE", page);
        if (!TextUtil.isNull(timelineId)) params.put("TIMELINE_ID", timelineId);
        StringRequest myReq = netUtil.requestPost(APIKEY.TIMELINE_LIST, URLS.TIMELINE_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestTimelineDelete(String timelineId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("TIMELINE_ID", timelineId);
        StringRequest myReq = netUtil.requestPost(APIKEY.TIMELINE_DELETE, URLS.TIMELINE_DELETE, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestLike(String targetId, String likeYN, String likeId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("LIKE_CATEGORY", "T");
        params.put("TARGET_ID", targetId);
        params.put("LIKE_YN", likeYN);
        if (!TextUtil.isNull(likeId))
            params.put("LIKE_ID", likeId);
        StringRequest myReq = netUtil.requestPost(APIKEY.LIKE_ACTION, URLS.LIKE_ACTION, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestLikeList(String likeCategory, String targetId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("LIKE_CATEGORY", likeCategory);
        params.put("TARGET_ID", targetId);
        StringRequest myReq = netUtil.requestPost(APIKEY.LIKE_LIST, URLS.LIKE_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestUserList(String type, String membId) {
        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("FRIEND_DEPT", type);
        if (TextUtil.isNull(membId)) {
            membId = PlaygreenManager.getUserInfo().MEMB_ID;
        }
        params.put("MEMB_ID", membId);
        StringRequest myReq = netUtil.requestPost(APIKEY.FRIEND_LIST, URLS.FRIEND_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestFollow(String membId, String followYN) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("MEMB_ID", membId);

        StringRequest myReq;
        if (!TextUtil.isNull(followYN) && followYN.equals(Definitions.YN.YES))
            myReq = netUtil.requestPost(APIKEY.FRIEND_UNFOLLOW, URLS.FRIEND_UNFOLLOW, params);
        else
            myReq = netUtil.requestPost(APIKEY.FRIEND_FOLLOW, URLS.FRIEND_FOLLOW, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestBlockUserList() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.BLOCK_LIST, URLS.BLOCK_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestBlock(String membId, String blockYN) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("MEMB_ID", membId);

        StringRequest myReq;
        if (!TextUtil.isNull(blockYN) && blockYN.equals(Definitions.YN.YES))
            myReq = netUtil.requestPost(APIKEY.USER_BLOCK_CANCEL, URLS.USER_BLOCK_CANCEL, params);
        else
            myReq = netUtil.requestPost(APIKEY.USER_BLOCK, URLS.USER_BLOCK, params);

        NetworkController.addToRequestQueue(myReq);
    }

    public void requestPointList(String lang) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("LANG", lang);
        StringRequest myReq = netUtil.requestPost(APIKEY.POINT_LIST, URLS.POINT_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestChangePW(String curPW, String newPW) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("MEMB_PWD_OLD", curPW);
        params.put("MEMB_PWD_NEW", newPW);
        StringRequest myReq = netUtil.requestPost(APIKEY.CHANGE_PW, URLS.CHANGE_PW, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestLeaveMember() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.LEAVE_MEMBER, URLS.LEAVE_MEMBER, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestNotice(int page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("PAGE", page + "");
        params.put("OS", "A");
        StringRequest myReq = netUtil.requestPost(APIKEY.NOTICE_LIST, URLS.NOTICE_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestMyAlarm(int page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("PAGE", page + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.ALARM_MINE, URLS.ALARM_MINE, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestFollowingAlarm(int page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("PAGE", page + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.ALARM_FOLLOWING, URLS.ALARM_FOLLOWING, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestSearchHistory(String type) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("SEARCH_DEPT", type);
        StringRequest myReq = netUtil.requestPost(APIKEY.SEARCH_HISTORY, URLS.SEARCH_HISTORY, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestHashTagSearch(String keyword, String category, String targetId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        if (!TextUtil.isNull(keyword)) params.put("HASHTAG", keyword);
        if (!TextUtil.isNull(category)) params.put("HASHTAG_CATEGORY", category);
        if (!TextUtil.isNull(targetId)) params.put("TARGET_ID", targetId);
        StringRequest myReq = netUtil.requestPost(APIKEY.SEARCH_HASHTAG, URLS.SEARCH_HASHTAG, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestUserSearch(String membName) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("MEMB_NAME", membName);
        StringRequest myReq = netUtil.requestPost(APIKEY.SEARCH_USER, URLS.SEARCH_USER, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestRemoveHistory(String type, String keyword) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        if (!TextUtil.isNull(type)) params.put("SEARCH_DEPT", type);
        if (!TextUtil.isNull(keyword)) params.put("SEARCH_KEYWORD", keyword);    //null이면 모두삭제
        StringRequest myReq = netUtil.requestPost(APIKEY.SEARCH_DELETE, URLS.SEARCH_DELETE, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestHashTagImageList(String keyword, String category, String sort, String page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        if (!TextUtil.isNull(keyword)) params.put("HASHTAG", keyword);
        if (!TextUtil.isNull(category)) params.put("HASHTAG_CATEGORY", category);
        if (!TextUtil.isNull(sort)) params.put("SORT", sort);
        if (!TextUtil.isNull(page)) params.put("PAGE", page);
        StringRequest myReq = netUtil.requestPost(APIKEY.SEARCH_HASHTAG_IMG, URLS.SEARCH_HASHTAG_IMG, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestLikePohoto(String membId, String page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        if (!TextUtil.isNull(membId)) params.put("SEARCH_MEMB_ID", membId);
        params.put("PAGE", page);
        StringRequest myReq = netUtil.requestPost(APIKEY.LIKE_PHOTO_LIST, URLS.LIKE_PHOTO_LIST, params);
        NetworkController.addToRequestQueue(myReq);
    }


    public void requestAlarmCount() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.INFO_COUNT, URLS.INFO_COUNT, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestLogout() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.LOGOUT, URLS.LOGOUT, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public void requestRegistPGpoint(String shareCategory, String targetId, String sns, String shareInfo1, String shareInfo2) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        params.put("SHARE_CATEGORY", shareCategory);
        params.put("TARGET_ID", targetId);
        params.put("SNS", sns);
//		params.put("SHARE_INFO1", PlaygreenManager.getAuthToken() + "");
//		params.put("SHARE_INFO2", PlaygreenManager.getAuthToken() + "");
        StringRequest myReq = netUtil.requestPost(APIKEY.SHARE_REGIST, URLS.SHARE_REGIST, params);
        NetworkController.addToRequestQueue(myReq);
    }
}
