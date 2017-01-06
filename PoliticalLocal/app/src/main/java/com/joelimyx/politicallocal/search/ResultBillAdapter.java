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
    private List<Result> mNewsResult;
    private OnResultBillSelectedListener mListener;

    interface OnResultBillSelectedListener{
        void OnResultBillSelected(String billId);
    }

    public ResultBillAdapter(List<Result> newsResult, OnResultBillSelectedListener listener) {
        mNewsResult = newsResult;
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
        String bill = mNewsResult.get(position).getBillType()+(int)mNewsResult.get(position).getNumber();
        holder.mBillNumber.setText(bill);
        holder.mBillTitle.setText(mNewsResult.get(position).getOfficialTitle());
        holder.mBillItem.setOnClickListener(v-> mListener.OnResultBillSelected(bill));
    }

    @Override
    public int getItemCount() {
        return mNewsResult.size()>=5 ? 5:mNewsResult.size();
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
