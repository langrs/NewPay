package com.unioncloud.newpay.presentation.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;
import com.unioncloud.newpay.presentation.ui.cart.views.QuantityAdapter;

/**
 * Created by cwj on 16/9/3.
 */
public class SettingsFragment extends StatedFragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    private Spinner printCountSpinner;
    private TextView currentSnTv;
    private Button skipSnBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("设置");
        printCountSpinner = (Spinner) view.findViewById(R.id.fragment_settings_print_number);

        currentSnTv = (TextView) view.findViewById(R.id.fragment_settings_current_sn);
        currentSnTv.setText(getString(R.string.Settings_Current_SN, PreferencesUtils.getOrderSerialNumber(getContext())));
        skipSnBtn = (Button) view.findViewById(R.id.fragment_settings_skip_sn_button);
        skipSnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastSn = PreferencesUtils.skipOrderSerialNumber(getContext());
                currentSnTv.setText(getString(R.string.Settings_Current_SN, lastSn));
            }
        });

        printCountSpinner.setAdapter(new QuantityAdapter(3));
        int printCount = PreferencesUtils.getPrintCount(getContext());
        printCountSpinner.setSelection(printCount - 1);
        printCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferencesUtils.setPrintCount(getContext(), position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
