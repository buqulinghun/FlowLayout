package com.example.flowlayout.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.widget.TextView;

import com.example.flowlayout.R;
import com.example.flowlayout.layout.FL;
import com.example.flowlayout.layout.FlowLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] appName = new String[]{"QQ", "视频", "放开那三国", "电子书", "酒店", "单机", "小说", "斗地主", "优酷", "网游", "WIFI万能钥匙", "播放器", "捕鱼达人2", "机票", "游戏", "熊出没之熊大快跑", "美图秀秀", "浏览器", "单机游戏", "我的世界", "电影电视", "QQ空间", "旅游", "免费游戏", "2048", "刀塔传奇", "壁纸", "节奏大师", "锁屏", "装机必备", "天天动听", "备份", "网盘", "海淘网", "大众点评", "爱奇艺视频", "腾讯手机管家", "百度地图", "猎豹清理大师", "谷歌地图", "hao123上网导航", "京东", "youni有你", "万年历-农历黄历", "支付宝钱包"};
        //     String [] appName = new String[]{"QQ","视频"};
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.fl);
        FL fl2 = (FL) findViewById(R.id.fl2);


        for (int i = 0; i < appName.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(appName[i]);


            Random random = new Random();

            int alpha = random.nextInt(255);
            int red = random.nextInt(255);
            int green = random.nextInt(255);
            int blue = random.nextInt(255);

            int argb = Color.argb(255, red, green, blue);

            textView.setTextColor(Color.BLACK);
            textView.setBackground(new ColorDrawable(argb));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            flowLayout.addView(textView);
        }

        for (int i = 0; i < appName.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(appName[i]);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(30);
            fl2.addView(textView);
        }




    }
}