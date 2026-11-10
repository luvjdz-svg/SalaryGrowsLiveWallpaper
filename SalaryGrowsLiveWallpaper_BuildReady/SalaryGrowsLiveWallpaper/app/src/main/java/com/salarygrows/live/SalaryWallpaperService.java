package com.salarygrows.live;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.graphics.*;
import android.os.*;
import android.content.SharedPreferences;
import java.text.NumberFormat;
import java.util.Locale;

public class SalaryWallpaperService extends WallpaperService {
    @Override public Engine onCreateEngine() { return new SalaryEngine(); }
    private class SalaryEngine extends Engine {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private boolean visible = false;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final NumberFormat fmt = NumberFormat.getNumberInstance(Locale.KOREA);
        private final Runnable tick = new Runnable() { @Override public void run() { if(visible) { draw(); schedule(); } } };
        private void schedule() {
            handler.removeCallbacks(tick);
            long now = System.currentTimeMillis();
            handler.postDelayed(tick, 1000 - (now % 1000));
        }
        @Override public void onVisibilityChanged(boolean shown) { visible=shown; handler.removeCallbacks(tick); if(shown){draw();schedule();} }
        @Override public void onSurfaceChanged(SurfaceHolder holder,int format,int width,int height) { super.onSurfaceChanged(holder,format,width,height); if(visible) draw(); }
        @Override public void onDestroy() { visible=false; handler.removeCallbacks(tick); super.onDestroy(); }
        private void text(Canvas c, String s, float x, float y, float size, int color, boolean bold) {
            paint.setColor(color); paint.setTextSize(size); paint.setTypeface(bold ? Typeface.create("sans-serif",Typeface.BOLD) : Typeface.create("sans-serif",Typeface.NORMAL));
            paint.setTextAlign(Paint.Align.CENTER); c.drawText(s,x,y,paint);
        }
        private void draw() {
            SurfaceHolder holder = getSurfaceHolder(); Canvas c=null;
            try {
                c=holder.lockCanvas(); if(c==null) return;
                int w=c.getWidth(),h=c.getHeight();
                c.drawColor(Color.rgb(12,18,30));
                float cx=w/2f, center=h*.46f;
                SharedPreferences p=getSharedPreferences("salary",MODE_PRIVATE);
                long monthly=p.getLong("monthly",2900000L); int day=p.getInt("day",15);
                double[] values=SalaryMath.calculate(monthly,day,System.currentTimeMillis());
                text(c,"월급이 자란다",cx,center-115,Math.min(w*.065f,32),0xFFB8C7DB,false);
                String amount=fmt.format((long)Math.floor(values[0]))+"원";
                float amountSize=Math.min(w*.10f,58);
                paint.setTypeface(Typeface.DEFAULT_BOLD); paint.setTextSize(amountSize);
                while(paint.measureText(amount)>w*.90f && amountSize>18) {amountSize-=2;paint.setTextSize(amountSize);}
                text(c,amount,cx,center,amountSize,Color.WHITE,true);
                text(c,"+"+String.format(Locale.KOREA,"%.4f",values[1])+"원 / 초",cx,center+60,Math.min(w*.05f,25),0xFF76E5AD,false);
                text(c,"매월 "+day+"일 급여 · "+fmt.format(monthly)+"원",cx,center+105,Math.min(w*.037f,19),0xFFB8C7DB,false);
            } catch(Exception ignored) { } finally { if(c!=null) {try{holder.unlockCanvasAndPost(c);}catch(Exception ignored){}} }
        }
    }
}
