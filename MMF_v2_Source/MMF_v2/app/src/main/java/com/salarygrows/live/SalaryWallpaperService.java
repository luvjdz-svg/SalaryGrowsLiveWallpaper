package com.salarygrows.live;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.graphics.*;
import android.os.*;
import android.content.SharedPreferences;
import java.text.NumberFormat;
import java.util.Locale;
public class SalaryWallpaperService extends WallpaperService {
 @Override public Engine onCreateEngine(){return new E();}
 private class E extends Engine {
  Handler handler=new Handler(Looper.getMainLooper()); boolean visible=false;
  Paint p=new Paint(3); NumberFormat fmt=NumberFormat.getNumberInstance(Locale.KOREA);
  Runnable tick=new Runnable(){public void run(){if(visible){render();schedule();}}};
  void schedule(){handler.removeCallbacks(tick);handler.postDelayed(tick,1000-System.currentTimeMillis()%1000);}
  @Override public void onVisibilityChanged(boolean v){visible=v;handler.removeCallbacks(tick);if(v){render();schedule();}}
  @Override public void onSurfaceChanged(SurfaceHolder h,int f,int w,int he){super.onSurfaceChanged(h,f,w,he);if(visible)render();}
  @Override public void onDestroy(){visible=false;handler.removeCallbacks(tick);super.onDestroy();}
  void render(){SurfaceHolder h=getSurfaceHolder();Canvas c=null;
   try{c=h.lockCanvas();if(c==null)return;int w=c.getWidth(),height=c.getHeight();c.drawColor(0xFF101719);
    SharedPreferences s=getSharedPreferences("salary",MODE_PRIVATE);
    double[] v=SalaryMath.calculate(s.getLong("monthly",2900000),s.getInt("day",15),System.currentTimeMillis());
    String number=fmt.format((long)Math.floor(v[0]));
    float x=w*s.getInt("x",50)/100f,y=height*s.getInt("y",46)/100f;
    float size=Math.min(w*.11f,72)*s.getInt("size",100)/100f;
    p.setTypeface(Typeface.create("sans-serif-medium",Typeface.NORMAL));p.setTextAlign(Paint.Align.CENTER);p.setColor(Color.WHITE);
    p.setTextSize(size);while(p.measureText(number)>w*.94f&&size>12){size-=1;p.setTextSize(size);}c.drawText(number,x,y,p);
   }catch(Exception ignored){}finally{if(c!=null)try{h.unlockCanvasAndPost(c);}catch(Exception ignored){}}
  }
 }
}
