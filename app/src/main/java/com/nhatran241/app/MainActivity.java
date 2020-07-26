package com.nhatran241.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nhatran241.simplepermission.PermissionDialog;
import com.nhatran241.simplepermission.PermissionManager;

public class MainActivity extends AppCompatActivity implements PermissionManager.IGrantPermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("nhatnhat", "onActivityResult: 1"+resultCode+"/"+requestCode);
        PermissionManager.getInstance().onActivityForResult(this,requestCode,resultCode,data);
    }

    public void OnGrantPermissionSuccess(PermissionManager.PermissionType permissionType) {
        switch (permissionType){
            case WRITE_STORAGE:{
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @Override
    public void OnGrantPermissionFail(final PermissionManager.PermissionType permissionType, String errror) {
        Toast.makeText(this, ""+errror, Toast.LENGTH_SHORT).show();
        if(errror==PermissionManager.deny){
            PermissionDialog.getInstance().getAlertDialog(MainActivity.this, null, "This permission is need ", false,new PermissionDialog.IPermissionDialogListener() {
                @Override
                public void onPermissionDialogOkClick() {
                    if(permissionType == PermissionManager.PermissionType.READ_STORAGE) {
                        PermissionManager.getInstance().GrantPermission(MainActivity.this, PermissionManager.PermissionType.READ_STORAGE, MainActivity.this);
                    }else  if(permissionType == PermissionManager.PermissionType.WRITE_STORAGE){
                        PermissionManager.getInstance().GrantPermission(MainActivity.this, PermissionManager.PermissionType.WRITE_STORAGE, MainActivity.this);
                    }
                 }

                @Override
                public void onPermissionDialogCancleClick() {

                }
            });
        }else if(errror == PermissionManager.deny_dontaskagain){
            PermissionDialog.getInstance().getAlertDialog(MainActivity.this, null, "This permission is need you need to turn it on in Setting",false, new PermissionDialog.IPermissionDialogListener() {
                @Override
                public void onPermissionDialogOkClick() {
                    if(permissionType == PermissionManager.PermissionType.READ_STORAGE) {
                        PermissionManager.getInstance().goToSetting(MainActivity.this, PermissionManager.PermissionType.READ_STORAGE);
                    }else  if(permissionType == PermissionManager.PermissionType.WRITE_STORAGE){
                        PermissionManager.getInstance().goToSetting(MainActivity.this, PermissionManager.PermissionType.WRITE_STORAGE);
                    }
                 }

                @Override
                public void onPermissionDialogCancleClick() {

                }
            });
        }
    }

    public void WRITE(View view) {
        PermissionManager.getInstance().GrantPermission(this, PermissionManager.PermissionType.WRITE_STORAGE, this);
    }

    public void READ(View view) {
        PermissionManager.getInstance().GrantPermission(this, PermissionManager.PermissionType.READ_STORAGE, this);

    }
}
