package com.gm.smartHomePlatform.Device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gmcompany.devices.GMCompanyDeviceHelper;
import com.gm.basedevice.BaseDevice;
import com.gm.smartHomePlatform.R;

import java.util.ArrayList;
import java.util.Map;

public class DeviceListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BaseDevice> mDeviceList = new ArrayList<BaseDevice>();
    public DeviceListAdapter(Context context, ArrayList<BaseDevice> list){
        mContext = context;
        mDeviceList.clear();
        mDeviceList.addAll(list);
    }
    @Override
    public int getCount() {
        if (mDeviceList != null){
            return mDeviceList.size();
        }else {
            return 0;
        }
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
        BaseDevice device = mDeviceList.get(position);
        switch (device.getCompany()){
            case "GMCompany":
                GMCompanyDeviceHelper gmCompanyDeviceHelper = new GMCompanyDeviceHelper();
                if (gmCompanyDeviceHelper.isSetAdapter()){
                    convertView = gmCompanyDeviceHelper.getConvertView(device,mContext);
                    return convertView;
                }
        }
        BaseViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new BaseViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lay_device_item,null);
            viewHolder.text_name = convertView.findViewById(R.id.textName_item);
            viewHolder.text_value = convertView.findViewById(R.id.textValue_item);
            viewHolder.image_item = convertView.findViewById(R.id.imageView_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (BaseViewHolder) convertView.getTag();
        }

        viewHolder.image_item.setImageResource(R.drawable.launcher_icon);
        viewHolder.text_name.setText(device.getName());
        viewHolder.text_value.setText("企业未设置");
        return convertView;
    }

    private final class BaseViewHolder{
        public TextView text_name;
        public TextView text_value;
        public ImageView image_item;
    }
}
