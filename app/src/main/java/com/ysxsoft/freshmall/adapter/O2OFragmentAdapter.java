package com.ysxsoft.freshmall.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.modle.NearByMallBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.view.O2OMallDetailActivity;
import com.ysxsoft.freshmall.view.O2OMoreClassifyListActivity;
import com.ysxsoft.freshmall.widget.HomeGoodsBanner;
import com.ysxsoft.freshmall.widget.MyRecyclerView;
import com.ysxsoft.freshmall.widget.banner.Banner;
import com.ysxsoft.freshmall.widget.banner.GlideImageLoader;
import com.ysxsoft.freshmall.widget.banner.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class O2OFragmentAdapter extends RecyclerView.Adapter implements OnBannerListener {

    private FragmentActivity context;
    public List<NearByMallBean.DataBean> data;
    public List<GetTypeListBean.DataBean> dataBeans;
    private String picurl;
    private final int GRIDE_VIEW_TYPE = 0;//八宫格
    private final int BANNER_VIEW_TYPE = 1;//banner
    private final int NORMAL_VIEW_TYPE = 2;//正常布局
    private OnGrideItemClickListener onGrideItemClickListener;
    private OnNormalClickListener onNormalClickListener;
    private OnClick onClick;
    private ArrayList<String> urls=new ArrayList<>();
    public O2OFragmentAdapter(FragmentActivity activity, List<NearByMallBean.DataBean> data, List<GetTypeListBean.DataBean> dataBeans, String picurl) {
        this.context = activity;
        this.data = data;
        this.dataBeans = dataBeans;
        this.picurl = picurl;
        urls.add(picurl);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return GRIDE_VIEW_TYPE;
        } else if (position == 1) {
            return BANNER_VIEW_TYPE;
        } else {
            return NORMAL_VIEW_TYPE;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == GRIDE_VIEW_TYPE) {
            view = getView(R.layout.gride_layout);
            return new GridHolder(view);
        } else if (viewType == BANNER_VIEW_TYPE) {
            view = getView(R.layout.o2o_banner_layout);
            return new BannerHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.o2o_fragment_item_layout, null);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof GridHolder) {
            GridLayoutManager manager = new GridLayoutManager(context, 4);
            ((GridHolder) viewHolder).gridView.setLayoutManager(manager);
            O2OHeadviewAdapter adapter = new O2OHeadviewAdapter(context, dataBeans);
            ((GridHolder) viewHolder).gridView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    if (onGrideItemClickListener != null) {
                        onGrideItemClickListener.grideItemClick(position);
                    }
                }
            });
        } else if (viewHolder instanceof BannerHolder) {
            ((BannerHolder) viewHolder).vp_banner.setImages(urls)
                             .setImageLoader(new GlideImageLoader())
                            .setOnBannerListener(this)
                             .start();
            ((BannerHolder) viewHolder).ll_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick != null) {
                        onClick.click();
                    }
                }
            });
        } else {
            ImageLoadUtil.GlideGoodsImageLoad(context, data.get(i - 2).getImgfm(), ((MyViewHolder) viewHolder).img_tupian);
            ((MyViewHolder) viewHolder).tv_mall_name.setText(data.get(i - 2).getDname());
            ((MyViewHolder) viewHolder).tv_distance.setText(data.get(i - 2).getShowjl());
            ((MyViewHolder) viewHolder).tv_xiaoliang.setText("销量：" + data.get(i - 2).getXiaoliang());
            ((MyViewHolder) viewHolder).tv_price.setText(data.get(i - 2).getJunjia());
            ((MyViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNormalClickListener != null) {
                        onNormalClickListener.itemClick(i - 2);
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return data.size() + 2;
    }

    /**
     * 用来引入布局的方法
     */
    private View getView(int LayoutResId) {
        return View.inflate(context, LayoutResId, null);
    }

    @Override
    public void OnBannerClick(int position) {

    }

    /**
     * 八宫格ViewHolder
     */
    public static class GridHolder extends RecyclerView.ViewHolder {

        private final MyRecyclerView gridView;

        public GridHolder(View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.recyclerView);
        }
    }

    /**
     * 轮播的holder
     */
    public static class BannerHolder extends RecyclerView.ViewHolder {
        private final Banner vp_banner;
        private final LinearLayout ll_more;

        public BannerHolder(View itemView) {
            super(itemView);
            vp_banner = itemView.findViewById(R.id.vp_banner);
            ll_more = itemView.findViewById(R.id.ll_more);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView img_tupian;
        private final TextView tv_distance;
        private final TextView tv_price, tv_mall_name, tv_xiaoliang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_mall_name = itemView.findViewById(R.id.tv_mall_name);
            tv_xiaoliang = itemView.findViewById(R.id.tv_xiaoliang);


        }
    }

    public interface OnGrideItemClickListener {
        void grideItemClick(int position);
    }

    public void setOnGrideItemClickListener(OnGrideItemClickListener onGrideItemClickListener) {
        this.onGrideItemClickListener = onGrideItemClickListener;
    }

    public interface OnNormalClickListener {
        void itemClick(int position);
    }

    public void setOnNormalClickListener(OnNormalClickListener onNormalClickListener) {
        this.onNormalClickListener = onNormalClickListener;
    }

    public interface OnClick {
        void click();
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

}
