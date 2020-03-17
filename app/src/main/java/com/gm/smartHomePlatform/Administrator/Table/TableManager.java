package com.gm.smartHomePlatform.Administrator.Table;

import android.content.Context;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class TableManager extends TableRow {
    public TableManager(Context context) {
        super(context);
    }

    //private TableRow tableRow;
    private View createCellView(Object object){
        View view = null;
        String className = object.getClass().toString();
        switch (className){
            case "java.lang.String":
                TextView textView = new TextView(getContext());
                textView.setGravity(TEXT_ALIGNMENT_CENTER);
                textView.setText((String)object);
                view = textView;break;
            default:
                break;
        }
        return view;
    }

    private List<View> createRowView(Object[] objects){
        List<View> list = null;
        View temp = null;
        for (int i = 0;i<objects.length;i++){
            temp = this.createCellView(objects[i]);
            list.add(temp);
        }
        return list;
    }

    public void setTableRow(Object[] objects){
        List<View> list = this.createRowView(objects);
        for (int i=0;i<list.size();i++){
            this.addView(list.get(i),i);
        }
    }

    public TableRow getTableRow(){
        return this;
    }
}
