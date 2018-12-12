package com.tesu.creditgold.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tesu.creditgold.R;
import com.tesu.creditgold.adapter.PictureAdapter;
import com.tesu.creditgold.adapter.ShowPictureAdapter;
import com.tesu.creditgold.base.BaseActivity;
import com.tesu.creditgold.base.MyVolley;
import com.tesu.creditgold.bean.FkBaseResponse;
import com.tesu.creditgold.bean.PhotoUpImageItem;
import com.tesu.creditgold.bean.PictureItemBean;
import com.tesu.creditgold.bean.QuotaOrderBean;
import com.tesu.creditgold.bean.UsrOrderPicBean;
import com.tesu.creditgold.protocol.GetUsrInfProtocol;
import com.tesu.creditgold.request.GetUsrInfRequest;
import com.tesu.creditgold.response.GetQuotaOrderInfoResponse;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.support.PercentRelativeLayout;
import com.tesu.creditgold.util.BitmapUtils;
import com.tesu.creditgold.util.Constants;
import com.tesu.creditgold.util.DialogUtils;
import com.tesu.creditgold.util.LogUtils;
import com.tesu.creditgold.util.SharedPrefrenceUtils;
import com.tesu.creditgold.util.StringUtils;
import com.tesu.creditgold.util.UIUtils;
import com.tesu.creditgold.widget.NoScrollGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotaOrderDetailActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_top_back;
    private View rootView;
    private TextView tv_order_id;  //订单号
    private EditText tv_name;  //姓名
    private EditText tv_id_card;  //身份证号
    private EditText tv_telephone;  //手机号
    private EditText tv_organization;  //单位名称
    private EditText tv_timber_name;  //木材品名
    private EditText tv_weight_quare;  //重量/方数
    private EditText tv_unit_price;  //单价
    private EditText tv_total_price;  //总价
    private EditText tv_yard_address;  //堆场/仓库地址
    private EditText tv_yard_contact_name;  //堆场联系人
    private EditText tv_yard_contact_telephone;  //堆场联系人电话

    private String name;
