package com.gm.smartHomePlatform.Administrator.Table;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TableManager extends TableRow {
    //获取上线文环境
    public TableManager(Context context) {
        super(context);
    }
    //根据输入属性添加视图
    public View addElement(Object object){
        View view = null;
        String className = object.getClass().toString();
        switch (className){
            case "class java.lang.String":
                TextView textView = new TextView(getContext());
                textView.setText((String)object);
                textView.setGravity(Gravity.CENTER);
                view = textView;
                break;
        }
        return view;
    }
    //对数据组进行添加
    public List<View> addRowView(Object[] objects){
        List<View> list = new ArrayList<View>();
        for (int i = 0;i < objects.length;i++){
            list.add(addElement(objects[i]));
        }
        return list;
    }
    //生成行视图
    public TableRow getTableRow(Object[] objects){
        List<View> list = new ArrayList<View>();
        list.addAll(this.addRowView(objects));
        for (int i = 0;i < list.size();i++){
            this.addView(list.get(i));
        }
        return this;
    }
}
