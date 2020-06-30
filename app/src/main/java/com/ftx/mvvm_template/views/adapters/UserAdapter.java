package com.ftx.mvvm_template.views.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftx.mvvm_template.R;
import com.ftx.mvvm_template.databinding.UserAdapterBinding;
import com.ftx.mvvm_template.model.db.models.UserModel;


/**
 * Name : UserAdapter
 * <br> Purpose : To display list of user in RecyclerView
 *
 * @PagedListAdapter used for pagination using google's Paging library
 */

public class UserAdapter extends PagedListAdapter<UserModel, UserAdapter.ViewHolderUserAdapter> {

    /**
     * Name : DIFF_CALLBACK
     * <br> Purpose : DiffCallback will check weather your data is changed or not if yes than return true other wise return
     * false and according to this your adapter will update your record or insert new record in recycler view.
     */
    public static final DiffCallback<UserModel> USER_DIFF_CALLBACK = new DiffCallback<UserModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserModel oldUserModel, @NonNull UserModel newUserModel) {
            return oldUserModel.id == newUserModel.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserModel oldUserModel, @NonNull UserModel newUserModel) {
            return oldUserModel.equals(newUserModel);
        }
    };
    private Context mContext;

    public UserAdapter(Context mContext) {
        super(USER_DIFF_CALLBACK);
        this.mContext = mContext;
    }

    @Override
    public ViewHolderUserAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        //Append "Binding" as a suffix to your Layout resource file name but make sure to remove '_' or any special character from Resource file name and than append 'Binding' as a suffix.
        //Inflate layout using DataBindingUtil
        UserAdapterBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_adapter, parent, false);

        return new ViewHolderUserAdapter(viewDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderUserAdapter holder, int position) {

        final UserModel mUserModel = getItem(position);
        if (mUserModel != null) {
            holder.bind(mUserModel);
        }
        holder.itemView.setTag(position);
//        holder.itemView.setOnClickListener(view -> {
//            int pos = (int) view.getTag();
//            UserModel userModel = getItem(pos);
//            if (userModel != null) {
//                Intent mIntent = new Intent(view.getContext(), DetailActivity.class);
//                mIntent.putExtra(Constants.IntentKey.KEY_USERID, userModel.getId());
//                view.getContext().startActivity(mIntent);
//            }
//        });
    }

    public class ViewHolderUserAdapter extends RecyclerView.ViewHolder {

        private final UserAdapterBinding binding;

        public ViewHolderUserAdapter(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull UserModel userModel) {
            //set UserModel class for set values automatically from xml
            binding.setUserModel(userModel);
            binding.executePendingBindings();
        }
    }
}