//    private String id_card;
    private String telephone;
    private String organization;
    private String timber_name;
    private String weight_quare;
    private String unit_price;
    private String total_price;
    private String yard_address;
    private String yard_contact_name;
    private String yard_contact_telephone;

    private NoScrollGridView gv_timber_goods;  //交易的木材商品照片
    private List<PictureItemBean> timberGoodsPictureItemBeanList;
    private ShowPictureAdapter mTimberGoodsPictureAdapter;
    private PictureAdapter mTimberGoodsPictureAdapter1;  //可以修改

    private NoScrollGridView gv_group_photo;  //借款人、木材商及交易商品合照
    private List<PictureItemBean> groupPictureItemBeanList;
    private ShowPictureAdapter mGroupPictureAdapter;
    private PictureAdapter mGroupPictureAdapter1; //可以修改

    private NoScrollGridView gv_yard_entry_photo;  //堆场入仓单或登记证
    private List<PictureItemBean> yardEntryPictureItemBeanList;
    private ShowPictureAdapter mYardEntryPictureAdapter;
    private PictureAdapter mYardEntryPictureAdapter1; //可以修改

    private NoScrollGridView gv_wood_pound_note_photo;  //交易的木材对应磅单
    private List<PictureItemBean> woodPoundNotePictureItemBeanList;
    private ShowPictureAdapter mWoodPoundNotePictureAdapter;
    private PictureAdapter mWoodPoundNotePictureAdapter1;  //可以修改

    private NoScrollGridView gv_wood_stack_photo;  //交易的木材堆号/柜号
    private List<PictureItemBean> woodStackPictureItemBeanList;
    private ShowPictureAdapter mWoodStackPictureAdapter;
    private PictureAdapter mWoodStackPictureAdapter1;  //可以修改

    private Button btn_copy_order_id;
    private Button btn_edite;
    private String order_sn;
    private Dialog loadingDialog;
    private Gson gson;
    private String url;
    private GetQuotaOrderInfoResponse getQuotaOrderInfoResponse;
    private QuotaOrderBean quotaOrderBean;
    private static final int SHOWIMAGE = 101;
    private boolean isChange;
    private FkBaseResponse fkbaseResponse;

    private PopupWindow pWindow;
    private View root;
    private LayoutInflater mInflater;
    private String timepath;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private File mFile;
    private String path;
    private int position;
    private int addType;  //添加照片类型  0 交易的木材商品照片   1,借款人、木材商及交易商品合照   2,堆场入仓单或登记证  3,交易的木材对应磅单
    // 4,交易的木材堆号/柜号

    private ArrayList<PhotoUpImageItem> selectImages;
    private String userId;

    // 图片储存成功后
    protected static final int INTERCEPT = 3;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERCEPT:
                    uploadImage(path);
                    break;
            }
        }
    };

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_quota_order_detail, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_order_id = (TextView) rootView.findViewById(R.id.tv_order_id);
        tv_name = (EditText) rootView.findViewById(R.id.tv_name);
        tv_id_card = (EditText) rootView.findViewById(R.id.tv_id_card);
        tv_telephone = (EditText) rootView.findViewById(R.id.tv_telephone);
        tv_organization = (EditText) rootView.findViewById(R.id.tv_organization);
        tv_timber_name = (EditText) rootView.findViewById(R.id.tv_timber_name);
        tv_weight_quare = (EditText) rootView.findViewById(R.id.tv_weight_quare);
        tv_unit_price = (EditText) rootView.findViewById(R.id.tv_unit_price);
        tv_total_price = (EditText) rootView.findViewById(R.id.tv_total_price);
        tv_yard_address = (EditText) rootView.findViewById(R.id.tv_yard_address);
        tv_yard_contact_name = (EditText) rootView.findViewById(R.id.tv_yard_contact_name);
        tv_yard_contact_telephone = (EditText) rootView.findViewById(R.id.tv_yard_contact_telephone);
        gv_timber_goods = (NoScrollGridView) rootView.findViewById(R.id.gv_timber_goods);
        gv_group_photo = (NoScrollGridView) rootView.findViewById(R.id.gv_group_photo);
        gv_yard_entry_photo = (NoScrollGridView) rootView.findViewById(R.id.gv_yard_entry_photo);
        gv_wood_pound_note_photo = (NoScrollGridView) rootView.findViewById(R.id.gv_wood_pound_note_photo);
        gv_wood_stack_photo = (NoScrollGridView) rootView.findViewById(R.id.gv_wood_stack_photo);
        btn_copy_order_id = (Button) rootView.findViewById(R.id.btn_copy_order_id);
        btn_edite = (Button) rootView.findViewById(R.id.btn_edite);

        setChangeed(false);
        initData();

        tv_top_back.setOnClickListener(this);
        btn_copy_order_id.setOnClickListener(this);
        btn_edite.setOnClickListener(this);
        return null;
    }

    private void setChangeed(boolean b) {
            tv_name.setEnabled(b);
            tv_telephone.setEnabled(b);
            tv_organization.setEnabled(b);
            tv_timber_name.setEnabled(b);
            tv_weight_quare.setEnabled(b);
            tv_unit_price.setEnabled(b);
            tv_total_price.setEnabled(b);
            tv_yard_address.setEnabled(b);
            tv_yard_contact_name.setEnabled(b);
            tv_yard_contact_telephone.setEnabled(b);

        if(b){
            PictureItemBean pictureItemBean = new PictureItemBean();

            timberGoodsPictureItemBeanList.add(pictureItemBean);
            mTimberGoodsPictureAdapter1 = new PictureAdapter(this, timberGoodsPictureItemBeanList);
            gv_timber_goods.setAdapter(mTimberGoodsPictureAdapter1);

            groupPictureItemBeanList.add(pictureItemBean);
            mGroupPictureAdapter1 = new PictureAdapter(this, groupPictureItemBeanList);
            gv_group_photo.setAdapter(mGroupPictureAdapter1);

            yardEntryPictureItemBeanList.add(pictureItemBean);
            mYardEntryPictureAdapter1 = new PictureAdapter(this, yardEntryPictureItemBeanList);
            gv_yard_entry_photo.setAdapter(mYardEntryPictureAdapter1);

            woodPoundNotePictureItemBeanList.add(pictureItemBean);
            mWoodPoundNotePictureAdapter1 = new PictureAdapter(this, woodPoundNotePictureItemBeanList);
            gv_wood_pound_note_photo.setAdapter(mWoodPoundNotePictureAdapter1);

            woodStackPictureItemBeanList.add(pictureItemBean);
            mWoodStackPictureAdapter1 = new PictureAdapter(this, woodStackPictureItemBeanList);
            gv_wood_stack_photo.setAdapter(mWoodStackPictureAdapter1);

            mTimberGoodsPictureAdapter1.setCallBack(new PictureAdapter.ICallBack() {
                @Override
                public void addImage(int pos) {
                    addType = 0;
                    position = pos;
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                @Override
                public void deleteImage(int pos) {
                    timberGoodsPictureItemBeanList.remove(pos);
                    mTimberGoodsPictureAdapter1.notifyDataSetChanged();
                }

                @Override
                public void showImage(int pos) {
                    addType = 0;
                    position = pos;
                    PictureItemBean pictureItemBean2 = timberGoodsPictureItemBeanList.get(pos);
                    Intent intent1 = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", pictureItemBean2);
                    UIUtils.startActivityForResult(intent1, SHOWIMAGE);
                }
            });
            mGroupPictureAdapter1.setCallBack(new PictureAdapter.ICallBack() {
                @Override
                public void addImage(int pos) {
                    addType = 1;
                    position = pos;
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                @Override
                public void deleteImage(int pos) {
                    groupPictureItemBeanList.remove(pos);
                    mGroupPictureAdapter1.notifyDataSetChanged();
                }

                @Override
                public void showImage(int pos) {
                    addType = 1;
                    position = pos;
                    PictureItemBean pictureItemBean2 = groupPictureItemBeanList.get(pos);
                    Intent intent1 = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", pictureItemBean2);
                    UIUtils.startActivityForResult(intent1, SHOWIMAGE);
                }
            });
            mYardEntryPictureAdapter1.setCallBack(new PictureAdapter.ICallBack() {
                @Override
                public void addImage(int pos) {
                    addType = 2;
                    position = pos;
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                @Override
                public void deleteImage(int pos) {
                    yardEntryPictureItemBeanList.remove(pos);
                    mYardEntryPictureAdapter1.notifyDataSetChanged();
                }

                @Override
                public void showImage(int pos) {
                    addType = 2;
                    position = pos;
                    PictureItemBean pictureItemBean2 = yardEntryPictureItemBeanList.get(pos);
                    Intent intent1 = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", pictureItemBean2);
                    UIUtils.startActivityForResult(intent1, SHOWIMAGE);
                }
            });
            mWoodPoundNotePictureAdapter1.setCallBack(new PictureAdapter.ICallBack() {
                @Override
                public void addImage(int pos) {
                    addType = 3;
                    position = pos;
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                @Override
                public void deleteImage(int pos) {
                    woodPoundNotePictureItemBeanList.remove(pos);
                    mWoodPoundNotePictureAdapter1.notifyDataSetChanged();
                }

                @Override
                public void showImage(int pos) {
                    addType = 3;
                    position = pos;
                    PictureItemBean pictureItemBean2 = woodPoundNotePictureItemBeanList.get(pos);
                    Intent intent1 = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", pictureItemBean2);
                    UIUtils.startActivityForResult(intent1, SHOWIMAGE);
                }
            });
            mWoodStackPictureAdapter1.setCallBack(new PictureAdapter.ICallBack() {
                @Override
                public void addImage(int pos) {
                    addType = 4;
                    position = pos;
                    pWindow.showAtLocation((PercentRelativeLayout) findViewById(R.id.relative),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                @Override
                public void deleteImage(int pos) {
                    woodStackPictureItemBeanList.remove(pos);
                    mWoodStackPictureAdapter1.notifyDataSetChanged();
                }

                @Override
                public void showImage(int pos) {
                    addType = 4;
                    position = pos;
                    PictureItemBean pictureItemBean2 = woodStackPictureItemBeanList.get(pos);
                    Intent intent1 = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                    intent1.putExtra("pictureItemBean", pictureItemBean2);
                    UIUtils.startActivityForResult(intent1, SHOWIMAGE);
                }
            });

        }else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            path = dir + "/" + timepath + ".jpg";

            tv_name.setFocusable(false);
            tv_telephone.setFocusable(false);
            tv_organization.setFocusable(false);
            tv_timber_name.setFocusable(false);
            tv_weight_quare.setFocusable(false);
            tv_unit_price.setFocusable(false);
            tv_total_price.setFocusable(false);
            tv_yard_address.setFocusable(false);
            tv_yard_contact_name.setFocusable(false);
            tv_yard_contact_telephone.setFocusable(false);

            new Thread() {
                @Override
                public void run() {
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(path);
                        if (photo != null) {
                            Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(path));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());
//                            String suoPath = BitmapUtils.saveMyBitmap(suoName,new_photo);
//                            uploadImage(suoPath);

                            path = BitmapUtils.saveMyBitmap(suoName, new_photo);
                            handler.sendEmptyMessage(INTERCEPT);

                            if (!photo.isRecycled()) {
                                photo.recycle();
                            }
                            if (!new_photo.isRecycled()) {
                                new_photo.recycle();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            //通知相册刷新
            Uri uriData = Uri.parse("file://" + path);
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriData));


        }

        // 读取相册缩放图片
        else if (requestCode == PHOTO_ZOOM && data != null) {
            getMultiplePhoto(data);
        }

        if (requestCode == SHOWIMAGE && data != null) {
            if (addType == 0) {
                timberGoodsPictureItemBeanList.remove(position);
                mTimberGoodsPictureAdapter.notifyDataSetChanged();
            } else if (addType == 1) {
                groupPictureItemBeanList.remove(position);
                mGroupPictureAdapter.notifyDataSetChanged();
            } else if (addType == 2) {
                yardEntryPictureItemBeanList.remove(position);
                mYardEntryPictureAdapter.notifyDataSetChanged();
            } else if (addType == 3) {
                woodPoundNotePictureItemBeanList.remove(position);
                mWoodPoundNotePictureAdapter.notifyDataSetChanged();
            } else if (addType == 4) {
                woodStackPictureItemBeanList.remove(position);
                mWoodStackPictureAdapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从相册读取多个照片
     *
     * @param data
     */
    private void getMultiplePhoto(Intent data) {
        String selectImagesStr = data.getStringExtra("selectImages");
        if (!TextUtils.isEmpty(selectImagesStr)) {
            LogUtils.e("selectImagesStr:" + selectImagesStr);
            selectImages = gson.fromJson(selectImagesStr, new TypeToken<List<PhotoUpImageItem>>() {
            }.getType());
            if (selectImages != null && selectImages.size() > 0) {
                for (int i = 0; i < selectImages.size(); i++) {
                    PhotoUpImageItem imageItem = selectImages.get(i);
                    try {
                        Bitmap photo = BitmapUtils.getSmallBitmap(imageItem.getImagePath());
                        if (photo != null) {
                            Bitmap new_photo = BitmapUtils.rotateBitmapByDegree(photo, BitmapUtils.getBitmapDegree(imageItem.getImagePath()));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_photo.compress(Bitmap.CompressFormat.JPEG, getResources().getInteger(R.integer.bitmap_compress), baos);

                            String suoName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                    .format(new Date());

                            String imgPath = BitmapUtils.saveMyBitmap(suoName + i, new_photo);

                            selectImages.get(i).setImagePath(imgPath);

                            if (i == selectImages.size() - 1) {
                                xUtilsUpload(i, true);
                            } else {
                                xUtilsUpload(i, false);
                            }

                            if (!photo.isRecycled()) {
                                photo.recycle();
                            }
                            if (!new_photo.isRecycled()) {
                                new_photo.recycle();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void xUtilsUpload(final int i, final boolean isLast) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        RequestParams params = new RequestParams();
        params.addBodyParameter("usrid", userId);
//      传图片时，要写3个参数
//      imageFile：键名
//      new File(path)：要上传的图片，path图片路径
//      image/jpg：上传图片的扩展名
        params.addBodyParameter("pic_string", new File(selectImages.get(i).getImagePath()), "image/jpg");
        HttpUtils http = new HttpUtils(Constants.XutilTimeOut);
        http.configCurrentHttpCacheExpiry(Constants.XutilCache);
        http.send(HttpRequest.HttpMethod.POST,
                url,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String resultStr = responseInfo.result;

                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        fkbaseResponse = gson.fromJson(resultStr, FkBaseResponse.class);
                        LogUtils.e("baseResponse:" + resultStr.toString());
                        HashMap<String, String> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                        String picUrl = hashMap.get("pic_path");
                        if (fkbaseResponse.getCode() == 0) {
                            PictureItemBean pictureItemBean = new PictureItemBean();
                            pictureItemBean.setPicturePath(selectImages.get(i).getImagePath());
                            pictureItemBean.setPictureUrl(picUrl);

                            if (addType == 0) {
                                timberGoodsPictureItemBeanList.add(0, pictureItemBean);
                                mTimberGoodsPictureAdapter1.notifyDataSetChanged();
                            } else if (addType == 1) {
                                groupPictureItemBeanList.add(0, pictureItemBean);
                                mGroupPictureAdapter1.notifyDataSetChanged();
                            } else if (addType == 2) {
                                yardEntryPictureItemBeanList.add(0, pictureItemBean);
                                mYardEntryPictureAdapter1.notifyDataSetChanged();
                            } else if (addType == 3) {
                                woodPoundNotePictureItemBeanList.add(0, pictureItemBean);
                                mWoodPoundNotePictureAdapter1.notifyDataSetChanged();
                            } else if (addType == 4) {
                                woodStackPictureItemBeanList.add(0, pictureItemBean);
                                mWoodStackPictureAdapter1.notifyDataSetChanged();
                            }
                        } else {
                            if (!isFinishing()) {
                                DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this,
                                        fkbaseResponse.getMsg());
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        if (isLast) {
                            loadingDialog.dismiss();
                        }
                        if (!isFinishing()) {
                            DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this,
                                    msg);
                        }
                    }
                });


    }

    private void initData() {
        userId = SharedPrefrenceUtils.getString(this, "usrid");

        timberGoodsPictureItemBeanList = new ArrayList<PictureItemBean>();
        mTimberGoodsPictureAdapter = new ShowPictureAdapter(this, timberGoodsPictureItemBeanList);
        gv_timber_goods.setAdapter(mTimberGoodsPictureAdapter);
        gv_timber_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictureItemBean pictureItemBean = timberGoodsPictureItemBeanList.get(position);
                Intent intent = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("pictureItemBean", pictureItemBean);
                UIUtils.startActivityForResult(intent, SHOWIMAGE);
            }
        });

        groupPictureItemBeanList = new ArrayList<PictureItemBean>();
        mGroupPictureAdapter = new ShowPictureAdapter(this, groupPictureItemBeanList);
        gv_group_photo.setAdapter(mGroupPictureAdapter);
        gv_group_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictureItemBean pictureItemBean = groupPictureItemBeanList.get(position);
                Intent intent = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("pictureItemBean", pictureItemBean);
                UIUtils.startActivityForResult(intent, SHOWIMAGE);
            }
        });

        yardEntryPictureItemBeanList = new ArrayList<PictureItemBean>();
        mYardEntryPictureAdapter = new ShowPictureAdapter(this, yardEntryPictureItemBeanList);
        gv_yard_entry_photo.setAdapter(mYardEntryPictureAdapter);
        gv_yard_entry_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictureItemBean pictureItemBean = yardEntryPictureItemBeanList.get(position);
                Intent intent = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("pictureItemBean", pictureItemBean);
                UIUtils.startActivityForResult(intent, SHOWIMAGE);
            }
        });

        woodPoundNotePictureItemBeanList = new ArrayList<PictureItemBean>();
        mWoodPoundNotePictureAdapter = new ShowPictureAdapter(this, woodPoundNotePictureItemBeanList);
        gv_wood_pound_note_photo.setAdapter(mWoodPoundNotePictureAdapter);
        gv_wood_pound_note_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictureItemBean pictureItemBean = woodPoundNotePictureItemBeanList.get(position);
                Intent intent = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("pictureItemBean", pictureItemBean);
                UIUtils.startActivityForResult(intent, SHOWIMAGE);
            }
        });

        woodStackPictureItemBeanList = new ArrayList<PictureItemBean>();
        mWoodStackPictureAdapter = new ShowPictureAdapter(this, woodStackPictureItemBeanList);
        gv_wood_stack_photo.setAdapter(mWoodStackPictureAdapter);
        gv_wood_stack_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictureItemBean pictureItemBean = woodStackPictureItemBeanList.get(position);
                Intent intent = new Intent(QuotaOrderDetailActivity.this, ShowImageActivity.class);
                intent.putExtra("pictureItemBean", pictureItemBean);
                UIUtils.startActivityForResult(intent, SHOWIMAGE);
            }
        });

        loadingDialog = DialogUtils.createLoadDialog(this, true);
        gson = new Gson();

        Intent intent = getIntent();
        order_sn = intent.getStringExtra("order_sn");
        if(!TextUtils.isEmpty(order_sn)){
            getOrderInfo();
        }

        if (mInflater == null) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        root = mInflater.inflate(R.layout.alert_dialog, null);
        pWindow = new PopupWindow(root, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);
        root.findViewById(R.id.btn_Phone).setOnClickListener(itemsOnClick);
        root.findViewById(R.id.btn_TakePicture)
                .setOnClickListener(itemsOnClick);
        root.findViewById(R.id.bg_photo).getBackground().setAlpha(100);

        root.findViewById(R.id.btn_cancel).setOnClickListener(itemsOnClick);




    }

    /**
     * 获得订单详情
     */
    private void getOrderInfo() {
        loadingDialog.show();
        GetUsrInfRequest getUsrInfRequest = new GetUsrInfRequest();
        url = Constants.FENGKONGSERVER_URL+"tsfkxt/user/getUsrOrderInf.do";
        getUsrInfRequest.map.put("order_sn", order_sn);
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, getUsrInfRequest.map, new MyVolley.VolleyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                LogUtils.e("获取额度订单详情:" + json);
                getQuotaOrderInfoResponse = gson.fromJson(json, GetQuotaOrderInfoResponse.class);
                if (getQuotaOrderInfoResponse.getCode()==0) {
                    quotaOrderBean = getQuotaOrderInfoResponse.getReturn_param();
                    if(quotaOrderBean != null){
                        setMessage();
                    }

                } else {
                    DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this,
                            getQuotaOrderInfoResponse.getMsg());

                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this, error);
            }
        });
    }

    private void setMessage() {
        if(!TextUtils.isEmpty(quotaOrderBean.getOrder_sn())){
            tv_order_id.setText(quotaOrderBean.getOrder_sn());
        }
        if(!TextUtils.isEmpty(quotaOrderBean.getStore_contract())){
            tv_name.setText(quotaOrderBean.getStore_contract());
        }
//        if(!TextUtils.isEmpty(quotaOrderBean.getStore_contract_id_card())){
//            tv_id_card.setText(quotaOrderBean.getStore_contract_id_card());
//        }
        if(!TextUtils.isEmpty(quotaOrderBean.getStore_tel())){
            tv_telephone.setText(quotaOrderBean.getStore_tel());
        }
        if(!TextUtils.isEmpty(quotaOrderBean.getStore_com_name())){
            tv_organization.setText(quotaOrderBean.getStore_com_name());
        }
        if(!TextUtils.isEmpty(quotaOrderBean.getWood_type_name())){
            tv_timber_name.setText(quotaOrderBean.getWood_type_name());
        }
        if(!TextUtils.isEmpty(quotaOrderBean.getQuantity())){
            tv_weight_quare.setText(quotaOrderBean.getQuantity());
        }
        tv_unit_price.setText(quotaOrderBean.getUnit_price()+"");
        tv_total_price.setText(quotaOrderBean.getOrder_money()+"");
        if(!TextUtils.isEmpty(quotaOrderBean.getWarehouse_address())){
            tv_yard_address.setText(quotaOrderBean.getWarehouse_address());
        }
        if(!TextUtils.isEmpty(quotaOrderBean.getWarehouse_contract())){
            tv_yard_contact_name.setText(quotaOrderBean.getWarehouse_contract());
        }
        if(!TextUtils.isEmpty(quotaOrderBean.getWarehouse_contract_tel())){
            tv_yard_contact_telephone.setText(quotaOrderBean.getWarehouse_contract_tel());
        }

        List<UsrOrderPicBean> usr_order_pic_list = quotaOrderBean.getUsr_order_pic_list();
        for(int i=0;i<usr_order_pic_list.size();i++){
            UsrOrderPicBean usrOrderPicBean = usr_order_pic_list.get(i);
            PictureItemBean pictureItemBean = new PictureItemBean();
//            pictureItemBean.setPictureUrl(usrOrderPicBean.getPic_addr());
            pictureItemBean.setPicWholeUrl(usrOrderPicBean.getPic_addr());
            String address = usrOrderPicBean.getPic_addr();
            address = address.substring(address.indexOf("//")+2);
            address = address.substring(address.indexOf("//")+2);
            pictureItemBean.setPictureUrl(address);
            LogUtils.e("address:"+address);
            switch (usrOrderPicBean.getPic_type()){
                case 19:
                    timberGoodsPictureItemBeanList.add(pictureItemBean);
                    break;
                case 20:
                    groupPictureItemBeanList.add(pictureItemBean);
                    break;
                case 21:
                    yardEntryPictureItemBeanList.add(pictureItemBean);
                    break;
                case 22:
                    woodPoundNotePictureItemBeanList.add(pictureItemBean);
                    break;
                case 23:
                    woodStackPictureItemBeanList.add(pictureItemBean);
                    break;
            }
        }
        mGroupPictureAdapter.notifyDataSetChanged();
        mTimberGoodsPictureAdapter.notifyDataSetChanged();
        mWoodPoundNotePictureAdapter.notifyDataSetChanged();
        mWoodStackPictureAdapter.notifyDataSetChanged();
        mYardEntryPictureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_back:
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            case R.id.btn_copy_order_id:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tv_order_id.getText());
                UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"订单号复制成功!");
                break;
            case R.id.btn_edite:
                if(isChange){
                    isChange = false;
                    name = tv_name.getText().toString();
                    if(TextUtils.isEmpty(name)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"商家姓名不能为空");
                        return;
                    }
                    telephone = tv_telephone.getText().toString();
                    if(TextUtils.isEmpty(telephone)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"商家手机号不能为空");
                        return;
                    }
                    if (!StringUtils.isMobileNO(telephone)) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "请输入正确的手机号码");
                        return;
                    }
                    organization = tv_organization.getText().toString();
                    if(TextUtils.isEmpty(organization)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"单位名称不能为空");
                        return;
                    }
                    timber_name = tv_timber_name.getText().toString();
                    if(TextUtils.isEmpty(timber_name)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"木材品名不能为空");
                        return;
                    }
                    weight_quare = tv_weight_quare.getText().toString();
                    if(TextUtils.isEmpty(weight_quare)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"重量/方数不能为空");
                        return;
                    }
                    unit_price = tv_unit_price.getText().toString();
                    if(TextUtils.isEmpty(unit_price)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"单价不能为空");
                        return;
                    }
                    total_price = tv_total_price.getText().toString();
                    if(TextUtils.isEmpty(total_price)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"总价不能为空");
                        return;
                    }
                    yard_address = tv_yard_address.getText().toString();
                    if(TextUtils.isEmpty(yard_address)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"堆场/仓库地址不能为空");
                        return;
                    }
                    yard_contact_name = tv_yard_contact_name.getText().toString();
                    if(TextUtils.isEmpty(yard_contact_name)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"堆场/仓库联系人不能为空");
                        return;
                    }
                    yard_contact_telephone = tv_yard_contact_telephone.getText().toString();
                    if(TextUtils.isEmpty(yard_contact_telephone)){
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"堆场/仓库联系人电话不能为空");
                        return;
                    }
                    if (!StringUtils.isMobileNO(yard_contact_telephone)) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "请输入正确的堆场联系人电话");
                        return;
                    }

                    if (timberGoodsPictureItemBeanList.size() <= 2) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "交易的木材商品照片至少需要2张");
                        return;
                    }
                    if (groupPictureItemBeanList.size() <= 2) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "借款人、木材商及交易商品合照至少需要2张");
                        return;
                    }
                    if (yardEntryPictureItemBeanList.size() <= 1) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "堆场入仓单或登记证至少需要1张");
                        return;
                    }
                    if (woodPoundNotePictureItemBeanList.size() <= 1) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "交易的木材对应磅单至少需要1张");
                        return;
                    }
                    if (woodStackPictureItemBeanList.size() <= 1) {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "交易的木材堆号/柜号至少需要1张");
                        return;
                    }
                    setUsrOrder();

                }else {
                    if(quotaOrderBean.getSubmit_step()==-1){
                        isChange = true;
                        btn_edite.setText("保存");
                        setChangeed(true);
                    }else {
                        UIUtils.showToastCenter(QuotaOrderDetailActivity.this,"该订单不能修改");
                    }
                }
                break;
        }
    }

    /**
     * 修改额度订单
     */
    private void setUsrOrder() {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/setUsrOrder_B.do";
        Map<String, String> params = new HashMap<String, String>();
        params.put("usr_order_id", quotaOrderBean.getUsr_order_id());
        params.put("store_uid", SharedPrefrenceUtils.getString(this, "usrid"));
        params.put("store_contract", name);
        params.put("store_tel", telephone);
        params.put("unit_price", unit_price);
        params.put("quantity", weight_quare);
        params.put("com_name", organization);
        params.put("wood_type_name", timber_name);
        params.put("warehouse_address", yard_address);
        params.put("warehouse_contract", yard_contact_name);
        params.put("warehouse_contract_tel", yard_contact_telephone);
//        params.put("submit_step", quotaOrderBean.getSubmit_step()+"");
        params.put("order_money", total_price);
        List<UsrOrderPicBean> usrOrderPicBeanList = new ArrayList<UsrOrderPicBean>();
        for (int i = 0; i < timberGoodsPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = timberGoodsPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(19);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("交易的木材商品照片");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < groupPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = groupPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(20);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("借款人、木材商及交易商品合照");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < yardEntryPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = yardEntryPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(21);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("堆场入仓单或登记证");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < woodPoundNotePictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = woodPoundNotePictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(22);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("交易的木材对应磅单");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        for (int i = 0; i < woodStackPictureItemBeanList.size() - 1; i++) {
            PictureItemBean pictureItemBean = woodStackPictureItemBeanList.get(i);
            UsrOrderPicBean usrOrderPicBean = new UsrOrderPicBean();
            usrOrderPicBean.setPic_type(23);
            usrOrderPicBean.setPic_addr(pictureItemBean.getPictureUrl());
            usrOrderPicBean.setPic_desc("交易的木材堆号/柜号");
            usrOrderPicBeanList.add(usrOrderPicBean);
        }
        params.put("usr_order_pic_list", gson.toJson(usrOrderPicBeanList)); //
        LogUtils.e("修改订单params:" + params.toString());
        MyVolley.uploadNoFileWholeUrl(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("修改订单:"+json);
                loadingDialog.dismiss();
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                if (fkbaseResponse.getCode() == 0) {
//                    setChangeed(false);
//                    btn_edite.setText("修改");
                    UIUtils.showToastCenter(QuotaOrderDetailActivity.this, "修改成功");
                    Intent intent = getIntent();
                    setResult(200,intent);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this, fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this, error);
                }

            }
        });
    }

    /**
     * 上传图片
     */
    private void uploadImage(final String suoPath) {
        loadingDialog.show();
        String url = Constants.FENGKONGSERVER_URL + "tsfkxt/user/uploadPicture.do";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("usrid", SharedPrefrenceUtils.getString(this, "usrid"));
        Map<String, String> filesMap = new HashMap<String, String>();
        filesMap.put("pic_string", suoPath);
        MyVolley.uploadWithFileWholeUrl(url, paramMap, filesMap, null, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                tv_name.setFocusable(true);
                tv_name.setFocusableInTouchMode(true);
                tv_telephone.setFocusable(true);
                tv_telephone.setFocusableInTouchMode(true);
                tv_organization.setFocusable(true);
                tv_organization.setFocusableInTouchMode(true);
                tv_timber_name.setFocusable(true);
                tv_timber_name.setFocusableInTouchMode(true);
                tv_weight_quare.setFocusable(true);
                tv_weight_quare.setFocusableInTouchMode(true);
                tv_unit_price.setFocusable(true);
                tv_unit_price.setFocusableInTouchMode(true);
                tv_total_price.setFocusable(true);
                tv_total_price.setFocusableInTouchMode(true);
                tv_yard_address.setFocusable(true);
                tv_yard_address.setFocusableInTouchMode(true);
                tv_yard_contact_name.setFocusable(true);
                tv_yard_contact_name.setFocusableInTouchMode(true);
                tv_yard_contact_telephone.setFocusable(true);
                tv_yard_contact_telephone.setFocusableInTouchMode(true);


                loadingDialog.dismiss();
                fkbaseResponse = gson.fromJson(json, FkBaseResponse.class);
                LogUtils.e("baseResponse:" + json.toString());
                HashMap<String, String> hashMap = gson.fromJson(gson.toJson(fkbaseResponse.getReturn_param()), HashMap.class);
                String picUrl = hashMap.get("pic_path");
                if (fkbaseResponse.getCode() == 0) {
                    PictureItemBean pictureItemBean = new PictureItemBean();
                    pictureItemBean.setPicturePath(suoPath);
                    pictureItemBean.setPictureUrl(picUrl);

                    if (addType == 0) {
                        timberGoodsPictureItemBeanList.add(0, pictureItemBean);
                        mTimberGoodsPictureAdapter1.notifyDataSetChanged();
                    } else if (addType == 1) {
                        groupPictureItemBeanList.add(0, pictureItemBean);
                        mGroupPictureAdapter1.notifyDataSetChanged();
                    } else if (addType == 2) {
                        yardEntryPictureItemBeanList.add(0, pictureItemBean);
                        mYardEntryPictureAdapter1.notifyDataSetChanged();
                    } else if (addType == 3) {
                        woodPoundNotePictureItemBeanList.add(0, pictureItemBean);
                        mWoodPoundNotePictureAdapter1.notifyDataSetChanged();
                    } else if (addType == 4) {
                        woodStackPictureItemBeanList.add(0, pictureItemBean);
                        mWoodStackPictureAdapter1.notifyDataSetChanged();
                    }
                } else {
                    if (!isFinishing()) {
                        DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this,
                                fkbaseResponse.getMsg());
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                tv_name.setFocusable(true);
                tv_name.setFocusableInTouchMode(true);
                tv_telephone.setFocusable(true);
                tv_telephone.setFocusableInTouchMode(true);
                tv_organization.setFocusable(true);
                tv_organization.setFocusableInTouchMode(true);
                tv_timber_name.setFocusable(true);
                tv_timber_name.setFocusableInTouchMode(true);
                tv_weight_quare.setFocusable(true);
                tv_weight_quare.setFocusableInTouchMode(true);
                tv_unit_price.setFocusable(true);
                tv_unit_price.setFocusableInTouchMode(true);
                tv_total_price.setFocusable(true);
                tv_total_price.setFocusableInTouchMode(true);
                tv_yard_address.setFocusable(true);
                tv_yard_address.setFocusableInTouchMode(true);
                tv_yard_contact_name.setFocusable(true);
                tv_yard_contact_name.setFocusableInTouchMode(true);
                tv_yard_contact_telephone.setFocusable(true);
                tv_yard_contact_telephone.setFocusableInTouchMode(true);

                loadingDialog.dismiss();
                if (!isFinishing()) {
                    DialogUtils.showAlertDialog(QuotaOrderDetailActivity.this,
                            error);
                }
            }
        });

    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            pWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_TakePicture: {
                    timepath = new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date());

                    if (Environment.MEDIA_MOUNTED.equals(Environment
                            .getExternalStorageState())) {
                        File dir = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        mFile = new File(dir, timepath + ".jpg");

                        startActivityForResult(new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile)),
                                PHOTO_GRAPH);
                    }
                    break;
                }
                case R.id.btn_Phone: {
                    Intent intent = new Intent(QuotaOrderDetailActivity.this, ChoosePhotoFolderActivity.class);
                    startActivityForResult(intent, PHOTO_ZOOM);

                    break;
                }
                case R.id.btn_cancel: {
                    pWindow.dismiss();
                    break;
                }
                default:
                    break;
            }

        }

    };

}
