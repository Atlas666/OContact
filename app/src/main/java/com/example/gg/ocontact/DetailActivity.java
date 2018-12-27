package com.example.gg.ocontact;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import org.litepal.LitePal;
import java.util.List;

import static android.view.View.INVISIBLE;

/*
 * To-do:12.27
 * Done
 * */
public class DetailActivity extends AppCompatActivity {

    int personId;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //setTitle("");
        Intent intent=getIntent();
        name=intent.getStringExtra("id");
        Log.d("DetailActivity", name);

        initInfo(name);//初始化详细信息

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                Intent intent=new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra("id",String.valueOf(personId));
                Log.d("DetailActivity_id", String.valueOf(personId));
                startActivityForResult(intent, 1);
                break;
            case R.id.delete:
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("是否删除此联系人？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LitePal.delete(ContactDatabase.class,personId);
                                Toast.makeText(DetailActivity.this,"已删除", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }).setNegativeButton("取消",null).create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                break;
            case R.id.share:
                List<ContactDatabase> list;
                list = LitePal.where("id = ?", String.valueOf(personId)).find(ContactDatabase.class);
                TextView person_name=(TextView) findViewById(R.id.detail_name);
                String share="姓名:"+person_name.getText();
                TextView temp_content;
                for (ContactDatabase one: list) {
                    temp_content=(TextView) findViewById(R.id.detail_work_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\n工作:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_number_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\n手机:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_mail_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\n电子邮件:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_remark_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\n备注:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_address_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\n住址:"+temp_content.getText();
                    }

                    temp_content=(TextView) findViewById(R.id.detail_birthday_content);
                    if (!TextUtils.isEmpty(temp_content.getText())) {
                        share+="\n生日:"+temp_content.getText();
                    }
                }
                Intent sendIntent = new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, share).setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"分享给......"));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInfo(String name) {
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView photo=(ImageView) findViewById(R.id.detail_image_view);//联系人头像
        //collapsingToolbar.setTitle("姓名");
        //
        String photoPath=null;

        List<ContactDatabase> list;
        list = LitePal.where("id = ?", name).find(ContactDatabase.class);

        TextView person_name=(TextView) findViewById(R.id.detail_name);
        TextView temp;
        TextView temp_content;
        for (ContactDatabase one: list) {
            person_name.setText(one.getName());
            personId=one.getId();
            photoPath=one.getPhotoPath();

            temp_content=(TextView) findViewById(R.id.detail_work_content);

            temp_content.setText(one.getJob());
            temp=(TextView) findViewById(R.id.detail_work);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_number_content);
            temp_content.setText(one.getPhoneNumber());
            temp=(TextView) findViewById(R.id.detail_number);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_mail_content);
            temp_content.setText(one.getEmail());
            temp=(TextView) findViewById(R.id.detail_mail);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_address_content);
            temp_content.setText(one.getAddress());
            temp=(TextView) findViewById(R.id.detail_address);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_remark_content);
            temp_content.setText(one.getComment());
            temp=(TextView) findViewById(R.id.detail_remark);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }

            temp_content=(TextView) findViewById(R.id.detail_birthday_content);
            temp_content.setText(one.getBirthday());
            temp=(TextView) findViewById(R.id.detail_birthday);
            if (TextUtils.isEmpty(temp_content.getText())) {
                temp.setVisibility(View.GONE);
                temp_content.setVisibility(View.GONE);
            }
            else {
                temp.setVisibility(View.VISIBLE);
                temp_content.setVisibility(View.VISIBLE);
            }
        }

        FloatingActionButton camera=findViewById(R.id.camera);
        if(!TextUtils.isEmpty(photoPath)) {
            camera.setVisibility(INVISIBLE);
            Glide.with(this).load(photoPath).into(photo); //头像载入工具
        }
        else {
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2=new Intent(DetailActivity.this, EditActivity.class);
                    intent2.putExtra("id",String.valueOf(personId));
                    startActivityForResult(intent2, 1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        Toast.makeText(this,"更新", Toast.LENGTH_SHORT).show();
        initInfo(name);
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
