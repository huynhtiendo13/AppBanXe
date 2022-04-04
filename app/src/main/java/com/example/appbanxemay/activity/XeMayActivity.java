package com.example.appbanxemay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;


import com.example.appbanxemay.R;
import com.example.appbanxemay.adapter.XeMayAdapter;
import com.example.appbanxemay.model.SanPhamMoi;
import com.example.appbanxemay.retrofit.ApiBanHang;
import com.example.appbanxemay.retrofit.RetrofitClient;
import com.example.appbanxemay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XeMayActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page =1;
    int idsanpham;
    XeMayAdapter adapterXm;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xe_may);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        idsanpham = getIntent().getIntExtra("idsanpham",1);
        linkViews();
        ActionToolBar();
        getData(page);
        addEventLoading();

    }

    private void addEventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false  ){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.add(null);
                adapterXm.notifyItemInserted(sanPhamMoiList.size()-1);

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                adapterXm.notifyItemRemoved(sanPhamMoiList.size());
                page = page +1;
                getData(page);
                adapterXm.notifyDataSetChanged();
                isLoading = false;

            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,idsanpham)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        if (adapterXm == null){
                            sanPhamMoiList = sanPhamMoiModel.getResult();
                            adapterXm = new XeMayAdapter(getApplicationContext(),sanPhamMoiList);
                            recyclerView.setAdapter(adapterXm);
                        }else{
                            int vitri = sanPhamMoiList.size()-1;
                            int soluongadd = sanPhamMoiModel.getResult().size();
                            for(int i =0;i<soluongadd;i++){
                                sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));

                            }
                            adapterXm.notifyItemRangeInserted(vitri,soluongadd);
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"Hết dữ liệu rồi",Toast.LENGTH_LONG).show();
                        isLoading = true;
                    }

                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), "Không kết nối server",Toast.LENGTH_LONG ).show();
                }
        ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void linkViews() {
        toolbar = findViewById(R.id.toolbarxemay);
        recyclerView = findViewById(R.id.recycleview_xm);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}