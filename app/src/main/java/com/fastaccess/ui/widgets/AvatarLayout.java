package com.fastaccess.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.fastaccess.R;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.ui.modules.user.UserPagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;

/**
 * Created by Kosh on 14 Nov 2016, 7:59 PM
 */

public class AvatarLayout extends FrameLayout implements ImageLoadingListener {

    @BindView(R.id.avatar) ShapedImageView avatar;
    @Nullable private String login;
    private boolean isOrg;
    private boolean isEnterprise;

    @OnClick(R.id.avatar) void onClick(@NonNull View view) {
        if (InputHelper.isEmpty(login)) return;
        UserPagerActivity.startActivity(view.getContext(), login, isOrg, isEnterprise, -1);
    }

    public AvatarLayout(@NonNull Context context) {
        super(context);
    }

    public AvatarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AvatarLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.avatar_layout, this);
        if (isInEditMode()) return;
        ButterKnife.bind(this);
        setBackground();
        if (PrefGetter.isRectAvatar()) {
            avatar.setShape(ShapedImageView.SHAPE_MODE_ROUND_RECT, 15);
        }
    }

    @Override public void onLoadingStarted(String imageUri, View view) {
//        setBackground(false);
    }

    @Override public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        if (failReason.getCause() != null) failReason.getCause().printStackTrace();
//        setBackground();
        setImageOnFailed();
    }

    @Override public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//        setBackground(true);
    }

    @Override public void onLoadingCancelled(String imageUri, View view) {}

    public void setUrl(@Nullable String url, @Nullable String login, boolean isOrg, boolean isEnterprise) {
        this.login = login;
        this.isOrg = isOrg;
        this.isEnterprise = isEnterprise;
        avatar.setContentDescription(login);
        if (url != null) {
            ImageLoader.getInstance().displayImage(url, avatar, this);
            if (login != null) {
                TooltipCompat.setTooltipText(avatar, login);
            }
        } else {
            avatar.setOnClickListener(null);
            if (login != null) {
                avatar.setOnLongClickListener(null);
            }
            ImageLoader.getInstance().displayImage(null, avatar);
            setImageOnFailed();
        }
    }

    private void setImageOnFailed() {
        avatar.setImageResource(R.drawable.ic_github);
    }

    private void setBackground() {
        if (PrefGetter.isRectAvatar()) {
            setBackgroundResource(R.drawable.rect_shape);
        } else {
            setBackgroundResource(R.drawable.circle_shape);
        }
    }
}
