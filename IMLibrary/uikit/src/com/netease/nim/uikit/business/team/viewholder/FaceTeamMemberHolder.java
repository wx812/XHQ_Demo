package com.netease.nim.uikit.business.team.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.team.model.Team;

public class FaceTeamMemberHolder extends TViewHolder {


    protected TeamMemberHolder.TeamMemberHolderEventListener teamMemberHolderEventListener;

    public void setEventListener(TeamMemberHolder.TeamMemberHolderEventListener eventListener) {
        this.teamMemberHolderEventListener = eventListener;
    }

    private HeadImageView headImageView;

    private ImageView ownerImageView;

    private ImageView adminImageView;

    private ImageView deleteImageView;

    private TextView nameTextView;

    private ImageView ivOperation;//删除按钮

    private Boolean isShowOperation = null;

    private TeamMemberAdapter.TeamMemberItem memberItem;

    public final static String OWNER = "owner";
    public final static String ADMIN = "admin";

    protected TeamMemberAdapter getAdapter() {
        return (TeamMemberAdapter) super.getAdapter();
    }

    @Override
    protected int getResId() {
        return R.layout.nim_face_team_member_item;
    }

    @Override
    protected void inflate() {
        headImageView = (HeadImageView) view.findViewById(R.id.imageViewHeader);
        nameTextView = (TextView) view.findViewById(R.id.textViewName);
        ownerImageView = (ImageView) view.findViewById(R.id.imageViewOwner);
        adminImageView = (ImageView) view.findViewById(R.id.imageViewAdmin);
        deleteImageView = (ImageView) view.findViewById(R.id.imageViewDeleteTag);
        ivOperation = (ImageView) view.findViewById(R.id.iv_face_team_member_operation);


    }

    @Override
    protected void refresh(Object item) {
        memberItem = (TeamMemberAdapter.TeamMemberItem) item;

        //判断当前用户是否是群组的创建人
        //任务紧急 这个isShowOperation应该adapter传过来的  后面再优化
        if (isShowOperation == null) {
            Team team = TeamDataCache.getInstance().getTeamById(memberItem.getTid());
            if (team != null) {
                isShowOperation = isSelf(team.getCreator());
            } else {
                isShowOperation = false;
            }
        }

        headImageView.resetImageView();
        ownerImageView.setVisibility(View.GONE);
        adminImageView.setVisibility(View.GONE);
        deleteImageView.setVisibility(View.GONE);
        ivOperation.setVisibility(View.GONE);

        if (getAdapter().getMode() == TeamMemberAdapter.Mode.NORMAL) {
            view.setVisibility(View.VISIBLE);
            if (memberItem.getTag() == TeamMemberAdapter.TeamMemberItemTag.ADD) {
                // add team member
                headImageView.setBackgroundResource(R.drawable.nim_team_member_add_selector);
                nameTextView.setText(context.getString(R.string.add));
                headImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAdapter().getAddMemberCallback().onAddMember();
                    }
                });
            } else if (memberItem.getTag() == TeamMemberAdapter.TeamMemberItemTag.DELETE) {
                // delete team member
                headImageView.setBackgroundResource(R.drawable.nim_team_member_delete_selector);
                nameTextView.setText(context.getString(R.string.remove));
                headImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAdapter().setMode(TeamMemberAdapter.Mode.DELETE);
                        getAdapter().notifyDataSetChanged();
                    }
                });
            } else {
                // show team member
                refreshTeamMember(memberItem, false);
            }
        } else if (getAdapter().getMode() == TeamMemberAdapter.Mode.DELETE) {
            if (memberItem.getTag() == TeamMemberAdapter.TeamMemberItemTag.NORMAL) {
                refreshTeamMember(memberItem, true);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void refreshTeamMember(final TeamMemberAdapter.TeamMemberItem item, boolean deleteMode) {
        nameTextView.setText(TeamHelper.getTeamMemberDisplayName(item.getTid(), item.getAccount()));
        headImageView.loadBuddyAvatar(item.getAccount());
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamMemberHolderEventListener != null) {
                    teamMemberHolderEventListener.onHeadImageViewClick(item.getAccount());
                }
            }
        });

        //删除按钮显示 当前显示删除按钮(即当前群是当前登录账户创建的)  并且当前item不是当前用户  则显示删除按钮
        if (isShowOperation) {
            if (!isSelf(item.getAccount())) {
                ivOperation.setVisibility(View.VISIBLE);
                ivOperation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAdapter().getRemoveMemberCallback().onRemoveMember(item.getAccount());
                    }
                });
            }
        }


        if (item.getDesc() != null) {
            //显示创建者标识
            if (item.getDesc().equals(OWNER)) {
                ownerImageView.setVisibility(View.VISIBLE);
            }
        }

    }

    private boolean isSelf(String account) {
        return account.equals(NimUIKit.getAccount());
    }
}
