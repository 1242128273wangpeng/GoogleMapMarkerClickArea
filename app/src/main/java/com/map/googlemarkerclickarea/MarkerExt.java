package com.map.googlemarkerclickarea;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerExt {
    Marker marker;
    Point leftBottomPoint;
    Point rightTopPoint;
    LatLng leftBottomLat;
    LatLng rightTopLatLng;
    Drawable originDrawable;
    BitmapDescriptor originBitmapDescriptor;
    Drawable selectDrawable;
    BitmapDescriptor selectBitmapDescriptor;
    boolean isSelect;

    public MarkerExt(Marker marker, Drawable drawable, BitmapDescriptor bitmapDescriptor, Drawable selectDrawable, BitmapDescriptor selectBitmapDescriptor) {
        this.marker = marker;
        this.originDrawable = drawable;
        this.originBitmapDescriptor = bitmapDescriptor;
        this.selectDrawable = selectDrawable;
        this.selectBitmapDescriptor = selectBitmapDescriptor;
    }

    public void updateSelect(Projection projection, boolean isSelect) {
        this.isSelect = isSelect;
        marker.setIcon(isSelect ? selectBitmapDescriptor : originBitmapDescriptor);
        updateRealRect(projection);
    }

    public void updateRect(Projection projection) {
        Drawable drawable = isSelect ? selectDrawable : originDrawable;
        LatLng latLng = marker.getPosition();
        Point point = projection.toScreenLocation(latLng);
        int leftX = point.x - drawable.getIntrinsicWidth() / 2;
        int rightX = point.x + drawable.getIntrinsicWidth() / 2;
        int topY = point.y - drawable.getIntrinsicHeight() / 2;
        int bottomY = point.y + drawable.getIntrinsicHeight() / 2;
        Point leftBottomPoint = new Point(leftX, bottomY);
        LatLng leftBottomLat = projection.fromScreenLocation(leftBottomPoint);
        Point rightTopPoint = new Point(rightX, topY);
        LatLng rightTopLatLng = projection.fromScreenLocation(rightTopPoint);
        this.leftBottomPoint = leftBottomPoint;
        this.rightTopPoint = rightTopPoint;
        this.leftBottomLat = leftBottomLat;
        this.rightTopLatLng = rightTopLatLng;
    }

    public void updateRealRect(Projection projection) {
        Drawable drawable = isSelect ? selectDrawable : originDrawable;
        LatLng latLng = marker.getPosition();
        Point point = projection.toScreenLocation(latLng);
        int leftX = point.x - drawable.getIntrinsicWidth() / 2;
        int rightX = point.x + drawable.getIntrinsicWidth() / 2;
        int topY = point.y - drawable.getIntrinsicHeight();
        int bottomY = point.y;
        Point leftBottomPoint = new Point(leftX, bottomY);
        LatLng leftBottomLat = projection.fromScreenLocation(leftBottomPoint);
        Point rightTopPoint = new Point(rightX, topY);
        LatLng rightTopLatLng = projection.fromScreenLocation(rightTopPoint);
        this.leftBottomPoint = leftBottomPoint;
        this.rightTopPoint = rightTopPoint;
        this.leftBottomLat = leftBottomLat;
        this.rightTopLatLng = rightTopLatLng;
    }

    public boolean isCoordinateInRectangle(Point targetPoint, Point leftBottomPoint, Point rightTopPoint) {
        double minX = Math.min(leftBottomPoint.x, rightTopPoint.x);
        double maxX = Math.max(leftBottomPoint.x, rightTopPoint.x);
        double minY = Math.min(leftBottomPoint.y, rightTopPoint.y);
        double maxY = Math.max(leftBottomPoint.y, rightTopPoint.y);
        double targetX = targetPoint.x;
        double targetY = targetPoint.y;
        Log.e("isCoordinateInRectangle", "minX:" + minX + " maxX:" + maxX + " minY:" + minY + " maxY:" + maxY + " targetX:" + targetX + " targetY:" + targetY);
        return targetX >= minX && targetX <= maxX && targetY >= minY && targetY <= maxY;
    }
}
