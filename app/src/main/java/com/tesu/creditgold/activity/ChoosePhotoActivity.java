package com.tesu.creditgold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.PhoteItemAdapter;
import com.tesu.creditgold.adapter.SelectPhoteItemAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.bean.PhotoUpImageBucket;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.UIUtils;

import java.util.ArrayList;
import java.util.List;


public class ChoosePhotoActivity extends BaseActivity {
	private TextView tv_top_back;
	private View rootView;

	private GridView phote_gv;
	private PhotoUpImageBucket photoUpImageBucket;
	private ArrayList<PhotoUpImageItem> selectImages;
	private PhoteItemAdapter adapter;
	private GridView select_phote_gv;
	private SelectPhoteItemAdapter mAdapter;
	private TextView start_make_tv;
	private Gson gson;
	/** 字幕文件 */
	private List<PhotoUpImageItem> imageList;
	private int size;

	@Override
	protected View initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rootView = View.inflate(this, R.layout.activity_choose_photo, null);
		setContentView(rootView);

		tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
		phote_gv = (GridView) rootView.findViewById(R.id.phote_gv);
		select_phote_gv = (GridView) rootView.findViewById(R.id.selected_phote_gv);
		start_make_tv = (TextView) rootView.findViewById(R.id.start_make_tv);

		initData();
		return null;
	}

	private void initData() {
		gson = new Gson();

		Intent intent = getIntent();
		String selectImagesStr = intent.getStringExtra("selectImagesStr");
		if(TextUtils.isEmpty(selectImagesStr)){
			selectImages = new ArrayList<PhotoUpImageItem>();
		}else {
			selectImages = gson.fromJson(selectImagesStr,new TypeToken<List<PhotoUpImageItem>>(){}.getType());
		}
		size = intent.getIntExtra("size",0);
		LogUtils.e("size2:"+size);
		mAdapter = new SelectPhoteItemAdapter(selectImages, ChoosePhotoActivity.this);
		select_phote_gv.setAdapter(mAdapter);

		photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
		imageList = photoUpImageBucket.getImageList();
		if(imageList != null){
			for(PhotoUpImageItem imageItem : imageList){
				if(selectImages.contains(imageItem)){
					imageItem.setIsSelected(true);
				}else {
					imageItem.setIsSelected(false);
				}
			}

		}else {
			imageList = new ArrayList<>();
		}
		adapter = new PhoteItemAdapter(imageList, ChoosePhotoActivity.this);
		adapter.setGridView(phote_gv);
		phote_gv.setAdapter(adapter);

		widgetListener();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			onKeyDownMethod(keyCode, event);
			Intent intent = new Intent(ChoosePhotoActivity.this,ChoosePhotoActivity.class);
			intent.putExtra("selectImages", gson.toJson(selectImages));
			setResult(100, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void widgetListener() {
		tv_top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("selectImages", gson.toJson(selectImages));
				setResult(100, intent);
				finish();
			}
		});

		phote_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				PhotoUpImageItem item = (PhotoUpImageItem) parent.getItemAtPosition(position);
				if(item.isSelected()){
					item.setIsSelected(false);
				}else {
					item.setIsSelected(true);
				}
				adapter.updateItemData(item);
//				CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
//				imageList.get(position).setIsSelected(
//						!checkBox.isChecked());
//				adapter.notifyDataSetChanged();

				if (imageList.get(position).isSelected()) {
					if (selectImages.contains(imageList.get(position))) {

					} else {
						selectImages.add(imageList.get(position));
					}
				} else {
					if (selectImages.contains(imageList.get(position))) {
						selectImages.remove(imageList.get(position));
					} else {

					}
				}

				mAdapter.notifyDataSetChanged();
			}
		});

		start_make_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectImages == null || selectImages.size() == 0) {
					Toast.makeText(ChoosePhotoActivity.this, "请先选择图片", Toast.LENGTH_LONG).show();
					return;
				}

				int remSize = 9;
				if(size != 0){
					remSize = 10-size;
				}
				if (selectImages.size() > remSize) {
					Toast.makeText(ChoosePhotoActivity.this, "最多还可选"+remSize+"张图片", Toast.LENGTH_LONG).show();
					return;
				}

				start_make_tv.setEnabled(false);
				start_make_tv.setBackgroundResource(R.mipmap.btn_sub_n);
				start_make_tv.setTextColor(UIUtils.getColor(R.color.text_color_gray));
				Intent intent = new Intent(ChoosePhotoActivity.this, ChoosePhotoFolderActivity.class);
				intent.putExtra("selectImages", gson.toJson(selectImages));
				setResult(200, intent);
				finish();
			}
		});
	}


	public void deletePhoto(int position){
		PhotoUpImageItem photoUpImageItem = selectImages.get(position);
		if(photoUpImageItem != null){
			selectImages.remove(photoUpImageItem);
			mAdapter.notifyDataSetChanged();

			if(imageList.contains(photoUpImageItem)){
				int pos = imageList.lastIndexOf(photoUpImageItem);
				imageList.get(pos).setIsSelected(false);
//				adapter.notifyDataSetChanged();
				adapter.updateItemData(photoUpImageItem);
			}
		}
	}

}
