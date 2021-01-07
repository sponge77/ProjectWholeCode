package com.example.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

/* gridview adapter는 내가 첫 주에 만든 custom adapter와 비슷하다.
같은 ClothesItem 객체(옷 이미지인 이미지뷰와, 옷 설명인 텍스트뷰로 구성)를 사용하는 공통점이 있다.

그러나 전자는 이미지뷰만 필요하므로, 그리드뷰에 이미지만 보이게 getView 설정해주었다. */

public class GridViewCustomAdapter extends BaseAdapter {
    private ArrayList<ClothesItem> gridItems = new ArrayList<>();

    public void addItem(ClothesItem item){
        gridItems.add(item);
    }

    public void deleteItem(Integer position) { gridItems.remove(position); }

    @Override
    public int getCount(){
        return gridItems.size();
    }

    // 내가 아이템을 선택했을 때 그 위치에 해당하는 아이템을 가져온다.
    @Override
    public Object getItem(int position){
        return gridItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CustomViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,null,false);

            holder = new CustomViewHolder();
            ImageView img = convertView.findViewById(R.id.grid_view_image_view);

            img.setMaxWidth(320);
            img.setMaxHeight(240);
            holder.iv = (ImageView) img;

            convertView.setTag(holder);
        }
        else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        ClothesItem ci = gridItems.get(position);

        holder.iv.setImageResource(ci.getResId());

        return convertView;
    }

    class CustomViewHolder{
        ImageView iv;
    }


}
