package com.joelimyx.politicallocal.bills;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.gson.Bill;

import java.util.List;

/**
 * Created by Joe on 12/20/16.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    List<Bill> mBillList;
    private OnBillItemSelectedListener mListener;

    interface OnBillItemSelectedListener {
        void onBillItemSelected(String billId);
    }

    public BillAdapter(List<Bill> billList, OnBillItemSelectedListener listener) {
        mBillList = billList;
        mListener = listener;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_bill,parent,false));
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, final int position) {
        Bill current = mBillList.get(position);
        holder.mBillNumber.setText(current.getNumber());
        holder.mBillTitle.setText(current.getTitle());
        holder.mBillItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billId = mBillList.get(position).getNumber().replace(".","");
                mListener.onBillItemSelected(billId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder{
        private TextView mBillNumber, mBillTitle;
        private LinearLayout mBillItem;

        public BillViewHolder(View itemView) {
            super(itemView);
            mBillNumber = (TextView) itemView.findViewById(R.id.list_bill_number);
            mBillTitle= (TextView) itemView.findViewById(R.id.list_bill_title);
            mBillItem= (LinearLayout) itemView.findViewById(R.id.item_bill);
        }
    }
}
