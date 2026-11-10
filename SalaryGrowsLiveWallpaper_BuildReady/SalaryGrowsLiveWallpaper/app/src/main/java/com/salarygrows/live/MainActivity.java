package com.salarygrows.live;

import android.app.*;
import android.app.WallpaperManager;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(12,18,30));
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setPadding(48,70,48,30);
        root.setBackgroundColor(Color.rgb(12,18,30));
        TextView title = new TextView(this); title.setText("월급이 자란다 LIVE"); title.setTextColor(Color.WHITE); title.setTextSize(27); root.addView(title);
        TextView note = new TextView(this); note.setText("화면이 켜져 있을 때 1초마다 금액이 증가합니다. 급여일 00:00을 새 급여기간의 시작으로 계산합니다."); note.setTextColor(0xFFB9C5D8); note.setTextSize(15); note.setPadding(0,22,0,26); root.addView(note);
        android.content.SharedPreferences prefs = getSharedPreferences("salary",MODE_PRIVATE);
        EditText salary = new EditText(this); salary.setHint("월급 (원)"); salary.setText(String.valueOf(prefs.getLong("monthly",2900000L))); salary.setTextColor(Color.WHITE); salary.setHintTextColor(0xFF9BA8BC); salary.setInputType(InputType.TYPE_CLASS_NUMBER); root.addView(salary);
        EditText day = new EditText(this); day.setHint("급여일 (1~31)"); day.setText(String.valueOf(prefs.getInt("day",15))); day.setTextColor(Color.WHITE); day.setHintTextColor(0xFF9BA8BC); day.setInputType(InputType.TYPE_CLASS_NUMBER); root.addView(day);
        Button save = new Button(this); save.setText("저장하고 라이브 배경화면 설정"); root.addView(save);
        save.setOnClickListener(v -> {
            try {
                long monthly = Long.parseLong(salary.getText().toString().trim());
                int payday = Integer.parseInt(day.getText().toString().trim());
                if(monthly <= 0 || monthly > 100000000000L || payday < 1 || payday > 31) throw new IllegalArgumentException();
                prefs.edit().putLong("monthly",monthly).putInt("day",payday).apply();
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,new android.content.ComponentName(this,SalaryWallpaperService.class));
                try { startActivity(intent); } catch(Exception ex) { startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)); }
            } catch(Exception ex) { Toast.makeText(this,"월급과 급여일을 올바르게 입력하세요.",Toast.LENGTH_LONG).show(); }
        });
        setContentView(root);
    }
}
