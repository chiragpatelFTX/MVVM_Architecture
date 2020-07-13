package com.ftx.mvvm_template.binding;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;
import com.ftx.mvvm_template.views.adapters.AlbumAdapter;
import com.ftx.mvvm_template.views.adapters.UserAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BindingUtils {

    @BindingAdapter("adapter")
    public static void bindAdapter(RecyclerView view, RecyclerView.Adapter<RecyclerView.ViewHolder> baseAdapter) {
        //view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setAdapter(baseAdapter);
    }

    @BindingAdapter("adapterList")
    public static void bindAdapterList(RecyclerView view, PagedList albumList) {
        if (view.getAdapter() instanceof AlbumAdapter) {
            if (albumList != null && albumList.size() > 0 && albumList.get(0) instanceof AlbumModel)
                ((AlbumAdapter) view.getAdapter()).submitList(albumList);
        } else if (view.getAdapter() instanceof UserAdapter) {
            if (albumList != null && albumList.size() > 0 && albumList.get(0) instanceof UserModel)
                ((UserAdapter) view.getAdapter()).submitList(albumList);
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

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url == null || url.isEmpty()) return;
        Glide.with(view.getContext()).load(url).into(view);
    }
}
