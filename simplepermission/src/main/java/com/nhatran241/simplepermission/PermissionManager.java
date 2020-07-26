package com.nhatran241.simplepermission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class PermissionManager {
    private static final int REQUEST_OVERLAY = 1111;
    private static final int REQUEST_WRTIE_STORAGE = 1112;
    private static final int REQUEST_READ_STORAGE = 1113;
    private static final int REQUEST_PERMISSION_SETTING = 1000;
    private String OVERLAYFAIL ="grant overlay permission failed";
    public static String deny="deny";
    public static String deny_dontaskagain="deny dont ask again";
    private static PermissionManager instance;
    private IGrantPermissionListener iGrantPermissionListener ;
    public static PermissionManager getInstance() {
        if(instance == null)
            instance = new PermissionManager();
        return instance;
    }

    public enum PermissionType{
        OVERLAY,
        WRITE_STORAGE,
        READ_STORAGE
    }
    public void goToSetting(Activity activity,PermissionType permissionType){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        switch (permissionType){
            case WRITE_STORAGE:{
                activity.startActivityForResult(intent, REQUEST_WRTIE_STORAGE);
                break;
            }
            case READ_STORAGE:{
                activity.startActivityForResult(intent, REQUEST_READ_STORAGE);
                break;
            }
        }
    }
    public void onActivityForResult(Activity activity,int req,int res,Intent data){

        Log.d("nhatnhat", "onActivityResult: 1"+res+"/"+req);
        if(iGrantPermissionListener==null){
            return;
        }
        if(req==REQUEST_OVERLAY){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    iGrantPermissionListener.OnGrantPermissionFail(PermissionType.OVERLAY,OVERLAYFAIL);
                } else iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.OVERLAY);
            }else iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.OVERLAY);
        }else if(req== REQUEST_WRTIE_STORAGE){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.WRITE_STORAGE);
            }else {
                iGrantPermissionListener.OnGrantPermissionFail(PermissionType.WRITE_STORAGE,deny_dontaskagain);
            }
        }else if(req== REQUEST_READ_STORAGE){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.READ_STORAGE);
            }else {
                iGrantPermissionListener.OnGrantPermissionFail(PermissionType.READ_STORAGE,deny_dontaskagain);
            }
        }
    }
    public void onRequestPermissionsResult(Activity activity, int req, String[] permissions, int[] grantResults){
        if(iGrantPermissionListener==null)
            return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = false;
                    showRationale = activity.shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        if(req == REQUEST_WRTIE_STORAGE){
                            iGrantPermissionListener.OnGrantPermissionFail(PermissionType.WRITE_STORAGE,deny_dontaskagain);
                        }else if(req ==REQUEST_READ_STORAGE){
                            iGrantPermissionListener.OnGrantPermissionFail(PermissionType.READ_STORAGE,deny_dontaskagain);
                        }
                    } else if (req == REQUEST_WRTIE_STORAGE && Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        iGrantPermissionListener.OnGrantPermissionFail(PermissionType.WRITE_STORAGE,deny);
                    }else if(req== REQUEST_READ_STORAGE && Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)){
                        iGrantPermissionListener.OnGrantPermissionFail(PermissionType.READ_STORAGE,deny);
                    }
                    // user did NOT check "never ask again"
                    // this is a good place to explain the user
                    // why you need the permission and ask if he wants
                    // to accept it (the rationale)
                }
            }

            }else {
                if(req==REQUEST_WRTIE_STORAGE) {
                    iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.WRITE_STORAGE);
                }
            }
        }



    public void GrantPermission(Activity activity, PermissionType permissionType, IGrantPermissionListener iGrantPermissionListener){
        this.iGrantPermissionListener = iGrantPermissionListener;
        switch (permissionType){
            case OVERLAY:{
                /**
                 *  OVERLAY
                 *     <uses-permission android:name="android.permission.ACTION_MANAGE_OVERLAY_PERMISSION" />
                 *     <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
                 *
                 *              TextView textView = new TextView(MainActivity.this);
                 *                 textView.setText("DMM");
                 *                 int LAYOUT_FLAG;
                 *                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 *                     LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                 *                 } else {
                 *                     LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
                 *                 }
                 *                 WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                 *                         WindowManager.LayoutParams.WRAP_CONTENT,
                 *                         WindowManager.LayoutParams.WRAP_CONTENT,
                 *                         LAYOUT_FLAG,
                 *                         WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                 *                         PixelFormat.TRANSLUCENT);
                 *                 params.gravity = Gravity.TOP | Gravity.LEFT;
                 *                 params.x = 0;
                 *                 params.y = 100;
                 *                 getWindowManager().addView(textView,params);
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(activity)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivityForResult(intent, REQUEST_OVERLAY);
                    }else {
                        this.iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.OVERLAY);
                    }
                }else {
                    this.iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.OVERLAY);
                }
                break;
            }
            case WRITE_STORAGE:{
                /**
                 *  WRITE
                 *
                 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.WRITE_STORAGE);
                    }else {
                        activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRTIE_STORAGE);
                    }
                 }else {
                    iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.WRITE_STORAGE);
                }
                break;
            }
            case READ_STORAGE:{
                /**
                 *  READ
                 *
                 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.READ_STORAGE);
                        }else {
                            activity.requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
                        }
                }else {
                    iGrantPermissionListener.OnGrantPermissionSuccess(PermissionType.READ_STORAGE);
                }
                break;
            }

        }

    }
    public interface IGrantPermissionListener{
        void OnGrantPermissionSuccess(PermissionType permissionType);
        void OnGrantPermissionFail(PermissionType permissionType, String errror);
    }
}
