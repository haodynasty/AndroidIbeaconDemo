package com.blakequ.androidibeacondemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) BlakeQu All Rights Reserved <blakequ@gmail.com>
 * <p/>
 * Licensed under the blakequ.com License, Version 1.0 (the "License");
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * author  : quhao <blakequ@gmail.com> <br>
 * date     : 2016/6/1 15:31 <br>
 * last modify author : <br>
 * version : 1.0 <br>
 * description:
 */
public class BeaconAdapter extends BaseAdapter{
    private List<Beacon> beaconList;
    private Context context;

    public BeaconAdapter(Context context){
        this.context =context;
        beaconList = new ArrayList<Beacon>();
    }

    public void add(Beacon data) {
        if(data != null) {
            if (!checkIsHasDevice(data)){
                beaconList.add(data);
            }
        }
        this.notifyDataSetChanged();
    }

    public void clear(){
        beaconList.clear();
        notifyDataSetChanged();
    }

    public boolean checkIsHasDevice(Beacon data){
        int count = getCount();
        if (count > 0){
            for (int i=0; i<count; i++){
                Beacon device = (Beacon)getItem(i);
                if (device.getBluetoothAddress().equals(data.getBluetoothAddress())){
                    beaconList.remove(i);
                    beaconList.add(i, data);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return beaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView != null) {
            holder = (Holder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_device, null);
            holder = new Holder();
            holder.mTvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            convertView.setTag(holder);
        }
        Beacon beacon = (Beacon) getItem(position);
        holder.mTvTitle.setText(beacon.getBluetoothName()+" "+beacon.getBluetoothAddress()+" "+formatDouble1(beacon.getDistance())+"m");
        return convertView;
    }

    static class Holder{
        private TextView mTvTitle;
    }

    /**
     * 四舍五入获取double
     * @param value
     * @return
     */
    public static double formatDouble1(double value) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(value).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }
}
