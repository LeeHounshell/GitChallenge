package com.harlie.leehounshell.gitchallenge.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.model.Repository_Model;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.view.UserTabFragment;

import java.util.List;

public class RepositoryListAdapter extends RecyclerView.Adapter<RepositoryListAdapter.RepositoryViewHolder> {
    private final static String TAG = "LEE: <" + RepositoryListAdapter.class.getSimpleName() + ">";

    private final UserTabFragment tabFragment;
    private List<Repository_Model> repositoryModelList;

    public RepositoryListAdapter(UserTabFragment tabFragment, List<Repository_Model> repositoryList) {
        this.tabFragment = tabFragment;
        this.repositoryModelList = repositoryList;
    }

    @NonNull
    @Override
    public RepositoryListAdapter.RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogHelper.v(TAG, "onCreateViewHolder");
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_repository_row, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryListAdapter.RepositoryViewHolder holder, int position) {
        final Repository_Model repositoryModel = repositoryModelList.get(position);

        holder.getHolderView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.v(TAG, "-CLICK-");
                tabFragment.showRepository(repositoryModel.getRepoUrl());
            }
        });

        Context context = GitChallengeApplication.getAppContext();
        holder.getRepoName().setText(repositoryModel.getRepoName());
        holder.getRepoUrl().setText(repositoryModel.getRepoUrl());
        holder.getRepoStars().setText(String.valueOf(repositoryModel.getRepoStars() + " " + context.getString(R.string.stars)));
    }

    @Override
    public int getItemCount() {
        return repositoryModelList.size();
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = "LEE: <" + RepositoryViewHolder.class.getSimpleName() + ">";

        private final View holderView;
        private AppCompatTextView repoName;
        private AppCompatTextView repoUrl;
        private AppCompatTextView repoStars;

        public RepositoryViewHolder(View itemView) {
            super(itemView);
            this.holderView = itemView;
            this.repoName = itemView.findViewById(R.id.repo_name);
            this.repoUrl = itemView.findViewById(R.id.repo_url);
            this.repoStars = itemView.findViewById(R.id.repo_stars);
        }

        View getHolderView() {
            //LogHelper.v(TAG, "getHolderView");
            return holderView;
        }

        public AppCompatTextView getRepoName() {
            return repoName;
        }

        public void setRepoName(AppCompatTextView repoName) {
            this.repoName = repoName;
        }

        public AppCompatTextView getRepoUrl() {
            return repoUrl;
        }

        public void setRepoUrl(AppCompatTextView repoUrl) {
            this.repoUrl = repoUrl;
        }

        public AppCompatTextView getRepoStars() {
            return repoStars;
        }

        public void setRepoStars(AppCompatTextView repoStars) {
            this.repoStars = repoStars;
        }
    }
}
