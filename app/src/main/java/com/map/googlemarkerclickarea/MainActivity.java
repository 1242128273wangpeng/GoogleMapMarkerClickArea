package com.map.googlemarkerclickarea;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.AdvancedMarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final LatLng operaHouse = new LatLng(-33.8567844, 151.213108);
    private static final LatLng tarongaZoo = new LatLng(-33.8472767, 151.2188164);
    private static final LatLng manlyBeach = new LatLng(-33.8209738, 151.2563253);
    private static final LatLng hyderPark = new LatLng(-33.8690081, 151.2052393);
    private static final LatLng circularQuay = new LatLng(-33.858761, 151.2055688);
    private static final LatLng harbourBridge = new LatLng(-33.852228, 151.2038374);
    private static final LatLng kingsCross = new LatLng(-33.8737375, 151.222569);
    private static final LatLng botanicGardens = new LatLng(-33.864167, 151.216387);
    private static final LatLng museumOfSydney = new LatLng(-33.8636005, 151.2092542);
    private static final float ZOOM_LEVEL = 3.5f;
    private static final String TAG = MainActivity.class.getName();

    private final List<MarkerExt> markerList = new ArrayList<>();
    private final List<BitmapDescriptor> iconList = new ArrayList<>();
    private List<LatLng> latLngList = new ArrayList<>();
    private Drawable normalDrawable = null;
    private Drawable selectDrawable = null;
    private GoogleMap googleMap = null;
    private BitmapDescriptor selectBitmapDescriptor = null;
    private final List<Polyline> polylineList = new ArrayList<>();
    private int topMargin = 0;

    public Bitmap changeBitmapBackgroundColor(Bitmap srcBitmap, int backgroundColor, PorterDuff.Mode mode) {
        // 创建一个和原始Bitmap大小相同的Bitmap
        Bitmap newBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        // 使用Canvas来绘制新的Bitmap
        Canvas canvas = new Canvas(newBitmap);
        // 创建一个Paint对象，并设置其颜色为新的背景色
        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL); // 设置填充样式
        // 绘制背景色
        canvas.drawRect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), paint);
        // 将原始Bitmap绘制在新的Bitmap上，这样原始Bitmap的内容不会丢失
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        return newBitmap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_click_area);
        normalDrawable = ResourcesCompat.getDrawable(MainActivity.this.getResources(), R.drawable.pegman, getTheme());
        selectDrawable = ResourcesCompat.getDrawable(MainActivity.this.getResources(), R.drawable.arrow, getTheme());
        selectBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(changeBitmapBackgroundColor(BitmapFactory.decodeResource(getResources(), R.drawable.arrow), Color.WHITE, PorterDuff.Mode.DARKEN));
        List<Integer> list = Arrays.asList(Color.MAGENTA, Color.BLUE, Color.BLACK, Color.CYAN, Color.DKGRAY, Color.LTGRAY, Color.GREEN, Color.RED, Color.GRAY);
        for (int i = 0; i < list.size(); i++) {
            int color = list.get(i);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(changeBitmapBackgroundColor(BitmapFactory.decodeResource(getResources(), R.drawable.pegman), color, PorterDuff.Mode.SCREEN));
            iconList.add(bitmapDescriptor);
        }
        latLngList = Arrays.asList(operaHouse, tarongaZoo, manlyBeach, hyderPark, circularQuay, harbourBridge, kingsCross, botanicGardens, museumOfSydney);
        Log.e(TAG, "onMarkerClick manRect===> width" + normalDrawable.getIntrinsicWidth() + " height:" + normalDrawable.getIntrinsicHeight());
        Log.e(TAG, "onMarkerClick arrowRect===> width" + normalDrawable.getIntrinsicWidth() + " height:" + normalDrawable.getIntrinsicHeight());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        for (int i = 0; i < latLngList.size(); i++) {
            BitmapDescriptor normalBitmapDescriptor = iconList.get(i);
            LatLng latLng = latLngList.get(i);
            AdvancedMarkerOptions markerOptions = new AdvancedMarkerOptions().icon(normalBitmapDescriptor).zIndex(i).position(latLng);
            Marker marker = map.addMarker(markerOptions);
            MarkerExt markerExt = new MarkerExt(marker, normalDrawable, normalBitmapDescriptor, selectDrawable, selectBitmapDescriptor);
            markerList.add(markerExt);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(museumOfSydney, ZOOM_LEVEL));
        map.setOnMapClickListener(latLng -> Log.e(TAG, "onMapClick===>" + latLng));
        map.setOnMarkerClickListener(marker -> {
            LatLng latLng = marker.getPosition();
            Log.e(TAG, "onMarkerClick===>" + latLng);
            return true;
        });
        new Handler().post(() -> {
            if (topMargin == 0) {
                topMargin = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() + getSupportActionBar().getHeight();
                updateMarkerExtRect(googleMap);
                Log.e(TAG, "topMargin:" + topMargin);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Projection projection = googleMap.getProjection();
            updateMarkerExtRect(googleMap);
            float x = event.getX();
            float y = event.getY() - topMargin;
            Point touchPoint = new Point((int) x, (int) y);
            LatLng touchLatLng = projection.fromScreenLocation(touchPoint);
            MarkerExt lastSelectMarkerExt = getLastSelectMarker(markerList);
            drawDebugDot(touchLatLng);
            MarkerExt selectMarkerExt = getSelectMarker(projection, markerList, touchLatLng);
            if (selectMarkerExt != null) {
                if (lastSelectMarkerExt != null) {
                    lastSelectMarkerExt.updateSelect(projection, false);
                }
                selectMarkerExt.updateSelect(projection, true);
                Log.e(TAG, "dispatchTouchEvent===>x:" + x + " y:" + y + " selectMarker:" + selectMarkerExt.marker);
            }
            drawDebugLine(googleMap);
        }
        return super.dispatchTouchEvent(event);
    }

    private void updateMarkerExtRect(GoogleMap googleMap) {
        for (int i = 0; i < polylineList.size(); i++) {
            Polyline polyline = polylineList.get(i);
            polyline.setVisible(false);
        }
        Projection projection = googleMap.getProjection();
        for (int i = 0; i < markerList.size(); i++) {
            MarkerExt markerExt = markerList.get(i);
            markerExt.updateRealRect(projection);
        }
    }

    private void drawDebugLine(GoogleMap googleMap) {
        for (int i = 0; i < markerList.size(); i++) {
            MarkerExt markerExt = markerList.get(i);
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(markerExt.leftBottomLat);
            polylineOptions.add(markerExt.rightTopLatLng);
            Polyline polyline = googleMap.addPolyline(polylineOptions);
            polyline.setZIndex(999);
            polylineList.add(polyline);
        }
    }

    private void drawDebugDot(LatLng touchLatLng) {
        googleMap.addCircle(new CircleOptions()
                .center(touchLatLng)
                .radius(5)
                .fillColor(Color.RED)
                .strokeColor(Color.MAGENTA)
                .zIndex(999)
                .clickable(true));
    }

    private MarkerExt getLastSelectMarker(List<MarkerExt> markerList) {
        MarkerExt lastSelectMarkerExt = null;
        for (int i = 0; i < markerList.size(); i++) {
            MarkerExt markerExt = markerList.get(i);
            if (markerExt.isSelect) {
                lastSelectMarkerExt = markerExt;
                break;
            }
        }
        return lastSelectMarkerExt;
    }

    private MarkerExt getSelectMarker(Projection projection, List<MarkerExt> markerList, LatLng clickLatLng) {
        Point targetPoint = projection.toScreenLocation(clickLatLng);
        List<MarkerExt> includingMarkerExtList = new ArrayList<>();
        for (int i = 0; i < markerList.size(); i++) {
            MarkerExt markerExt = markerList.get(i);
            if (markerExt.isCoordinateInRectangle(targetPoint, markerExt.leftBottomPoint, markerExt.rightTopPoint)) {
                includingMarkerExtList.add(markerExt);
            }
        }
        includingMarkerExtList.sort((o1, o2) -> Float.compare(o2.marker.getZIndex(), o1.marker.getZIndex()));
        if (includingMarkerExtList.isEmpty()) {
            return null;
        }
        MarkerExt markerExt = includingMarkerExtList.get(0);
        markerExt.isSelect = true;
        return markerExt;
    }
}
