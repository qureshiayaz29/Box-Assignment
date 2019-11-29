package com.ayaz.assignment.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ayaz.assignment.R;

public class ExportBoxDialog {
    public ExportBoxDialog(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box_export);
        dialog.show();

        TextView box_string = dialog.findViewById(R.id.txt_boxes);
        Button close = dialog.findViewById(R.id.btn_close);
        Button export = dialog.findViewById(R.id.btn_export);

        final String box_json = BoxOperation.fetchBoxesString(context);
        box_string.setText(box_json);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, box_json);
                context.startActivity(Intent.createChooser(intent2, "Share via"));
            }
        });
    }
}
