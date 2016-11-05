package com.ydc.laundromat.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydc.laundromat.R;
import com.ydc.laundromat.dto.ItemPoint;

import java.util.List;

/**
 * ListView适配器
 * @author w.w
 */
public class ListViewAdapter extends ArrayAdapter<ItemPoint> {
	
	private LayoutInflater inflater;
	private int resourceId;
	private Context context;
	public ListViewAdapter(Context context, int resourceId,List<ItemPoint> list) {
		super(context, 0, list);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.resourceId = resourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		ItemPoint info = getItem(position);
		if (convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.pointNameText = (TextView) convertView.findViewById(R.id.point_name);
			viewHolder.emptyNumText = (TextView) convertView.findViewById(R.id.point_status);
			viewHolder.distanceText = (TextView) convertView.findViewById(R.id.point_distance);
			viewHolder.status_ll = (LinearLayout) convertView.findViewById(R.id.point_status_ll);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.icon_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.pointNameText.setText(info.getPointName()+ " " +info.getAddress());
		viewHolder.emptyNumText.setText(info.getEmptyNum()+"台空闲");

		ColorDrawable drawable = new ColorDrawable(ContextCompat.getColor(context,R.color.red_1));
		if(info.getEmptyNum() == 0){
			viewHolder.status_ll.setBackground(drawable);
			viewHolder.imageView.setImageResource(R.mipmap.icon_machine_red);
		}

		viewHolder.distanceText.setText(info.getDistance()+"米");

		return convertView;
	}
	public static class ViewHolder {
		ImageView imageView;
		TextView pointNameText;
		TextView emptyNumText;
		TextView distanceText;
		LinearLayout status_ll;

	}
}
