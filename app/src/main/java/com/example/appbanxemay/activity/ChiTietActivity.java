package com.example.appbanxemay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanxemay.R;
import com.example.appbanxemay.model.GioHang;
import com.example.appbanxemay.model.SanPhamMoi;
import com.example.appbanxemay.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView txtten,txtgia,txtmota;
    Button btnthem;
    ImageView imgchitiet;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Utils.manggiohang.size() >0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i =0;i <Utils.manggiohang.size();i++){
                if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasanpham()) * Utils.manggiohang.get(i).getSoluong();
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if (flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiasanpham()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensanpham());
                gioHang.setHinhsp(sanPhamMoi.getHinhanhsanpham());
                Utils.manggiohang.add(gioHang);
            }

        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasanpham()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensanpham());
            gioHang.setHinhsp(sanPhamMoi.getHinhanhsanpham());
            Utils.manggiohang.add(gioHang);
        }
        badge.setText(String.valueOf(Utils.manggiohang.size()));
    }

    private void initData() {
        SanPhamMoi sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        txtten.setText(sanPhamMoi.getTensanpham());
        txtmota.setText(sanPhamMoi.getMotasanpham());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanhsanpham()).into(imgchitiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtgia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasanpham()))+"Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        txtten = findViewById(R.id.txtten);
        txtgia = findViewById(R.id.txtgia);
        txtmota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemspvaogio);
        spinner = findViewById(R.id.spinner);
        imgchitiet = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbarchitiet);
        badge = findViewById(R.id.menu_sl);
        if (Utils.manggiohang != null){
            badge.setText(String.valueOf(Utils.manggiohang.size()));
        }

    }
}