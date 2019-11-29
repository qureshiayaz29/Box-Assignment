package com.ayaz.assignment.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayaz.assignment.R;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;

public class DisplayBoxDialog {
    public DisplayBoxDialog(Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box_list);
        dialog.show();

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Button btn_close = dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ArrayList<Box> boxArrayList = BoxOperation.fetchBoxes(context);
        SlimAdapter.create()
                .attachTo(recyclerView)
                .updateData(boxArrayList)
                .register(R.layout.box_item, new SlimInjector<Box>() {
                    @Override
                    public void onInject(Box data, IViewInjector injector) {
                        injector.text(R.id.txt_name, "Box "+data.getIndex())
                                .text(R.id.txt_scale, data.getScale()+"x")
                                .text(R.id.txt_x, ""+data.getX())
                                .text(R.id.txt_y, ""+data.getY());
                    }
                });
    }
}
