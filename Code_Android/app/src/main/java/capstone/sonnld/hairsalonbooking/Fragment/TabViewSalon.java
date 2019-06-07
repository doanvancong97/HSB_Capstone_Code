package capstone.sonnld.hairsalonbooking.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.model.Salon;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewServiceAdapter;


public class TabViewSalon extends Fragment {

    List<Salon> salonList;
    List<SalonService> salonServiceList;




    ImageView mAd1, mAd2, mAd3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tab_view_salon, container, false);

//        mAd1 = view.findViewById(R.id.ad_1);
//        mAd2 = view.findViewById(R.id.ad_2);
//        mAd3 = view.findViewById(R.id.ad_3);
//        Picasso.with(getActivity())
//                .load("https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Liem-Barber-Shop-1.jpg")
//                .into(mAd1);
//        Picasso.with(getActivity())
//                .load("https://cdn.jamja.vn/blog/wp-content/uploads/2017/12/dich-vu-tai-Liem-Barber-Shop-1.jpg")
//                .into(mAd2);
//        Picasso.with(getActivity())
//                .load("https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Mekong-Barbershop.jpg")
//                .into(mAd3);


        String des1= "ÁP DỤNG KHI DÙNG DỊCH VỤ TẠI CỬA HÀNG* \n" +
                "\n" +
                "- Giảm 20% tổng hóa đơn áp dụng cho tất cả các dịch vụ \n" +
                "- Áp dụng cho khách hàng nữ \n" +
                "- Mỗi mã ưu đãi đổi được nhiều suất trong suốt chương trình \n" +
                "- Khách hàng có thể lấy nhiều mã trong suốt chương trình \n" +
                "\n" +
                "THỜI GIAN ÁP DỤNG \n" +
                "- Khung giờ: 9h30 - 19h00\t\n" +
                "- Áp dụng tất cả các ngày trong tuần \n" +
                "- Không áp dụng các ngày lễ, Tết: 30/4, 1/5 \n" +
                "\n" +
                "Chi tiết địa điểm xem tại \"Điểm áp dụng\" \n" +
                "\n" +
                "Vui lòng bấm XÁC NHẬN ĐẶT CHỖ để nhận mã giảm giá \n" +
                "\n" +
                "LƯU Ý \n" +
                "- Chương trình chỉ áp dụng với khách dùng dịch vụ tại cửa hàng \n" +
                "- Không áp dụng đồng thời với các chương trình khác của MIA.Nails & Cafe \n" +
                "- Không áp dụng phụ thu \n" +
                "- Ưu đãi chưa bao gồm VAT \n" +
                "- Khách hàng được phép đến sớm hoặc muộn hơn 15 phút so với giờ hẹn đến \n" +
                "- Mã giảm giá không có giá trị quy đổi thành tiền mặt ";

        String shop1 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/4RAU-Barber-SHOP.jpg";
        String shop2 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Tiem-Barber-Shop-Vu-Tri.jpg";
        String shop3 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Tony-Barber-House.jpg";
        String shop4 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Tiem-Barber-Shop-Vu-Tri-2.jpg";

        salonList = new ArrayList<>();
        salonList.add(new Salon("Giảm giá 30% toàn bộ dịch vụ","509 Quang Trung, HCM",des1, shop1,"shop1Service"));
        salonList.add(new Salon("Giảm giá 20% toàn bộ dịch vụ","69 Trần Duy Hưng, HN", des1,shop2,"shop2Service"));
        salonList.add(new Salon("Tony Barber","1050 Nguyễn Oanh, HCM", des1,shop3));
        salonList.add(new Salon("Paris Hair Salon","123 Gò Vấp, HCM", des1,shop4));

        // recycler view for salon
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_salon);
        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(getActivity(), salonList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        recyclerView.setAdapter(viewAdapter);


        salonServiceList = new ArrayList<>();
        salonServiceList.add(new SalonService("Gội Đầu",R.drawable.hair_wash));
        salonServiceList.add(new SalonService("Đắp mặt nạ",R.drawable.beauty1));
        salonServiceList.add(new SalonService("Tấy tóc",R.drawable.glue));
        salonServiceList.add(new SalonService("Kid combo",R.drawable.kid));
        salonServiceList.add(new SalonService("Uốn cao cấp",R.drawable.hair_curl));
        salonServiceList.add(new SalonService("Nhuộm Echosline",R.drawable.dye));
        //recycler view for service
        RecyclerView recyclerViewService = view.findViewById(R.id.recycler_view_service);
        RecyclerViewServiceAdapter recyclerViewServiceAdapter = new RecyclerViewServiceAdapter(getActivity(),salonServiceList);
        recyclerViewService.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerViewService.setAdapter(recyclerViewServiceAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
