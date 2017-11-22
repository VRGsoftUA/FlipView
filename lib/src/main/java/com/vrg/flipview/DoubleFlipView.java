package com.vrg.flipview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class DoubleFlipView<I> extends LinearLayout implements View.OnClickListener{
    private FlipView<I> leftView;
    private FlipView<I> rightView;
    private int centerX;
    private boolean animationRunning;

    private OnItemClickListener<I> itemListener;
    private List<Pair<I>> pairs;

    public DoubleFlipView(Context context) {
        super(context);
        initView();
    }

    public DoubleFlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DoubleFlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        setClipChildren(false);
        leftView = new FlipView<>(getContext());
        rightView = new FlipView<>(getContext());

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.START;
        params.weight = 1;
        params.rightMargin = 20;
        leftView.setLayoutParams(params);
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.END;
        params.weight = 1;
        params.leftMargin = 20;
        rightView.setLayoutParams(params);

        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);

        leftView.setClipChildren(false);
        rightView.setClipChildren(false);

        leftView.addFrontInAnimListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animationRunning = false;
            }
        });

        addView(leftView);
        addView(rightView);
    }

    /**
     * If this value equal to 0, rotation 3D effect will be completely shown.
     *
     * @param cameraDistance distance to camera
     */
    public void setCameraDistance(int cameraDistance) {
        leftView.setCameraDistance(cameraDistance);
        rightView.setCameraDistance(cameraDistance);
    }

    /**
     * Returns current position in data set provided to this view
     * @return current position in data set
     */
    public int getCurrentPosition(){
        return leftView.getCurrentPosition();
    }

    /**
     * Sets current position in data set provided to this view
     * @param position position to be set
     */
    void setCurrentPosition(int position){
        leftView.setCurrentPosition(position);
        rightView.setCurrentPosition(position);
    }

    /**
     * Adds a listener to back in animator (when back side starts to show itself)
     * @param listener animator listener
     */
    public void addBackInAnimListener(Animator.AnimatorListener listener){
        leftView.addBackInAnimListener(listener);
    }

    /**
     * Adds a listener to front in animator (when front side starts to show itself)
     * @param listener animator listener
     */
    public void addFrontInAnimListener(Animator.AnimatorListener listener){
        leftView.addFrontInAnimListener(listener);
    }

    /**
     * Removes a listener from back in animator
     * @param listener animator listener to be removed
     */
    public void removeBackInAnimListener(Animator.AnimatorListener listener){
        leftView.removeBackInAnimListener(listener);
    }

    /**
     * Removes a listener from front in animator
     * @param listener animator listener to be removed
     */
    public void removeFrontInAnimListener(Animator.AnimatorListener listener){
        leftView.removeFrontInAnimListener(listener);
    }


    /**
     * Sets whether this view should show data set cyclically or not.
     * True by default.
     * @param cyclic if true data set will be used cyclically
     */
    public void setCyclic(boolean cyclic) {
        leftView.setCyclic(cyclic);
        rightView.setCyclic(cyclic);
    }

    /**
     * Sets whether this view should use hardware acceleration or not.
     * (If set to true - smooths out rotation and removes flickering effect on some devices).
     * True by default.
     * @param hardwareAccelerate use hardware to accelerate drawing or not
     */
    public void setHardwareAccelerate(boolean hardwareAccelerate) {
        leftView.setHardwareAccelerate(hardwareAccelerate);
        rightView.setHardwareAccelerate(hardwareAccelerate);
    }

    public void setItemListener(OnItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setRightBinder(FlipView.ViewBinder<I> binder){
        rightView.setBinder(binder);
    }

    public void setLeftBinder(FlipView.ViewBinder<I> binder){
        leftView.setBinder(binder);
    }

    public void notifyDatasetChanged(){
        leftView.notifyItemsChanged();
        rightView.notifyItemsChanged();
    }

    public void setItems(List<Pair<I>> pairs){
        this.pairs = pairs;
        List<I> left = new ArrayList<>();
        List<I> right = new ArrayList<>();
        for(Pair<I> pair : pairs){
            left.add(pair.getLeftItem());
            right.add(pair.getRightItem());
        }
        leftView.setItems(left);
        rightView.setItems(right);
    }

    public void setDistanceBetweenCards(int distance){
        int margin = distance / 2;
        ((LayoutParams)leftView.getLayoutParams()).rightMargin = margin;
        ((LayoutParams)rightView.getLayoutParams()).leftMargin = margin;
    }

    @Override
    public void onClick(View v) {
        if(animationRunning){
            return;
        }

        animationRunning = true;

        if (v.equals( leftView)) {
            if (itemListener != null){
                itemListener.onItemClick(leftView.getCurrentPosition(), true, pairs.get(leftView.getCurrentPosition()).getLeftItem());
            }
            ViewCompat.setZ(leftView, 2);
            ViewCompat.setZ(rightView, 1);
            leftView.setConfirmVisible(true);
            centerX = (int) getX() + getWidth() / 2;
            leftView.animate()
                    .translationX(centerX - leftView.getWidth() / 2 - leftView.getX())
                    .translationY(-50)
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            final ViewPropertyAnimator animator = leftView.animate();
                            animator
                                    .translationX(-centerX + leftView.getX() + leftView.getWidth() / 2)
                                    .translationY(0)
                                    .setStartDelay(200)
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            animator.setListener(null);
                                            leftView.flipTheView();
                                            rightView.flipTheView();
                                        }
                                    });
                        }
                    });
        } else {
            if (itemListener != null){
                itemListener.onItemClick(rightView.getCurrentPosition(), false, pairs.get(rightView.getCurrentPosition()).getRightItem());
            }
            ViewCompat.setZ(leftView, 1);
            ViewCompat.setZ(rightView, 2);
            rightView.setConfirmVisible(true);
            centerX = (int) getX() + getWidth() / 2;
            rightView.animate()
                    .translationX(centerX - rightView.getWidth() / 2 - rightView.getX())
                    .translationY(-50)
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            final ViewPropertyAnimator animator = rightView.animate();
                            animator
                                    .translationX(-centerX + rightView.getX() + rightView.getWidth() / 2)
                                    .translationY(0)
                                    .setStartDelay(200)
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            animator.setListener(null);
                                            leftView.flipTheView();
                                            rightView.flipTheView();
                                        }
                                    });
                        }
                    });
        }
    }
    public interface OnItemClickListener<I>{
        void onItemClick(int position, boolean left, I item);
    }
}

