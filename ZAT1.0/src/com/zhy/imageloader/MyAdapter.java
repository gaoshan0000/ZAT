package com.zhy.imageloader;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.amap.location.demo.R;
import com.zhy.utils.CommonAdapter;

public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * �û�ѡ���ͼƬ���洢ΪͼƬ������·��
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * �ļ���·��
	 */
	private String mDirPath;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
	}

	@Override
	public void convert(final com.zhy.utils.ViewHolder helper, final String item)
	{
		//����no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		//����no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);
		//����ͼƬ
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		//����ImageView�ĵ���¼�
		mImageView.setOnClickListener(new OnClickListener()
		{
			//ѡ����ͼƬ�䰵����֮��֮
			@Override
			public void onClick(View v)
			{

				// �Ѿ�ѡ�����ͼƬ
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// δѡ���ͼƬ
				{
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}

			}
		});
		
		/**
		 * �Ѿ�ѡ�����ͼƬ����ʾ��ѡ�����Ч��
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
}