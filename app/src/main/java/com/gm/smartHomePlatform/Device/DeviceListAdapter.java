package com.gm.smartHomePlatform.Device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.smartHomePlatform.R;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BaseDevice> mDeviceList;
    public DeviceListAdapter(Context context,ArrayList<BaseDevice> list){
        mContext = context;
        mDeviceList = list;
    }
    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lay_device_item,null);
            viewHolder.text_name = convertView.findViewById(R.id.textName_item);
            viewHolder.text_value = convertView.findViewById(R.id.textValue_item);
            viewHolder.image_item = convertView.findViewById(R.id.imageView_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BaseDevice device = mDeviceList.get(position);
        viewHolder.image_item.setImageResource(R.drawable.launcher_icon);
        viewHolder.text_name.setText(device.getName());
        viewHolder.text_value.setText("测量属性测试");
        return convertView;
    }

    private final class ViewHolder{
        public TextView text_name;
        public TextView text_value;
        public ImageView image_item;
    }
}
