package com.tofirst.study.hbkdassistant.acitvity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.domain.Person;
import com.tofirst.study.hbkdassistant.global.Constant;
import com.tofirst.study.hbkdassistant.utils.common.IOUtils;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.utils.common.UIUtils;
import com.tofirst.study.hbkdassistant.view.PullToZoomListView;
import com.tofirst.study.hbkdassistant.view.SlideSwitch;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {


    private TextView tv_user_name;
    private TextView tv_user_grade;
    private TextView tv_user_level;
    private TextView tv_user_description;
    private TextView tv_user_email;
    private TextView tv_user_pnumber;
    private SlideSwitch tb_user_email_bd;
    private SlideSwitch tb_user_pnumber_bd;
    PullToZoomListView listView;
    private String[] adapterData;
    private View footview;
    private boolean isLight;
    private Toolbar toolbar;
    private CircleImageView user_icon;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setTitle("个人信息");
        initView();
        initData();
    }

    private void initToolBar() {
        isLight = SharePreUtils.getsPreBoolean(this, "isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        initToolBar();
        listView = (PullToZoomListView) findViewById(R.id.listview);
        listView.setDividerHeight(0);//去掉分割线
        listView.setClickable(false);
        adapterData = new String[]{""};
        listView.setAdapter(new ArrayAdapter<String>(UserInfoActivity.this,
                android.R.layout.simple_list_item_1, adapterData));
        //头部布局
        listView.getHeaderView().setImageResource(R.mipmap.pulldown);
        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        //底部布局
        footview = View.inflate(UserInfoActivity.this, R.layout.activity_user_footview, null);
        listView.addFooterView(footview);
        tv_user_name = (TextView) footview.findViewById(R.id.tv_user_name);
        user_icon = (CircleImageView) footview.findViewById(R.id.user_icon);
        tv_user_grade = (TextView) footview.findViewById(R.id.tv_user_grade);
        tv_user_level = (TextView) footview.findViewById(R.id.tv_user_level);
        tv_user_description = (TextView) footview.findViewById(R.id.tv_user_description);
        tv_user_email = (TextView) footview.findViewById(R.id.tv_user_email);
        tv_user_pnumber = (TextView) footview.findViewById(R.id.tv_user_pnumber);
        tb_user_email_bd = (SlideSwitch) footview.findViewById(R.id.tb_user_email_bd);
        tb_user_pnumber_bd = (SlideSwitch) footview.findViewById(R.id.tb_user_pnumber_bd);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UserInfoActivity.this, UserPicCutActivity.class), 1);
            }
        });

        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    private void initData() {
        //从服务器拿到数据
        getDataFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // 将布局挂载到menu上
        inflater.inflate(R.menu.update_userinfo, menu);
        //改变日间和夜间的模式
        menu.getItem(0).setTitle(SharePreUtils.getsPreBoolean(UserInfoActivity.this, "isLight", true) ? "夜间模式" : "日间模式");
        return true;//已经处理好了
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.update_userinfo) {
            startActivity(new Intent(UserInfoActivity.this, UpdateUserInfoActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == UserPicCutActivity.HASHCHANGE) {
            String path = data.getStringExtra("photo");
            int count = data.getIntExtra("default", 0);
            if (path != null) {
                Bitmap bitmap = getBitmap(path);
                user_icon.setImageBitmap(bitmap);
            } else {
                user_icon.setImageResource(Constant.BITMAPS[count]);
            }
        }
    }

    /**
     * 从服务器获得信息
     */
    private void getDataFromServer() {
        final Person user = BmobUser.getCurrentUser(this, Person.class);
        //加载图片
        BmobQuery<Person> query = new BmobQuery<Person>();
        query.findObjects(this, new FindListener<Person>() {

            @Override
            public void onSuccess(List<Person> persons) {
                // TODO Auto-generated method stub
                if (persons.size() > 0) {
                    for (Person person : persons) {
                        if (person.getUsername().equals(user.getUsername())) {
                            person.getPic().loadImageThumbnail(UserInfoActivity.this, user_icon, 100, 100);
                            UIUtils.showToastSafe("图片下载成功");
                        }

                    }
                }

//                if(persons.size()>0){
//                    // 如果查询结果大于0，取第一条数据的icon缩略图进行显示
//                    persons.get(0).getPic().loadImageThumbnail(UserInfoActivity.this, user_icon, 100, 100);
//                    UIUtils.showToastSafe("图片下载成功" );
//                    mImageloader.displayImage(persons.get(0).getPic().getFileUrl(UserInfoActivity.this), user_icon, options);
//                }

            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                UIUtils.showToastSafe("查询数据失败：" + arg1);
            }
        });
        if (user != null) {
            //判断是否为空
            if (user.getGrade() != null) {
                tv_user_grade.setText(user.getGrade() + "");
            }
            //判断是否为空
            if (user.getLevel() != null) {
                Integer level = user.getLevel();
                if (level > 100) {
                    tv_user_level.setText("青铜圣斗士");
                } else {
                    tv_user_level.setText("黄金圣斗士");
                }

            }
            //判断是否为空
            if (user.getDescription() != null) {
                tv_user_description.setText(user.getDescription());
            }
            //判断是否为空
            if (user.getUsername() != null) {
                tv_user_name.setText(user.getUsername());
            }
            //判断是否为空
            if (user.getEmail() != null) {
                tv_user_email.setText(user.getEmail());
            }
            //判断是否为空
            if (user.getMobilePhoneNumber() != null) {
                tv_user_pnumber.setText(user.getMobilePhoneNumber());
            }
            //设置不能滑动
            tb_user_pnumber_bd.setSlideable(false);
            tb_user_email_bd.setSlideable(false);
            //判断是否绑定手机
            if (null != user.getMobilePhoneNumberVerified()) {
                tb_user_pnumber_bd.setState(user.getMobilePhoneNumberVerified());
            }

            //判断是否绑定邮箱
            if (null != user.getEmailVerified()) {
                tb_user_email_bd.setState(user.getEmailVerified());
            }
        }
    }

    //保存图片并返回路径
    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        File file = new File(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(in);
        }
        return bitmap;
    }


}
