package com.joelimyx.politicallocal.bills;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.joelimyx.politicallocal.R;

/**
 * Created by Joe on 1/1/17.
 */

public class FilterDialogFragment extends DialogFragment {
    public static final String ARG_CHAMBER = "chamber";
    public static final String ARG_FILTER = "filter";

    private static final String TAG = "FilterDialogFragment";

    private String mChamber, mFilter;
    private OnDialogPositiveClickedListener mListener;

    interface OnDialogPositiveClickedListener{

        /**
         * Pass data from the spinner to the bill fragment to be filtered
         * @param chamber chamber of congress
         * @param filter three different type of filter: "introduced", "passed", "updated"
         */
        void OnDialogPositiveClicked(String chamber, String filter);
    }

    public FilterDialogFragment() {
    }

    public static FilterDialogFragment newInstance(String chamber, String filter) {

        Bundle args = new Bundle();
        args.putString(ARG_CHAMBER, chamber);
        args.putString(ARG_FILTER, filter);

        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            mChamber = getArguments().getString(ARG_CHAMBER);
            mFilter= getArguments().getString(ARG_FILTER);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(),R.layout.fragment_dialog_bill_filter,null);

        MaterialSpinner chamberSpinner = (MaterialSpinner) view.findViewById(R.id.chamber_spinner);
        MaterialSpinner filterSpinner= (MaterialSpinner) view.findViewById(R.id.filter_spinner);
        chamberSpinner.setItems("House", "Senate");
        filterSpinner.setItems("Introduced", "Passed", "Updated");

        if (mChamber.equalsIgnoreCase("senate")){
            chamberSpinner.setSelectedIndex(1);
        }

        if (mFilter.equalsIgnoreCase("passed")){
            filterSpinner.setSelectedIndex(1);
        }else if (mFilter.equalsIgnoreCase("updated")){
            filterSpinner.setSelectedIndex(2);
        }

        return new AlertDialog.Builder(getContext())
                .setTitle("Filter")
                .setView(view)
                .setPositiveButton("Filter", (dialog, which) -> {
                    String chamber = String.valueOf(chamberSpinner.getText()).toLowerCase(),
                            filter = String.valueOf(filterSpinner.getText()).toLowerCase();

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.bill_filter), Context.MODE_PRIVATE).edit();
                    editor.putString(getString(R.string.chamber),chamber);
                    editor.putString(getString(R.string.filter),filter);
                    editor.commit();

                    mListener.OnDialogPositiveClicked(chamber, filter);
                    dismiss();
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    public void setListener(OnDialogPositiveClickedListener listener){
        mListener = listener;
    }
}
