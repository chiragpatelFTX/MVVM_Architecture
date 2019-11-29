package com.sql.projecttemplate.views.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sql.projecttemplate.R;
import com.sql.projecttemplate.databinding.AlbumAdapterBinding;
import com.sql.projecttemplate.model.db.models.AlbumModel;
import com.sql.projecttemplate.utils.Constants;
import com.sql.projecttemplate.views.activities.DetailActivity;


/**
 * Name : UserAdapter
 * <br> Purpose : To display list of Album in RecyclerView
 *
 * @PagedListAdapter used for pagination using google's Paging library
 */

public class AlbumAdapter extends PagedListAdapter<AlbumModel, AlbumAdapter.ViewHolderUserAdapter> {

    /**
     * Name : DIFF_CALLBACK
     * <br> Purpose : DiffCallback will check weather your data is changed or not if yes than return true other wise return
     * false and according to this your adapter will update your record or insert new record in recycler view.
     */
    public static final DiffCallback<AlbumModel> ALBUM_DIFF_CALLBACK = new DiffCallback<AlbumModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull AlbumModel oldAlbumModel, @NonNull AlbumModel newAlbumModel) {
            return oldAlbumModel.userId == newAlbumModel.userId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AlbumModel oldAlbumModel, @NonNull AlbumModel newAlbumModel) {
            return oldAlbumModel.equals(newAlbumModel);
        }
    };
    private Context mContext;

    public AlbumAdapter(Context mContext) {
        super(ALBUM_DIFF_CALLBACK);
        this.mContext = mContext;
    }

    @Override
    public ViewHolderUserAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        //Append "Binding" as a suffix to your Layout resource file name but make sure to remove '_' or any special character from Resource file name and than append 'Binding' as a suffix.
        //Inflate layout using DataBindingUtil
        AlbumAdapterBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.album_adapter, parent, false);

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
