# simplepermission
public class MainActivity extends AppCompatActivity implements PermissionManager.IGrantPermissionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionManager.getInstance().GrantPermission(this, PermissionManager.PermissionType.OVERLAY,this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionManager.getInstance().onActivityForResult(this,requestCode,resultCode,data);
       
    }

    @Override
    public void OnGrantPermissionSuccess(PermissionManager.PermissionType permissionType) {
        Toast.makeText(this, "ádasd", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnGrantPermissionFail(PermissionManager.PermissionType permissionType, String errror) {

    }
    }
    Dont forget add Permission Manifest
