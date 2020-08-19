package com.ftx.mvvm_template.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.AlbumAdapterBinding;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.utils.AppLog;
import com.ftx.mvvm_template.utils.Constants;
import com.ftx.mvvm_template.views.activities.DetailActivity;


/**
 * Name : UserAdapter
 * <br> Purpose : To display list of Album in RecyclerView
 *
 * @PagedListAdapter used for pagination using google's Paging library
 */

public class AlbumAdapter extends PagedListAdapter<AlbumModel, AlbumAdapter.ViewHolderUserAdapter> {

    private Context mContext;

    public AlbumAdapter(Context mContext) {
        super(AlbumModel.ALBUM_DIFF_CALLBACK);
        this.mContext = mContext;
    }
    @Override
    public ViewHolderUserAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        //Append "Binding" as a suffix to your Layout resource file name but make sure to remove '_' or any special character from Resource file name and than append 'Binding' as a suffix.
        //Inflate layout using DataBindingUtil
        AlbumAdapterBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.album_adapter, parent, false);

        return new ViewHolderUserAdapter(viewDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderUserAdapter holder, int position) {

        final AlbumModel mAlbumModel = getItem(position);
        if (mAlbumModel != null) {
            holder.bind(mAlbumModel);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            AlbumModel albumModel = getItem(pos);
            if (albumModel != null) {
                Intent mIntent = new Intent(view.getContext(), DetailActivity.class);
                mIntent.putExtra(Constants.IntentKey.KEY_ALBUMID, albumModel.getId());
                view.getContext().startActivity(mIntent);
            }
        });
    }

    public class ViewHolderUserAdapter extends RecyclerView.ViewHolder {

        private final AlbumAdapterBinding binding;

        public ViewHolderUserAdapter(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull AlbumModel albumModel) {
            //set AlbumModel class for set values automatically from xml
            binding.setAlbumModel(albumModel);
            binding.executePendingBindings();
        }
    }
}
