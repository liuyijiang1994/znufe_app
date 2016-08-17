
package com.iceman.yangtze;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
/**
 * @author iceman 只是一个创建对话框的工具类
 */
public class Util {
    public static AlertDialog createAlertDialog(Activity activity, String title, String message,
            String positiveBtn, String negativeBtn,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener) {
        if (positiveBtn == null) {
            return new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setNegativeButton(negativeBtn, negativeListener).create();
        } else if (negativeBtn == null) {
            return new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setPositiveButton(positiveBtn, positiveListener).create();
        } else {
            return new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setPositiveButton(positiveBtn, positiveListener)
                    .setNegativeButton(negativeBtn, negativeListener).create();
        }
    }
}
