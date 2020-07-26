package com.nhatran241.simplepermission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

public class PermissionDialog {
    public String defaulTitle ="Permission necessary";
    private static PermissionDialog instance;
    private AlertDialog alertDialog;

    public static PermissionDialog getInstance() {
        if(instance ==null)
            instance = new PermissionDialog();
        return instance;
    }
    public void getAlertDialog(Context context, String title, @NonNull String message,boolean cancleAble , final IPermissionDialogListener iPermissionDialogListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        if(title!=null)
            defaulTitle = title;
        alertBuilder.setTitle(defaulTitle);
        alertBuilder.setMessage(message);
        alertBuilder.setCancelable(cancleAble);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                iPermissionDialogListener.onPermissionDialogOkClick();
            }
            });
        alertBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                iPermissionDialogListener.onPermissionDialogCancleClick();
            }
        });
         alertDialog = alertBuilder.create();
         alertDialog.show();
    }

    public interface IPermissionDialogListener{
        void onPermissionDialogOkClick();
        void onPermissionDialogCancleClick();
    }
}
