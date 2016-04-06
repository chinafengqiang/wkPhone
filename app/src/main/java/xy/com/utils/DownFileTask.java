package xy.com.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xy.wk.R;


public class DownFileTask extends AsyncTask<String, Integer, String> {

	ProgressDialog pdialog;//用于显示进度的Dialog
	Handler handler;  //用于下载完成之后的客户端回调
	Context context;  //上下文对象
	String fileName = ""; //要下载资源名称
	FileService fileService =null;  //工具类
	private boolean isFinished = false; //用于标志是否下载完成


	public DownFileTask(Context context, Handler handler, String fileName, String pathName) {
		this.context = context;
		this.handler = handler;

		fileService = new FileService(context);
		this.fileName = fileService.getSdCardDirectory()+"/"+pathName+"/"+ fileName;
		
		pdialog = new ProgressDialog(context);
		pdialog.setButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				dialog.cancel();
				DownFileTask.this.cancel(true);
			}
		});

		pdialog.setIcon(R.mipmap.flag);
		pdialog.setTitle(R.string.download_course_downloading);
		pdialog.setCancelable(false);
		pdialog.setMax(100);
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.show();
	}

	@Override
	protected void onPreExecute() {
		boolean isNet = NetworkUtil.hasNetwork(context);
		if (!isNet) {
			ToastUtil.showToast(context, R.string.get_data_fail);
			pdialog.cancel();
			DownFileTask.this.cancel(true);
			return;
		}

	}

	@Override
	protected void onPostExecute(String result) {
		if (this.handler != null) {
			if (result != null&&"ok".equals(result)) {
				Message msg = new Message();
				msg.obj = this.fileName;
				msg.what = 1;
				handler.sendMessage(msg);
				this.isFinished = true;
				
			}else{
				ToastUtil.showToast(context,R.string.download_fail);
			}
		}
		pdialog.dismiss();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		pdialog.setProgress(values[0]);
	}

	@Override
	protected void onCancelled() {
		ToastUtil.showToast(context,R.string.download_cancle);
		if (!this.isFinished){
		   FileUtils.delFile(this.fileName);
		}
		super.onCancelled();
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			int count;
			URL url = new URL(params[0]);
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(10*1000);
			//conn.connect();

			int lenghtOfFile = conn.getContentLength();
			Log.d("DownFileTask", "Lenght of file: " + lenghtOfFile);
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(this.fileName);
			byte data[] = new byte[4096];
			long total = 0;
			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress((int) ((total * 100) / lenghtOfFile));
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();
			return "ok";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return null;
	}

}