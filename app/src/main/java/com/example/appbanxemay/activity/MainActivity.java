package com.example.appbanxemay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanxemay.R;
import com.example.appbanxemay.adapter.LoaiSpAdapter;
import com.example.appbanxemay.adapter.SanPhamMoiAdapter;
import com.example.appbanxemay.model.LoaiSp;
import com.example.appbanxemay.model.LoaiSpModel;
import com.example.appbanxemay.model.SanPhamMoi;
import com.example.appbanxemay.model.SanPhamMoiModel;
import com.example.appbanxemay.retrofit.ApiBanHang;
import com.example.appbanxemay.retrofit.RetrofitClient;
import com.example.appbanxemay.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbarmanhinhchinh;
    ViewFlipper viewFlipper;
    RecyclerView recyclerview;
    NavigationView navigationview;
    ListView listviewmanhinhchinh;
    DrawerLayout drawerlayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    ImageView imgsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        linkViews();
        ActionBar();

        if(isConnected(this)){

            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(), "Không có Internet, vui lòng kiểm tra kết nối của bạn",Toast.LENGTH_LONG).show();
        }

    }

    private void getEventClick() {
        listviewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                switch(i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent xemay = new Intent(getApplicationContext(), XeMayActivity.class);
                        xemay.putExtra("idsanpham", 1);
                        startActivity(xemay);
                        break;
                    case 2:
                        Intent phutung = new Intent(getApplicationContext(), XeMayActivity.class);
                        phutung.putExtra("idsanpham", 2);
                        startActivity(phutung);
                        break;
                    case 3:
                        Intent thongtin = new Intent(getApplicationContext(), ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case 4:
                        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;


                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        mangSpMoi = sanPhamMoiModel.getResult();
                        spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                        recyclerview.setAdapter(spAdapter);
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), "Không kết nối được với server"+throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaiSpModel -> {
                    if(loaiSpModel.isSuccess()){
                        mangloaisp =  loaiSpModel.getResult();
                        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                        listviewmanhinhchinh.setAdapter(loaiSpAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://cdn.honda.com.vn/motorbikes/November2020/5zg71nkmbdJNLOSaoyoY.jpg");
        mangquangcao.add("https://cdn.honda.com.vn/motorbikes/August2021/0iWnMT7utbGIadCX6CS0.jpg");
        mangquangcao.add("https://cdn.honda.com.vn/motorbikes/October2021/DBSFFeIyA2rDz3ymUWDL.jpg");
        mangquangcao.add("https://cdn.honda.com.vn/motorbikes/February2021/05XV52cyzKEv8jJyihVi.jpg");

        for(int i =0; i<mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbarmanhinhchinh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarmanhinhchinh.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarmanhinhchinh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerlayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void linkViews() {
        imgsearch = findViewById(R.id.imgsearch);
        toolbarmanhinhchinh = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        listviewmanhinhchinh = findViewById(R.id.listviewmanhinhchinh);
        navigationview = findViewById(R.id.navigationview);
        drawerlayout = findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });




    }
    private  boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) ||(mobile != null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}