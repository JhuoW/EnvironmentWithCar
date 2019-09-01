package com.ewc.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ewc.bean.PostModel;
import com.example.environmentwithcar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HealthNewsAdapter extends BaseListAdapter<PostModel> {

	private DisplayImageOptions displayImageOptions;

	public HealthNewsAdapter(Context ctx, List<PostModel> datas, int layoutId) {
		super(ctx, datas, layoutId);
		displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.image_error)
				.showImageOnFail(R.drawable.image_error).build();
	}
	
	@Override
	public void conver(ViewHolder holder, int position, PostModel t) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tvTitle, t.getTitle());
		holder.setText(R.id.tvExcept, t.getContent());
		holder.setText(R.id.tv_collect, t.getCollection());
		holder.setText(R.id.tvView, t.getView_count());
		holder.setText(R.id.tvTime, t.getTime());
		ImageView image = holder.getView(R.id.ivRight);
		String thumbnail = t.getImageurl();
		if (thumbnail.length() > 0) {
			image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(thumbnail, image,
					displayImageOptions);
		} else {
			image.setVisibility(View.GONE);
		}
	}

}
