package xy.com.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.widget.EditText;

public class SmsContent extends ContentObserver {
    private Cursor cursor = null;
    private Activity activity;
    private EditText verifyText;
    public SmsContent(Activity activity, Handler handler, EditText verifyText) {
        super(handler);
        this.activity = activity;
        this.verifyText = verifyText;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange); // 读取收件箱中指定号码的短信
        cursor = activity.managedQuery(Uri.parse("content://sms/inbox"), new String[] {
                        "_id", "address", "read", "body" }, " read=?",
                new String[] { "0" }, "date desc");
        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            cursor.moveToNext();
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            String pass = CommonUtil.getDynamicPassword(smsBody);
            // 在这里把需要的验证码填到编辑框
            verifyText.setText(pass);
        }

        if(VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }
}
