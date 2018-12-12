package com.tesu.creditgold.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.PhoteFolderAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.bean.PhotoUpImageBucket;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.PhotoUpAlbumHelper;

import java.util.List;


public class ChoosePhotoFolderActivity extends BaseActivity {
	private TextView tv_top_back;
	private View rootView;

	private ListView image_folder_lv;
	private PhotoUpAlbumHelper photoUpAlbumHelper;
	private List<PhotoUpImageBucket> list;
	private PhoteFolderAdapter mAdapter;
	private String selectImagesStr;
	private int size;


	@Override
	protected View initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rootView = View.inflate(this, R.layout.activity_choose_photo_folder, null);
		setContentView(rootView);

		tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
		image_folder_lv = (ListView) rootView.findViewById(R.id.image_folder_lv);

		initData();

		return null;
	}

	private void initData() {
		Intent intent = getIntent();
		size = intent.getIntExtra("size",0);
		LogUtils.e("size1:" + size);
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();
		photoUpAlbumHelper.init(ChoosePhotoFolderActivity.this);
		photoUpAlbumHelper.setGetAlbumList(new PhotoUpAlbumHelper.GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoUpImageBucket> list) {
				mAdapter.setArrayList(list);
				mAdapter.notifyDataSetChanged();
				ChoosePhotoFolderActivity.this.list = list;
			}
		});
		photoUpAlbumHelper.execute(false);

		mAdapter = new PhoteFolderAdapter(ChoosePhotoFolderActivity.this);
		image_folder_lv.setAdapter(mAdapter);

		tv_top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		image_folder_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intent = new Intent(ChoosePhotoFolderActivity.this, ChoosePhotoActivity.class);
				intent.putExtra("imagelist", list.get(position));
				if (!TextUtils.isEmpty(selectImagesStr)) {
					intent.putExtra("selectImagesStr", selectImagesStr);
				}
				intent.putExtra("size",size);
				startActivityForResult(intent, 200);
				setFinish();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==200){
			if(resultCode == 100){
				selectImagesStr = data.getStringExtra("selectImages");
			}else if(resultCode == 200){
				selectImagesStr = data.getStringExtra("selectImages");
				Intent intent = getIntent();
				intent.putExtra("selectImages", selectImagesStr);
				setResult(200, intent);
				finish();
			}
		}
	}

}
