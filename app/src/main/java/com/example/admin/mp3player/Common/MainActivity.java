package com.example.admin.mp3player.Common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.admin.mp3player.Fragments.AblumFragment;
import com.example.admin.mp3player.Fragments.DanhSachFragment;
import com.example.admin.mp3player.Fragments.MusicFragment;
import com.example.admin.mp3player.Fragments.NgheSiFragment;
import com.example.admin.mp3player.Fragments.OnlineSearchFragment;
import com.example.admin.mp3player.Fragments.TheLoaiFragment;
import com.example.admin.mp3player.Fragments.ThuMucFragment;
import com.example.admin.mp3player.R;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public ViewPager vpMusic;
    public static LinearLayout linearLayout;
    public static TabLayout tabLayout;
    private FragmentAdapter mFragment ;
    public static FrameLayout frameLayout ;
    public static RelativeLayout Relay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fullAct();

        setContentView(R.layout.activity_main);
        initsView();
        setEvents();

    }


    private void initsView() {
        vpMusic = (ViewPager) findViewById(R.id.vp_music);
        tabLayout = (TabLayout) findViewById(R.id.tabLay) ;
        linearLayout = (LinearLayout) findViewById(R.id.linea);
        findViewById(R.id.online).setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.main) ;
        Relay = (RelativeLayout) findViewById(R.id.Relay) ;
    }

    private void fullAct() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void setEvents() {
        mFragment = new FragmentAdapter(getSupportFragmentManager());
        vpMusic.setAdapter(mFragment);
        tabLayout.setupWithViewPager(vpMusic);
        tabLayout.setSelectedTabIndicatorColor(Color.CYAN);
        tabLayout.getTabAt(0).setText("BÀI HÁT");
        tabLayout.getTabAt(1).setText("D.SÁCH PHÁT");
        tabLayout.getTabAt(2).setText("ALBUM");
        tabLayout.getTabAt(3).setText("NGHỆ SĨ");
        tabLayout.getTabAt(4).setText("THỂ LOẠI");
        tabLayout.getTabAt(5).setText("THƯ MỤC");
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#4DD0E1"));
        vpMusic.addOnPageChangeListener(this);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        OnlineSearchFragment  fragment = new OnlineSearchFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main, fragment) ;
        transaction.addToBackStack("AHIHI");
        transaction.commit();
    }


    private  class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new MusicFragment();
            }
            if(position == 1) {
                return new DanhSachFragment();
            }
            if(position == 2) {
                return new AblumFragment();
            }
            if(position == 3) {
                return new NgheSiFragment();
            }
            if(position == 4) {
                return new  TheLoaiFragment();
            }
            if(position == 5) {
                return new ThuMucFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        linearLayout.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
    }
}
