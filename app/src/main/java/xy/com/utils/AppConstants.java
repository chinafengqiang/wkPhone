package xy.com.utils;

/**
 * Created by FQ.CHINA on 2015/8/28.
 */
public interface AppConstants {

    //App所在的学校
    int DEPT_ID = 1;

    String SERVER_HOST = "http://192.168.18.131:8080/xywk";

    //API基础地址
    String API_HOST = SERVER_HOST+"/api";

    //服务器存放文件的地址
    String FILE_HOST = SERVER_HOST+"/upload/";

    //获取一级分类
    String API_GET_ROOT_CATEGORY = API_HOST+"/getRootCategoryList";

    String API_GET_INDEX_CATEGORY = API_HOST+"/getIndexCategoryList";

    String API_GET_CATEGORY_BY_SN = API_HOST+"/getCategoryBySn";

    String API_GET_COURSE_BY_SN = API_HOST+"/getCourseBySn";

    String API_GET_COURSE_TREE = API_HOST+"/getCourseChapterTree";

    String API_GET_QUETION_CHAPTERUNIT = API_HOST+"/getChapterUnitQuestion";

    String API_GET_RIGHT_ANSWER = API_HOST+"/getQuestionRightAnswer";

    String API_USER_LOGIN = API_HOST+"/userLogin";

    String API_SAVE_COURSE = API_HOST+"/saveCourseRule";

    String API_GET_USER_COURSE = API_HOST+"/getUserCourses";

    String API_GET_USER_INFO = API_HOST+"/getUserAndAccount";

    String API_SAVE_USER_INFO = API_HOST+"/saveUserInfo";

    String API_SAVE_USER_QUESTION = API_HOST+"/saveUserQuestion";

    String API_SAVE_USER_QUESTION_RESP = API_HOST+"/saveUserQuestionResp";

    String API_GET_COURSE_INFO = API_HOST+"/getCourseChapterInfo";

    String API_GET_CHAPTER_ORDER_UNIT = API_HOST+"/getOrderChapterInfo";

    String API_GET_USER_TYPE_QUESTION = API_HOST+"/getUserTypeQuestion";

    String API_REMOVE_USER_TYPE_QUESTION = API_HOST+"/removeUserQuestion";

    String API_SEND_CODE = API_HOST+"/sendCode";

    String API_USER_REGISTER = API_HOST+"/register";

    /*
       试题类型
     */
    int QUESTION_TYPE_DX = 10;//多选
    int QUESTION_TYPE_DDX = 20;//多选
    int QUESTION_TYPE_PD = 30;//判断
    int QUESTION_TYPE_TK = 40;//填空
    int QUESTION_TYPE_JD = 50;//简答


    /*
      试题分类
     */
    int QUESTION_CLASS_ZJLX = 1;
    int QUESTION_CLASS_LNZT = 2;
    int QUESTION_CLASS_MNLX = 3;

    int QUESTION_CLASS_SC = 100;//收藏
    int QUESTION_CLASS_ERR = 200;//错题

    /*
      资料的下载地址
     */
    String COURSE_DOWNLOAD_URL = "xywk_course";
    String COURSE_DOWNLOAD_HTML_DIR = "resource";
    String COURSE_DOWNLOAD_VIDEO_DIR = "video";
    /*
      课程封面图片地址
     */
    String COURSE_ICON_PRE = "/upload/file/";


}
