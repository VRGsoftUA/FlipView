package com.vrg.flipview;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * This view provides an easy way to implement double flip animation
 * @param <I> model item
 */
public class FlipView<I> extends FrameLayout {
    private static final int DEFAULT_CAMERA_DISTANCE = 30000;

    private AnimatorSet mBackAnimation;
    private AnimatorSet mFrontAnimation;
    private AnimatorSet mConfirmAnimation;
    private AnimatorSet backIn;
    private AnimatorSet frontIn;

    private View mCardFrontLayout;
    private View mCardBackLayout;
    private TextView mConfirmMask;
    private Context context;

    private List<I> mItems;
    private int position;
    private ViewBinder<I> binder;
    private int cameraDistance = DEFAULT_CAMERA_DISTANCE;
    private boolean cyclic = true;

    public FlipView(Context context) {
        super(context);
        this.context = context;
        initAnimation();
    }

    public FlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAnimation();
    }

    private void findViews(@LayoutRes int frontRes, @LayoutRes int backRes) {
        mCardBackLayout = LayoutInflater.from(context).inflate(backRes, this, false);
        mCardFrontLayout = LayoutInflater.from(context).inflate(frontRes, this, false);
        addView(mCardBackLayout);
        addView(mCardFrontLayout);

        mConfirmMask = new TextView(getContext());
        mConfirmMask.setBackground(getResources().getDrawable(R.drawable.confirm_mask));
        mConfirmMask.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        setConfirmVisible(false);
        addView(mConfirmMask);

        setHardwareAccelerate(true);

        mBackAnimation.setTarget(mCardBackLayout);
        mFrontAnimation.setTarget(mCardFrontLayout);
        mConfirmAnimation.setTarget(mConfirmMask);

        initCameraDistance();
    }

    private void initAnimation() {
        backIn = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.back_in);
        AnimatorSet backOut = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.back_out);
        frontIn = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.front_in);
        AnimatorSet frontOut = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.front_out);
        mConfirmAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.front_out);

        frontOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (++position > mItems.size() - 1) {
                    position = 0;
                }

                setConfirmVisible(false);
                notifyItemsChanged();
            }
        });

        mBackAnimation = new AnimatorSet();
        mBackAnimation.play(backIn).before(backOut);

        mFrontAnimation = new AnimatorSet();
        mFrontAnimation.play(frontOut).before(frontIn);
    }

    private void initCameraDistance() {
        float scale = getResources().getDisplayMetrics().density * cameraDistance;
        if(mCardFrontLayout != null) {
            mCardFrontLayout.setCameraDistance(scale);
        }
        if(mCardBackLayout != null) {
            mCardBackLayout.setCameraDistance(scale);
        }
        if(mConfirmMask != null){
            mConfirmMask.setCameraDistance(scale);
        }
    }

    public void setConfirmVisible(boolean visible){
        mConfirmMask.setVisibility(visible ? VISIBLE : GONE);
        if(!visible){
            mConfirmMask.setAlpha(1f);
        } else {
            mConfirmMask.setRotationY(0f);
        }
    }

    void setCameraDistance(int cameraDistance) {
        this.cameraDistance = cameraDistance;
        initCameraDistance();
    }


    int getCurrentPosition(){
        return position;
    }

    void setCurrentPosition(int position){
        this.position = position;
        binder.bindView(mItems.get(position));
    }

    void addBackInAnimListener(Animator.AnimatorListener listener){
        backIn.addListener(listener);
    }

    void addFrontInAnimListener(Animator.AnimatorListener listener){
        frontIn.addListener(listener);
    }

    void removeBackInAnimListener(Animator.AnimatorListener listener){
        backIn.removeListener(listener);
    }


    void removeFrontInAnimListener(Animator.AnimatorListener listener){
        backIn.removeListener(listener);
    }

    void flipTheView() {
        if(!cyclic){
            if (position + 1 > mItems.size() - 1) {
                return;
            }
        }

        if (mBackAnimation.isRunning() || mFrontAnimation.isRunning()){
            return;
        }

        mBackAnimation.start();
        mFrontAnimation.start();
        mConfirmAnimation.start();
    }

    void notifyItemsChanged(){
        if (binder != null) {
            binder.bindView(mItems.get(position));
        } else {
            throw new IllegalStateException("Binder should not be null");
        }
    }

    void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    void setHardwareAccelerate(boolean hardwareAccelerate) {
        if(hardwareAccelerate) {
            mCardBackLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mCardFrontLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mConfirmMask.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mCardBackLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mCardFrontLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mConfirmMask.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    void setItems(List<I> items){
        this.mItems = items;
        position = 0;
    }

    void setBinder(ViewBinder<I> binder) {
        this.binder = binder;
        findViews(binder.getFrontSideLayoutRes(), binder.getBackSideLayoutRes());
        binder.initView(mCardFrontLayout);
    }

    /**
     * You should implement this interface to give this view information
     * about how to set data in your layout.
     * @param <I> Your model item with data to be set.
     */
    public interface ViewBinder<I> {

        /**
         * Here you should find all child views.
         * @param frontView front view
         */
        void initView(View frontView);

        /**
         * Return front side layout resource id here
         * @return front side layout resource id
         */
        @LayoutRes
        int getFrontSideLayoutRes();

        /**
         * Return back side layout resource id here
         * @return back side layout resource id
         */
        @LayoutRes
        int getBackSideLayoutRes();

        /**
         * Here you should set data to your front view
         */
        void bindView(I item);
    }
}

