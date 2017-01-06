package com.joelimyx.politicallocal.bills;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.joelimyx.politicallocal.R;
import com.joelimyx.politicallocal.bills.gson.Bill;
import com.joelimyx.politicallocal.database.RepsSQLHelper;
import com.joelimyx.politicallocal.reps.MyRep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 12/20/16.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private Context mContext;
    private List<Bill> mBillList;
    private VotersAdapter mVotersAdapter;
    private OnBillItemSelectedListener mListener;
    private String mFilter;

    private static final String TAG = "BillAdapter";

    interface OnBillItemSelectedListener {
        /**
         * Call back from Adapter to fragment to start the DetailBillActivity
         * @param billId bill number in the format of hr123
         */
        void onBillItemSelected(String billId);
    }

    public BillAdapter(List<Bill> billList, Context context, OnBillItemSelectedListener listener, String filter) {
        mBillList = billList;
        mListener = listener;
        mContext = context;
        mFilter = filter;
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
        holder.mBillTitle.setText(Html.fromHtml(current.getTitle()));
        String billId = mBillList.get(position).getNumber().replace(".","");
        holder.mBillItem.setOnClickListener(view -> {
            mListener.onBillItemSelected(billId);
        });

        if (mFilter.equals("passed")) {
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mVotersAdapter = new VotersAdapter(new ArrayList<>(), mContext);
            holder.mRecyclerView.setAdapter(mVotersAdapter);
            // TODO: 1/5/17 Work on votes once there is more bills passed
//            getVotes("house",billId.toLowerCase());
//            getVotes("senate",billId.toLowerCase());
        }
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }

    void swapData(List<Bill> newBills, String filter){
        mFilter = filter;
        mBillList.clear();
        mBillList = newBills;
        notifyDataSetChanged();
    }

    void addData(List<Bill> newBills){
        int temp = mBillList.size();
        mBillList.addAll(newBills);
        notifyItemRangeInserted(temp,newBills.size());
    }

    private void getVotes(String chamber, String billId){
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://congress.api.sunlightfoundation.com/votes?bill_id="+billId+"-115&order=voted_at__desc&chamber="+chamber+"&fields=voter_ids";

        List<MyRep> myReps = RepsSQLHelper.getInstance(mContext).getRepsList();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response1 -> {
                    Log.i(TAG, chamber+": "+billId);
                    try {
                        JSONArray result = response1.getJSONArray("results");
                        if (result.length()>0) {
                            JSONObject recent = (JSONObject) result.get(0);
                            JSONObject voters = recent.getJSONObject("voter_ids");
                            for (MyRep myRep : myReps) {
                                if (myRep.getChamber().equalsIgnoreCase(chamber)) {
                                    if (voters.has(myRep.getBioId())) {
                                        Log.d(TAG, "getVotes() called with: bioId = [" + myRep.getBioId() + "]"+", votes = ["+voters.optString(myRep.getBioId())+"]");
                                        mVotersAdapter.addVote(new Voter(myRep.getBioId(),voters.optString(myRep.getBioId())));
                                    }
                                }
                            }
                        }else {
                            for (MyRep myRep : myReps) {
                                if (myRep.getChamber().equalsIgnoreCase("chamber")){
                                    Log.d(TAG, myRep.getBioId()+": empty");
                                    mVotersAdapter.addVote(new Voter(myRep.getBioId(),"Yea"));
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);

        queue.add(request);

    }

    class BillViewHolder extends RecyclerView.ViewHolder{
        private TextView mBillNumber, mBillTitle;
        private LinearLayout mBillItem;
        private RecyclerView mRecyclerView;

        BillViewHolder(View itemView) {
            super(itemView);
            mBillNumber = (TextView) itemView.findViewById(R.id.list_bill_number);
            mBillTitle= (TextView) itemView.findViewById(R.id.list_bill_title);
            mBillItem= (LinearLayout) itemView.findViewById(R.id.item_bill);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.voter_recyclerview);
        }
    }
}
