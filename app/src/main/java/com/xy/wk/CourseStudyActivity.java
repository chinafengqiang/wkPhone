package com.xy.wk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xy.dao.CourseService;
import com.xy.view.TitleBarView;
import com.xy.vo.CategoryList;
import com.xy.vo.CourseInfoVO;
import com.xy.vo.UserCourseingSp;
import com.xy.volley.FRestClient;
import com.xy.volley.FastJsonRequest;

import xy.com.utils.AppConstants;
import xy.com.utils.DownFileTask;
import xy.com.utils.FileService;
import xy.com.utils.SpUtil;
import xy.com.utils.ToastUtil;
import xy.com.utils.ZipUtils;

/**
 * Created by FQ.CHINA on 2015/9/8.
 */
public class CourseStudyActivity extends BaseFragmentActivity {
    private TitleBarView mTitleBarView;
    private int courseId;
    private String courseName = "";
    private RelativeLayout course_study_chapter, lay_study_chapter_lx, lay_study_mn, lay_study_zt;
    private Button button_download;
    private String course_path;
    private SharedPreferences sp;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
        course_study_chapter = (RelativeLayout) findViewById(R.id.course_study_chapter);
        lay_study_chapter_lx = (RelativeLayout) findViewById(R.id.lay_study_chapter_lx);
        lay_study_mn = (RelativeLayout) findViewById(R.id.lay_study_mn);
        lay_study_zt = (RelativeLayout) findViewById(R.id.lay_study_zt);
        button_download = (Button) findViewById(R.id.button_download);
    }

    @Override
    protected void initTitle() {
        mTitleBarView.setTitleText(courseName);
        mTitleBarView.setTitleComeBackVisibility(View.VISIBLE);
        mTitleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                finish();
            }
        });
    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
        courseId = getIntent().getIntExtra("courseId", 0);
        courseName = getIntent().getStringExtra("courseName");
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.course_study);
    }

    @Override
    protected void processLogic() {
        FileService fileService = new FileService(mContext);
        course_path = fileService.getSdCardDirectory()+"/"+AppConstants.COURSE_DOWNLOAD_URL+"/"+courseId+"/";
    }

    @Override
    protected void setListener() {
        course_study_chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uingCourse = sp.getInt(UserCourseingSp.C_ING_ID+"_"+courseId,0);
                Intent intent = null;
                if(uingCourse > 0){
                    intent = new Intent(mContext, CourseStudyChapterResActivity.class);
                    String purl = sp.getString(UserCourseingSp.C_ING_P_URL+"_"+courseId,"");
                    String vurl = sp.getString(UserCourseingSp.C_ING_V_URL+"_"+courseId,"");
                    String title = sp.getString(UserCourseingSp.C_ING_TITLE+"_"+courseId,"");
                    int unitId = sp.getInt(UserCourseingSp.C_ING_UNIT_ID+"_"+courseId,0);
                    intent.putExtra(CourseStudyChapterResActivity.C_RES_ID, courseId);
                    intent.putExtra(CourseStudyChapterResActivity.P_URL, purl);
                    intent.putExtra(CourseStudyChapterResActivity.V_URL, vurl);
                    intent.putExtra(CourseStudyChapterResActivity.TITLE_TEXT, title);
                    intent.putExtra(CourseStudyChapterResActivity.C_LOAD_FROM,1);
                    intent.putExtra(CourseStudyChapterResActivity.C_RES_NAME,courseName);
                    intent.putExtra(CourseStudyChapterResActivity.C_UNIT_ID, unitId);
                }else{
                    intent = new Intent(mContext, CourseStudyChapterTreeActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("courseName", courseName);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        lay_study_chapter_lx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseStudyChapterTreeActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("courseName", courseName);
                intent.putExtra("isQuestionChapter", true);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        /*lay_study_mn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseQuestionChapterActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("queClass", AppConstants.QUESTION_CLASS_MNLX);
                intent.putExtra("title", mContext.getString(R.string.course_moni));
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        lay_study_zt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseQuestionChapterActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("queClass", AppConstants.QUESTION_CLASS_LNZT);
                intent.putExtra("title", mContext.getString(R.string.course_linian));
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });*/

        lay_study_mn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CoursePaperActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("queClass", AppConstants.QUESTION_CLASS_MNLX);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        lay_study_zt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CoursePaperActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("queClass", AppConstants.QUESTION_CLASS_LNZT);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCourse();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void downloadCourse() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(getText(R.string.download_course_pre));
        pDialog.show();

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_COURSE_INFO + "?courseId=" + courseId;

        FastJsonRequest<CourseInfoVO> fastRequest = new FastJsonRequest<CourseInfoVO>(Request.Method.GET, url, CourseInfoVO.class, null, new Response.Listener<CourseInfoVO>() {

            @Override
            public void onResponse(CourseInfoVO resVO) {
                if (resVO != null) {
                    CourseService service = new CourseService(mContext);
                    boolean res = service.saveCourse(resVO);
                    if(res){
                        String fileName = courseId+".zip";
                        DownFileTask task = new DownFileTask(mContext, handler, fileName,
                                AppConstants.COURSE_DOWNLOAD_URL);
                        String downloadUrl = AppConstants.FILE_HOST+courseId+"/"+courseId+".zip";
                        task.execute(downloadUrl);
                    }
                }
                pDialog.dismiss();
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            int result = msg.what;
            String fileName = (String)msg.obj;
            if(result == 1){
                ToastUtil.showToast(mContext, R.string.download_ok);
                try{
                    ZipUtils.unZip(fileName,course_path);
                }catch (Exception e){
                    ToastUtil.showToast(mContext,R.string.unzip_fail);
                }

            }else{
                ToastUtil.showToast(mContext,R.string.download_fail);
            }
        }
    };
}
