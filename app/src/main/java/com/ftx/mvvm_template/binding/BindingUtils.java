package com.ftx.mvvm_template.binding;

import android.arch.paging.PagedList;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.views.adapters.AlbumAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BindingUtils {

    @BindingAdapter("adapter")
    public static void bindAdapter(RecyclerView view, RecyclerView.Adapter<RecyclerView.ViewHolder> baseAdapter) {
        view.setAdapter(baseAdapter);
    }

    @BindingAdapter("adapterAlbumList")
    public static void bindAdapterAlbumList(RecyclerView view, PagedList albumList) {
        if (view.getAdapter() instanceof AlbumAdapter) {
            if (albumList != null && albumList.size() > 0 && albumList.get(0) instanceof AlbumModel)
                ((AlbumAdapter) view.getAdapter()).setList(albumList);
        }
    }

    @BindingAdapter("age")
    public static void setAge(TextView textView, String dob) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Calendar today = Calendar.getInstance();
            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(format.parse(dob));

            int age = today.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR) && age > 0) {
                age--;
            }

            textView.setText(String.valueOf(age));
        } catch (Exception ignored) {
        }
    }
}
