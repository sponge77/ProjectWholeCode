package com.example.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/*
 custom adapter 가 왜 필요한가?
 내가 카테고리 리스트 페이지에서 옷을 선택한 다음, 새로운 페이지에 그 아이템의 정보를 넘겨줘야 함 -> 이때 getItem 함수 사용
 그리고 리스트뷰에 아이템을 추가할 때 addItem 필요
 객체화한것
 */

public class CustomAdapter extends BaseAdapter implements Filterable {
    private ArrayList<ClothesItem> customList = new ArrayList<>(); // 이 리스트는 옷 전체 데이터를 담고있다.
    /* filteredCustomList는 customList의 복사본.
    검색결과에 따라 리스트뷰 목록이 바뀌므로, filteredCustomList가 기본 리스트가 된다.*/
    private ArrayList<ClothesItem> filteredCustomList = customList;

    Filter filter;

    @Override
    public int getCount(){
        return filteredCustomList.size();
    }

    // 내가 아이템을 선택했을 때 그 위치에 해당하는 아이템을 가져온다. - filteredCustomList로 처리함
    @Override
    public Object getItem(int position){
        return filteredCustomList.get(position);
    }


    public String getExplain(int position) {
        return filteredCustomList.get(position).getExplain();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CustomViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card,null,false);

            holder = new CustomViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iconItem);
            holder.content = (TextView) convertView.findViewById(R.id.explain);

            convertView.setTag(holder);
        }
        else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        // getView에 filter된 리스트를 보여주는 것으로..
        ClothesItem ci = filteredCustomList.get(position);

        holder.iv.setImageResource(ci.getResId());  // resource id로 이미지뷰에 설정
        holder.content.setText(ci.getExplain());

        return convertView;
    }

    @Override
    public Filter getFilter(){
        if(filter == null){
            filter = new ListFilter();
        }
        return filter;
    }

    // constraint에 따라서 데이터를 필터링한다.
    private class ListFilter extends Filter{

        /* 2개의 Protected method */

        // performFiltering은 FilterResult타입을 리턴한다. 이러면 바로 publishResults함수가 수행되면서 화면에 보여지는 원리임
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            // 만약 constraint가 null이면 기존의 list가 보여져야한다.
            if (constraint == null || constraint.length() == 0) {
                results.values = customList ;
                results.count = customList.size() ;
            } else {
                ArrayList<ClothesItem> itemList = new ArrayList<ClothesItem>() ;

                for (ClothesItem item : customList) {
                    // 옷 설명의 철자 비교: 대소문자 구별 안 해도 검색 되도록 모두 대문자로 바꾼다.
                    if (item.getExplain().toUpperCase().contains(constraint.toString().toUpperCase()) )
                    {
                        // 사용자가 검색한 것과 동일한 설명을 가진 아이템은 추가
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        /* UI에 필터링된 결과를 뿌려주는 함수.
         performFiltering함수 결과를 보여주기 위해 반드시 필요한 함수이다. */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // results를 받아와서 filtered data list를 업데이트한다.
            filteredCustomList = (ArrayList<ClothesItem>) results.values ;

            // notify해야 변경된게 반영이 된다.
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }

    }
    class CustomViewHolder{
        ImageView iv;
        TextView content;
    }

    public void addItem(ClothesItem ci){
        customList.add(ci);
    }

    public void clear(){
        customList.clear();
    }
}
