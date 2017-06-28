package com.example.lhc.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
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
    private ShareUtils su;
    private List<LocalMedia> selectList = new ArrayList<>();
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_TAKE_PHOTO = 20;
    ActionSheetDialog phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        su = new ShareUtils(this);

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
                   for(int i=0;i<selectList.size();i++){

                       Log.i("back", "onActivityResult:" + selectList.get(i).getCutPath());
                   }

                    break;
            }
        }
    }
    private int maxSelectNum = 9;
    private int themeId = R.style.picture_QQ_style;
    private int chooseMode = PictureMimeType.ofAll();
    @OnClick({R.id.share, R.id.chose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share:
                share(share,"http://www.baidu.com","测试");
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
}
