package com.ayaz.assignment.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ayaz.assignment.R;

public class InfoBoxDialog {
    public InfoBoxDialog(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box_info);
        dialog.show();

        Button btn_close = dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
