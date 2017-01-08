package com.joelimyx.politicallocal.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.search.gson.Result;

import java.util.List;

/**
 * Created by Joe on 1/5/17.
 */

public class ResultBillAdapter extends RecyclerView.Adapter<ResultBillAdapter.ResultViewHolder> {
    private List<Result> mBillsList;
    private OnResultBillSelectedListener mListener;

    interface OnResultBillSelectedListener{
        void OnResultBillSelected(String billId, long congress);
    }

    public ResultBillAdapter(List<Result> billsList, OnResultBillSelectedListener listener) {
        mBillsList = billsList;
        mListener = listener;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_bill,parent,false));
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        String type = getBillType(mBillsList.get(position).getBillType());
        String bill = mBillsList.get(position).getBillType()+(int) mBillsList.get(position).getNumber();
        holder.mBillNumber.setText(String.format("%s%d", type, mBillsList.get(position).getNumber()));
        holder.mBillTitle.setText(mBillsList.get(position).getOfficialTitle());
        holder.mBillItem.setOnClickListener(v-> mListener.OnResultBillSelected(bill,mBillsList.get(position).getCongress()));
    }

    private String getBillType(String type){
        StringBuilder holder = new StringBuilder(type);
        StringBuilder builder = new StringBuilder();
        if (type.contains("res")) {
            builder.append("Res.");
            holder.replace(holder.indexOf("res"),type.length(),"");
        }
        if (type.contains("con")){
            builder.insert(0,"Con.");
            holder.replace(holder.indexOf("con"),holder.length(),"");
        }
        for (int i = holder.length(); i > 0; i--) {
            holder.insert(i,".");
        }
        builder.insert(0,holder.toString().toUpperCase());

        return builder.toString();
    }
    @Override
    public int getItemCount() {
        return mBillsList.size()>=5 ? 5: mBillsList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{

        private TextView mBillNumber, mBillTitle;
        private LinearLayout mBillItem;

        public ResultViewHolder(View itemView) {
            super(itemView);
            mBillNumber = (TextView) itemView.findViewById(R.id.list_bill_number);
            mBillTitle = (TextView) itemView.findViewById(R.id.list_bill_title);
            mBillItem = (LinearLayout) itemView.findViewById(R.id.item_bill);
        }
    }
}
