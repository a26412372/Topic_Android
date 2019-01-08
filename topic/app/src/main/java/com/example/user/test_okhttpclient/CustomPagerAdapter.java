package com.example.user.test_okhttpclient;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter{

    List<PageView> pageList;

    public CustomPagerAdapter(List<PageView> pageList){
        this.pageList = pageList;
    }

    @Override
    public int getCount(){
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view,Object o){
        return o == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){
        container.addView(pageList.get(position));
        return pageList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((View) object);
    }
}

