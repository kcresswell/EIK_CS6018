package com.example.mcresswell.project01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

public class RV_Adapter extends RecyclerView.Adapter<RV_Adapter.ViewHolder> {
    private List<DashButton> m_btn_img_ListItems;
    private Context m_Context;

    private OnDataChannel m_dataListener;

    //constructor
    public RV_Adapter(List<DashButton> inputList) {
        m_btn_img_ListItems = inputList;
    }

    //viewholder that conncets to recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View itemLayout;
        protected ImageButton btn_image_itemData;

        public ViewHolder(@NonNull View view) {
            super(view);
            itemLayout = view;
            btn_image_itemData = (ImageButton) view.findViewById(R.id.btn_img_dashboard_item);
        }
    }

    @NonNull
    @Override
    public RV_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        m_Context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(m_Context);
        View view = layoutInflater.inflate(R.layout.dashboard_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        try {
            m_dataListener = (OnDataChannel) m_Context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(m_Context.toString() + " must implement OnDataChannel");
        }

        //set values of the button.
        viewHolder.btn_image_itemData.setImageDrawable(m_btn_img_ListItems.get(position).getImage());
        viewHolder.btn_image_itemData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                m_dataListener.onDataPass(position);
            }
        });
    }

    public interface OnDataChannel {
        void onDataPass (int position);
    }

    @Override
    public int getItemCount() {
        return m_btn_img_ListItems.size();
    }
}