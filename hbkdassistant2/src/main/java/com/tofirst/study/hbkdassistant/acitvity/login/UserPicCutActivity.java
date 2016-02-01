package com.tofirst.study.hbkdassistant.acitvity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.domain.Person;
import com.tofirst.study.hbkdassistant.utils.common.FileUtils;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.StringUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserPicCutActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int HASHCHANGE = 0;
    private boolean isLight;
    private Toolbar toolbar;
    private LinearLayout ll_user_pic_camera;
    private LinearLayout ll_user_pic_native;
    private GridView gv_set_user_default;
    private int[] imageIDs = {R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default, R.mipmap.pic_default};
    private static final int PHOTO_REQUEST_GALLERY = 1;
    private static final int PHOTO_REQUEST_CAREMA = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private Bitmap bitmap;
    private BmobFile bmobFile;
    private ImageView iv_image;
    private String fileUrl;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pic_cut);
        initData();
        initView();
    }

    private void initData() {
        if (tempFile == null && FileUtils.isSDCardAvailable()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
        }
    }

    private void initView() {
        setTitle("设置头像");
        initToolBar();
        ll_user_pic_camera = (LinearLayout) findViewById(R.id.ll_user_pic_camera);
        ll_user_pic_native = (LinearLayout) findViewById(R.id.ll_user_pic_native);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        ll_user_pic_camera.setOnClickListener(this);
        ll_user_pic_native.setOnClickListener(this);
        gv_set_user_default = (GridView) findViewById(R.id.gv_set_user_default);
        UserDefualtAdapter adapter = new UserDefualtAdapter();
        gv_set_user_default.setAdapter(adapter);
        gv_set_user_default.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iv_image.setImageResource(imageIDs[position]);
            }
        });
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        isLight = SharePreUtils.getsPreBoolean(this, "isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_pic_camera:
                // 激活相机
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                // 判断存储卡是否可以用，可用进行存储
                // 从文件中创建uri
                Uri uri = Uri.fromFile(tempFile);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                startActivityForResult(intent1, PHOTO_REQUEST_CAREMA);
                break;
            case R.id.ll_user_pic_native:
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                break;
            default:

                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // 将布局挂载到menu上
        inflater.inflate(R.menu.change_userpic, menu);
        //改变日间和夜间的模式
        menu.getItem(0).setTitle(SharePreUtils.getsPreBoolean(UserPicCutActivity.this, "isLight", true) ? "夜间模式" : "日间模式");
        return true;//已经处理好了
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_userpic) {
            if (null != bitmap) {
                final String path = saveBitmap(bitmap);
                if (tempFile != null && !StringUtils.isEmpty(path)) {

                    bmobFile = new BmobFile(new File(path));
                    bmobFile.uploadblock(UserPicCutActivity.this, new UploadFileListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
                            fileUrl = bmobFile.getFileUrl(UserPicCutActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("photo", path);
                            setResult(HASHCHANGE, intent);
                            //                        Person person = new Person();
                            //                        person.setPic(bmobFile);
                            //                        person.save(UserPicCutActivity.this);
                            Person person = new Person();
                            person.setPic(bmobFile);
                            BmobUser bmobUser = BmobUser.getCurrentUser(UserPicCutActivity.this);
                            person.update(UserPicCutActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    // TODO Auto-generated method stub
                                    UIUtils.showToastSafe("更新成功");
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    // TODO Auto-generated method stub
                                    UIUtils.showToastSafe("更新用户信息失败:" + msg);
                                }
                            });
                            UIUtils.showToastSafe("上传文件成功:" + bmobFile.getFileUrl(UserPicCutActivity.this));
                            finish();
                        }

                        @Override
                        public void onProgress(Integer value) {
                            // TODO Auto-generated method stub
                        }


                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            UIUtils.showToastSafe("上传文件失败：" + msg);
                        }
                    });
                }
            } else {
                UIUtils.showToastSafe("选择默认图片");
                Intent intent = new Intent();
                count=0;
                intent.putExtra("default", count);
                setResult(HASHCHANGE, intent);
                finish();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    class UserDefualtAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageIDs.length;
        }

        @Override
        public Object getItem(int position) {
            return imageIDs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = new ImageView(UserPicCutActivity.this);
            iv.setImageResource(imageIDs[position]);
            return iv;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (FileUtils.isSDCardAvailable()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(UserPicCutActivity.this, "存储卡不可用，无法存储照片！", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
                this.iv_image.setImageBitmap(bitmap);//测试
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 剪切图片的方法
     *
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("return-data", true);//是否把剪切后的图片通过data返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//图片的输出格式
        intent.putExtra("noFaceDetection", true);  //关闭面部识别
        //设置剪切的图片保存位置
//        Uri cropUri = Uri.fromFile(new File(
//                Environment.getExternalStorageDirectory().getPath() + PHOTO_FILE_NAME));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,cropUri);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //保存图片并返回路径
    public String saveBitmap(Bitmap bitmap) {
        File f = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
        String path = f.getPath();
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return path;
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
