package com.itsv.FSZHZX.api;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    /**
     * 根据姓名检索
     */
    @POST("User/selectUsers")
    Call<ResponseBody> selectUsers(@Query("jieci") String jieci, @Query("name") String name, @Query("type") String type, @Query("typeValue") String typeValue);

    /**
     * 根据界别（第二个spinner姓名之外的）检索
     */
    @POST("User/selectGroupUsers")
    Call<ResponseBody> selectGroupUsers(@Query("jieci") String jieciID, @Query("type") String type);

    /**
     * 获取界次（第一个spinner）
     */
    @POST("User/selectGroupBase")
    Call<ResponseBody> selectGroupBase();

    //我的
    @POST("User/personalData")
    Call<ResponseBody> personalData(@Query("token") String token);

    //个人详情

    @POST("User/userDetailInfo")
    Call<ResponseBody> userDetailInfo(@Query("token") String token);


    @POST("questionBank/getRoundQuestion")
//题目列表
    Call<ResponseBody> getRoundQuestion(@Query("token") String token);

    //修改密码
    @POST("User/editPassword")
    Call<ResponseBody> editPassword(@Query("token") String token, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    //修改个人信息
    @POST("User/editUserInfo")
    Call<ResponseBody> modifyBirthday(@Query("token") String token, @Query("birthday") String birthday);//修改个人信息

    @POST("User/editUserInfo")
    Call<ResponseBody> modifyName(@Query("token") String token, @Query("name") String name);//修改个人信息

    @POST("User/editUserInfo")
    Call<ResponseBody> modifyGerder(@Query("token") String token, @Query("sex") String sex);

    //修改头像
    @POST("editAvatar")
    Call<ResponseBody> editAvatar(@Query("token") String token, @Query("msg") String msg);

    @POST("questionBank/answerOnline")
    Call<ResponseBody> answerOnline(@Query("token") String token, @Query("startTime") String startTime,
                                    @Query("questionIds") String questionIds, @Query("answers") String answers, @Query("scores") String scores,
                                    @Query("duration") String duration, @Query("totalScore") String totalScore);

    //会议
    //会议通知
    @POST("meetingUser/adviceList")
    Call<ResponseBody> adviceList(@Query("token") String token, @Query("currentPage") int currentPage, @Query("pageSize") int pageSize);

    //视频会议
    @POST("meetingBase/userIndexList")
    Call<ResponseBody> userIndexList(@Query("token") String token, @Query("currentPage") int currentPage, @Query("pageSize") int pageSize
            , @Query("meetingTitle") String meetingTitle, @Query("meetingOwnerUserName") String meetingOwnerUserName, @Query("meetingTime") String meetingTime);

    //会议详情
    @POST("meetingBase/detail")
    Call<ResponseBody> detail(@Query("id") long id);

    @POST("previewPDF")
    Call<ResponseBody> previewPDF(@Query("fileid") String fileid, @Query("fileName") String fileName);

    //是否参会  是否参会（“1”：确认参会；“2”：确认不参会）
    @POST("meetingUser/editItem")
    Call<ResponseBody> editItem(@Query("id") long id, @Query("isCanhui") String isCanhui, @Query("content") String content);

    //stage
    @POST("meetingBase/setStageByRoomNumber")
    Call<ResponseBody> setStageByRoomNumber(@Query("roomNumber") String roomNumber, @Query("userId") String userId);

    //stage
    @POST("getStageByRoomNumber")
    Call<ResponseBody> getStageByRoomNumber(@Query("roomNumber") String roomNumber);

    //app更新
    @POST("getAppVersion")
    Call<ResponseBody> getAppVersion(@Query("token") String token);

//    @POST("downloadApp")
//    Call<ResponseBody> downloadApp(@Query("version") String version);

    //创建临时会议
    @POST("meetingBase/createTempConference")
    Call<ResponseBody> createTempConference(@Query("token") String token);

    @POST("meetingBase/validEnableInto")
    Call<ResponseBody> validEnableInto(@Query("token") String token, @Query("sdkRoomNum") String sdkRoomNum);

    /*  //进入会议调用
      @POST("handleUserIdAndJid")
      Call<ResponseBody> handleUserIdAndJid(@Query("clientType") String clientType, @Query("userId") String userId, @Query("roomNum") String roomNum,
                                            @Query("stageJid") String stageJid, @Query("newJid") String newJid);*/
    //会议结束
    @POST("meetingBase/closeMeeting")
    Call<ResponseBody> closeMeeting(@Query("token") String token, @Query("roomNum") String roomNum);

    //会议自动签到
    @GET("meetingBase/autoCheckIn")
    Call<ResponseBody> autoCheckin(@Query("token") String token, @Query("roomNumber") String roomNum, @Query("userId") String userId);

    //通知公告未读数量
    @GET("gg.app.do?m=getGgUnreadNum")
    Call<ResponseBody> queryNoticeUnreadCount(@Query("userName") String userName, @Query("userId") String userId);

    //未读政协文件数量
    @GET("zxwj.app.do?m=getZxwjUnreadNum")
    Call<ResponseBody> queryCppccFileUnreadCount(@Query("userName") String userName, @Query("userId") String userId);

    //https://www.fszxpt.cn:9528/api/meetingUser/adviceList

    @GET("meetingUser/adviceList")
    Call<ResponseBody> queryMeetingNoticeUnreadCount(@Query("userName") String userName, @Query("userId") String userId, @Query("token") String token);

    @GET("file_receive.app.do?m=getFile_receiveUnreadNum")
    Call<ResponseBody> queryFileExchangeUnreadCount(@Query("userName") String userName, @Query("userId") String userId);

}
