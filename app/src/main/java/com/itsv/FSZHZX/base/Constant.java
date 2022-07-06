package com.itsv.FSZHZX.base;

public class Constant {

    //    public static String DEVICE_ID = "";
    public static String SP_NAME = "fszx";
    public static final String BASEURL = "https://www.fszxpt.cn:9528/api/";//正式
    public static final String BASEURL2 = "https://www.fszxpt.cn:9526/";//正式
    //    public static final String logoutURL = "https://www.fszxpt.cn:9528/api/login/";//正式
//    public static final String UserURL = "https://www.fszxpt.cn:9528/api/User/";//正式
//    public static final String questionURL = "https://www.fszxpt.cn:9528/api/questionBank/";//正式
//    public static final String notifiURL = "https://www.fszxpt.cn:9528/api/meetingUser/";//正式
//    public static final String meetingURL = "https://www.fszxpt.cn:9528/api/meetingBase/";//正式
    public static final String appURL = "https://www.fszxpt.cn:9528/api/App/";//正式
    public static String WEBURL = "https://fs.itsv.com.cn:9530/";//正式

    public static String TOKEN = "";//正式
    public static int SCREEN_DENSITY;
    public static String FilePath = "";
    public static String PDFPath = "";
    public static String APP_ID = "wx13088771b5a0334b";
    public static String USER_NAME = "";
    public static String IMEI = "";
    public static String PDF_Link = "pdf_link";
    public static String PDF_FILE = "pdf_file";
    public static int FILE_0_LENGTH = 555;//下载的源文件大小为0kb的标识码
    //h5的url
//    public static String BASE_H5_URL = "http://fszxta.itsv.com.cn:2021/";
    public static String BASE_H5_URL = "https://www.fszxpt.cn:9526/";
//    public static String BASE_H5_URL = "http://www.fszxpt.cn:8080/";

    /**
     * 提案查询
     */
    public static String TAG_PROPOSAL = "sjstian.app.do?";
    public static String param_proposal = "m=getTianListView";

    /**
     * 社情民意
     */
    public static String TAG_SITUATION = "sheqingminyi.app.do?";
    public static String param_situation = "m=getSqmyListView";

    /**
     * 履职
     */
    //档案
    public static String TAG_DUTY_FILE = "lvzhi_dangan.app.do?";
    public static String listDutyFile = "m=getDanganByUserName";
    //积分
    public static String TAG_DUTY_SCORE = "lvzhi_jifen.app.do?";
    public static String paramScore = "m=getJifenByUserName";


    /**
     * 文件互传
     */
    public static String TAG_FILE_EXCHANGE = "file_receive.app.do?";
    public static String listFileEXchange = "m=getFileListView";

    /**
     *政协
     */
    public static String TAG_CPPCC = "zxwj.app.do?";//政协业务
    public static String listCPPCC = "m=getZxwjListView";//政协业务
//    public static String detailsCPPCC = "m=getZxwjDetailView";//政协业务


    /**
     * 公告
     */
    public static String TAG_NOTICE = "gg.app.do?";//公告业务
    public static String listNotice = "m=getGgListView";//公告业务
//    public static String detailsNotice = "m=getGgDetailView";//公告业务
}
