package com.itheima.topline.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.itheima.topline.PrivacyPolicyDialog;
import com.itheima.topline.R;
import com.itheima.topline.view.SwipeBackLayout;


public class MapActivity extends AppCompatActivity {
    private MapView mMapView;
    private AMap mAmap;
    private Polyline mVirtureRoad;
    private Marker mMoveMarker;
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.0001;
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private SwipeBackLayout layout;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 显示隐私政策弹窗
        PrivacyPolicyDialog.showPrivacyDialog(this, new PrivacyPolicyDialog.OnPrivacyAgreedListener() {
            @Override
            public void onPrivacyAgreed() {
                // 用户同意隐私政策后继续初始化
                initializeApp(savedInstanceState);
            }
        });
    }

    private void initializeApp(Bundle savedInstanceState) {
        // 初始化高德地图 SDK
        com.amap.api.maps.MapsInitializer.updatePrivacyShow(this, true, true);
        com.amap.api.maps.MapsInitializer.updatePrivacyAgree(this, true);

        // 设置布局
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(R.layout.activity_map);

        // 初始化地图
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();

        // 检查并请求必要权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            init();
            initRoadData();
            moveLooper();
        }
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("地图");
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_back = findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapActivity.this.finish();
            }
        });
    }

    private void initRoadData() {
        double centerLatitude = 39.916049;
        double centerLontitude = 116.399792;
        double deltaAngle = Math.PI / 180 * 5;
        double radius = 0.02;
        PolylineOptions polylineOptions = new PolylineOptions();
        for (double i = 0; i < Math.PI * 2; i = i + deltaAngle) {
            float latitude = (float) (-Math.cos(i) * radius + centerLatitude);
            float longtitude = (float) (Math.sin(i) * radius + centerLontitude);
            polylineOptions.add(new LatLng(latitude, longtitude));
            if (i > Math.PI) {
                deltaAngle = Math.PI / 180 * 30;
            }
        }
        float latitude = (float) (-Math.cos(0) * radius + centerLatitude);
        float longtitude = (float) (Math.sin(0) * radius + centerLontitude);
        polylineOptions.add(new LatLng(latitude, longtitude));
        polylineOptions.width(10);
        polylineOptions.color(Color.RED);
        mVirtureRoad = mAmap.addPolyline(polylineOptions);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        markerOptions.position(polylineOptions.getPoints().get(0));
        mMoveMarker = mAmap.addMarker(markerOptions);
        mMoveMarker.setRotateAngle((float) getAngle(0));
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
            throw new RuntimeException("index out of bounds");
        }
        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {
        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude -
                fromPoint.longitude));
        return slope;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
                init();
                initRoadData();
                moveLooper();
            } else {
                // 权限被拒绝
                finish(); // 关闭应用
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 循环进行移动逻辑
     */
    public void moveLooper() {
        new Thread() {
            public void run() {
                while (true) {
                    for (int i = 0; i < mVirtureRoad.getPoints().size() - 1; i++) {
                        LatLng startPoint = mVirtureRoad.getPoints().get(i);
                        LatLng endPoint = mVirtureRoad.getPoints().get(i + 1);
                        mMoveMarker.setPosition(startPoint);
                        mMoveMarker.setRotateAngle((float) getAngle(startPoint,
                                endPoint));
                        double slope = getSlope(startPoint, endPoint);
                        // 是否是正向的标志（向上设为正向）
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);
                        double intercept = getInterception(slope, startPoint);
                        double xMoveDistance = isReverse ? getXMoveDistance(slope)
                                : -1 * getXMoveDistance(slope);
                        for (double j = startPoint.latitude;
                             !((j > endPoint.latitude) ^ isReverse);
                             j = j - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope != Double.MAX_VALUE) {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            } else {
                                latLng = new LatLng(j, startPoint.longitude);
                            }
                            mMoveMarker.setPosition(latLng);
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
    }
}