package com.iceman.yangtze;

import java.io.IOException;

import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConstant;
import com.special.ResideMenuDemo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

public abstract class HandActivity extends Activity{
	public Dialog mNetLoadingDialog;

    protected NetClient mNetClient;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
        }
        return super.onKeyDown(keyCode, event);

    }

    public void showNetLoadingDialog() {
        if (mNetLoadingDialog == null) {
            mNetLoadingDialog = new Dialog(this, R.style.laoding_progress_dialog_style);
            mNetLoadingDialog.setContentView(R.layout.loading_progress_bar_layout);
            mNetLoadingDialog.setCancelable(true);
            mNetLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Globe.sNetClient.getmRequestTemp().clear();
                }
            });
        }
        mNetLoadingDialog.show();
    }

    public abstract void handUiCourse(String html);

    public void dismissNetLoadingDialog() {
        if (mNetLoadingDialog != null && mNetLoadingDialog.isShowing()) {
            mNetLoadingDialog.dismiss();
        }
    }

    public void showNetErrorDialog() {

    }

    public static void showWeekCourse() throws IOException{
    	
    }
    public void showTipDialog(String str) {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle("小伙伴们").setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		} );
    	builder.create().show();

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == NetConstant.MSG_NONE_NET) {
            mAlertDialog = Util.createAlertDialog(this, this.getString(R.string.tishi),
                    this.getString(R.string.none_net), this.getString(R.string.quit), null,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mNetClient.isRunning = false;
                            finish();
                        }
                    }, null);
            return mAlertDialog;
        } else if (id == NetConstant.MSG_NET_ERROR) {
            mAlertDialog = Util.createAlertDialog(this, this.getString(R.string.tishi),
                    this.getString(R.string.net_error), this.getString(R.string.confirm),
                    this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mNetClient.isRunning = false;
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            return mAlertDialog;
        } else if (id == NetConstant.MSG_PASSWORD_ERROR) {
            mAlertDialog = Util.createAlertDialog(this, this.getString(R.string.tishi),
                    this.getString(R.string.password_error), this.getString(R.string.confirm),
                    this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mNetClient.isRunning = false;
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            return mAlertDialog;
        } else if (id == NetConstant.MSG_SERVER_ERROR) {
            mAlertDialog = Util.createAlertDialog(this, this.getString(R.string.tishi),
                    this.getString(R.string.server_error), this.getString(R.string.confirm),
                    this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mNetClient.isRunning = false;
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            return mAlertDialog;
        } else {
            mAlertDialog = Util.createAlertDialog(this, this.getString(R.string.tishi),
                    this.getString(R.string.other_errors), this.getString(R.string.confirm),
                    this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mNetClient.isRunning = false;
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
        }
        return mAlertDialog;
    }
}
