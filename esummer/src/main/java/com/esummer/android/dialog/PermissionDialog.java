package com.esummer.android.dialog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esummer.android.R;
import com.esummer.android.fragment.BaseFragment;

/**
 * Created by cwj on 16/7/11.
 */
public class PermissionDialog extends BaseFragment {

    private int permissionMessageStrId;

    public static PermissionDialog newInstance(@StringRes int permissionMessage) {
        PermissionDialog permissionFragment = new PermissionDialog();
        permissionFragment.permissionMessageStrId = permissionMessage;
        return permissionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.permissionMessageStrId = savedInstanceState.getInt("KEY_PERMISSION_MESSAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permisions_dialog, container);
        TextView messageTv = (TextView) view.findViewById(R.id.fragment_permissions_dialog_message);
        TextView okBtn = (TextView) view.findViewById(R.id.fragment_permissions_dialog_button_yes);
        TextView noBtn = (TextView) view.findViewById(R.id.fragment_permissions_dialog_button_no);
        messageTv.setText(permissionMessageStrId);
        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                try {
                    intent.setData(Uri.parse("package:" + getContext().getPackageName()));
                    startActivity(intent);
                    dismiss();
                } catch (ActivityNotFoundException var2) {
                    Log.e("PermissionsDialog", "Can\'t start application settings");
                    dismiss();
                }
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("KEY_PERMISSION_MESSAGE", this.permissionMessageStrId);
    }
}
