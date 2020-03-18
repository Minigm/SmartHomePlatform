package com.gm.smartHomePlatform.Administrator.Table;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
//表格行管理类
public class TableManager extends TableRow {
    //获取上线文环境
    public TableManager(Context context) {
        super(context);
    }
    //根据输入属性添加视图
    public View addElement(Object object){
        View view = null;
        String className = object.getClass().toString();
        //对输入类进行判断，并生成相应的控件
        switch (className){
            //如果是字符串，则生成一个TextView
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
            //将生成的控件数组存入列表中
            list.add(addElement(objects[i]));
        }
        return list;
    }
    //生成行视图
    public TableRow getTableRow(Object[] objects){
        List<View> list = new ArrayList<View>();
        list.addAll(this.addRowView(objects));
        for (int i = 0;i < list.size();i++){
            //将列表中的数据添加入行视图
            this.addView(list.get(i));
        }
        return this;
    }
}
