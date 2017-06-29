package com.example.lhc.share;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.example.lhc.share.bean.CardBean;
import com.example.lhc.share.bean.JsonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.umeng.socialize.media.UMImage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import simbest.com.sharelib.IShareCallback;
import simbest.com.sharelib.ShareModel;
import simbest.com.sharelib.ShareUtils;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.share)
    Button share;
    @Bind(R.id.chose)
    Button chose;
    @Bind(R.id.time)
    Button time;
    @Bind(R.id.muthchose)
    Button muthchose;
    @Bind(R.id.city)
    Button city;
    @Bind(R.id.choseitem)
    Button choseitem;
    @Bind(R.id.customtime)
    Button customtime;
    private ShareUtils su;
    TimePickerView pvTime,pvCustomTime;
    private List<LocalMedia> selectList = new ArrayList<>();
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_TAKE_PHOTO = 20;
    ActionSheetDialog phone;
    OptionsPickerView optionsPickerView, pvCustomOptions;
    private ArrayList<String> food = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> computer = new ArrayList<>();
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ArrayList<CardBean> cardItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        su = new ShareUtils(this);
        initJsonData();
        getCardData();
        getNoLinkData();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (int i = 0; i < selectList.size(); i++) {

                        Log.i("back", "onActivityResult:" + selectList.get(i).getCutPath());
                    }

                    break;
            }
        }
    }

    private int maxSelectNum = 9;
    private int themeId = R.style.picture_QQ_style;
    private int chooseMode = PictureMimeType.ofAll();

    @OnClick({R.id.customtime,R.id.choseitem, R.id.city, R.id.muthchose, R.id.time, R.id.share, R.id.chose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share:
                share(share, "http://www.baidu.com", "测试");
                break;
            case R.id.muthchose:

                initNoLinkOptionsPicker();
                break;
            case R.id.city:
                ShowCityPickerView();
                break;
            case R.id.customtime:
                initCustomTimePicker();
                break;
            case R.id.choseitem:
                initCustomOptionPicker();
                break;
            case R.id.time:
                initTimePicker();
                break;
            case R.id.chose:
                phone = new ActionSheetDialog(MainActivity.this);
                phone.builder();
                phone.setCancelable(false);
                phone.setCanceledOnTouchOutside(false);
                phone.setTitle("上传证件图片");
                phone.addSheetItem("相册选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        // 进入相册 以下是例子：不需要的api可以不写
                        PictureSelector.create(MainActivity.this)
                                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                                .minSelectNum(1)// 最小选择数量
                                .imageSpanCount(4)// 每行显示个数
                                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选  PictureConfig.MULTIPLE : PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片
                                .previewVideo(true)// 是否可预览视频
                                .enablePreviewAudio(true) // 是否可播放音频
                                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                                .isCamera(true)// 是否显示拍照按钮
                                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                                .enableCrop(true)// 是否裁剪
                                .compress(true)// 是否压缩
                                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                                .withAspectRatio(4, 4)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                                .isGif(false)// 是否显示gif图片
                                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                                .circleDimmedLayer(false)// 是否圆形裁剪
                                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                                .openClickSound(false)// 是否开启点击声音
                                .selectionMedia(selectList)// 是否传入已选图片
                                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                                        //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                                        //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                                        //.rotateEnabled() // 裁剪是否可旋转图片
                                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                                        //.videoQuality()// 视频录制质量 0 or 1
                                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                                        //.recordVideoSecond()//录制视频秒数 默认60s
                                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    }
                });
                phone.addSheetItem("拍照上传", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        PictureSelector.create(MainActivity.this)
                                .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                                .theme(themeId)// 主题样式设置 具体参考 values/styles
                                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                                .minSelectNum(1)// 最小选择数量
                                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                                .previewImage(true)// 是否可预览图片
                                .previewVideo(true)// 是否可预览视频
                                .enablePreviewAudio(true) // 是否可播放音频
                                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                                .isCamera(true)// 是否显示拍照按钮
                                .enableCrop(true)// 是否裁剪
                                .compress(true)// 是否压缩
                                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                                .withAspectRatio(4, 4)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                                .isGif(false)// 是否显示gif图片
                                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                                .circleDimmedLayer(true)// 是否圆形裁剪
                                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                                .openClickSound(false)// 是否开启点击声音
                                .selectionMedia(selectList)// 是否传入已选图片
                                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                                        //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                                        //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                                        //.rotateEnabled() // 裁剪是否可旋转图片
                                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                                        //.videoQuality()// 视频录制质量 0 or 1
                                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
                                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    }
                });
                phone.show();
                break;
        }
    }

    private void ShowCityPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1) +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

                Toast.makeText(MainActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Toast.makeText(MainActivity.this,getTime(date),Toast.LENGTH_LONG).show();
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
        pvCustomTime.show();

    }

    private void initTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_LONG).show();
            }
        })

              /*  .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字*/
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setType(new boolean[]{true, true, true, false, false, false})//设置显示年月日时分秒
                .setTitleText("时间选择")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示

                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                        //  .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                        // .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        // pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public void share(View view, String url, String title) {
        ShareModel model = new ShareModel();
        model.setTitle(title);
        model.setContent(url);
        model.setImageMedia(new UMImage(this, R.mipmap.ic_launcher));
        su.share(model, new IShareCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFaild() {
                Toast.makeText(MainActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initNoLinkOptionsPicker() {// 不联动的多级选项
        optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = "food:" + food.get(options1)
                        + "\nclothes:" + clothes.get(options2)
                        + "\ncomputer:" + computer.get(options3);

                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }).build();
        optionsPickerView.setNPicker(food, clothes, computer);
        optionsPickerView.show();
    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();
                Toast.makeText(MainActivity.this, tx, Toast.LENGTH_LONG).show();
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });
                        tvAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getCardData();
                                pvCustomOptions.setPicker(cardItem);
                            }
                        });

                    }
                })
                .isDialog(true)
                .build();

        pvCustomOptions.setPicker(cardItem);//添加数据
        pvCustomOptions.show();

    }

    private void getCardData() {
        for (int i = 0; i < 5; i++) {
            cardItem.add(new CardBean(i, "No.ABC12345 " + i));
        }

        for (int i = 0; i < cardItem.size(); i++) {
            if (cardItem.get(i).getCardNo().length() > 6) {
                String str_item = cardItem.get(i).getCardNo().substring(0, 6) + "...";
                cardItem.get(i).setCardNo(str_item);
            }
        }
    }

    private void getNoLinkData() {
        food.add("KFC");
        food.add("MacDonald");
        food.add("Pizza hut");

        clothes.add("Nike");
        clothes.add("Adidas");
        clothes.add("Anima");

        computer.add("ASUS");
        computer.add("Lenovo");
        computer.add("Apple");
        computer.add("HP");
    }

    private void initJsonData() {//解析数据
        ArrayList<JsonBean> jsonBean = null;
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = null;//获取assets目录下的json文件数据
        try {
            JsonData = new GetJsonDataUtil().getJson(this, "province.json");
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<JsonBean>>() {
            }.getType();
            jsonBean = gson.fromJson(JsonData, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        // options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            options1Items.add(jsonBean.get(i).getName());
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }


    }

}
