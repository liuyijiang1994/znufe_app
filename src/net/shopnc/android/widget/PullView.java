package net.shopnc.android.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.special.ResideMenuDemo.R;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

public class PullView extends FrameLayout implements
		GestureDetector.OnGestureListener, Animation.AnimationListener {
	public static int MAX_LENGHT = 0;
	private Animation animationDown;
	private Animation animationUp;
	private ImageView arrow;
	private FrameLayout content;
	private Flinger flinger;
	private GestureDetector gestureDetector;
	private boolean isAutoScroller;
	private boolean isIgnore = false;
	//private int mOriginalTopPadding = 0;
	//private int multiple = 2;
	private int paddingTop = 0;
	private ProgressBar progressBar;
	private int state = 0;
	private final int state_close = 1;
	// private final int state_init = 0;
	private final int state_open = 2;
	private final int state_open_max = 3;
	private final int state_open_max_release = 5;
	private final int state_open_release = 4;
	private final int state_update = 6;
	private String suffix = "";
	private TextView title;
	private String updateData = "";
	private UpdateHandle updateHandle;

	public PullView(Context paramContext) {
		super(paramContext);
		init();
		initLayout();
	}

	public PullView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init();
		initLayout();
	}

	private void init() {
		this.updateData = getCurDate();
		MAX_LENGHT = getResources().getDimensionPixelSize(
				R.dimen.updatebar_height);
		this.flinger = new Flinger();
		this.gestureDetector = new GestureDetector(this);
		this.gestureDetector.setIsLongpressEnabled(true);
	    setDrawingCacheEnabled(false);
	    setBackgroundDrawable(null);
	    setClipChildren(false);
		this.state = state_close;
	}

	private void initLayout() {
		this.animationUp = AnimationUtils.loadAnimation(getContext(),
				R.anim.rotate_up);
		this.animationUp.setAnimationListener(this);
		this.animationDown = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_down);
		this.animationDown.setAnimationListener(this);
		addView(LayoutInflater.from(getContext()).inflate(R.layout.pull_bar,null));
		
		this.arrow = new ImageView(getContext());
		//this.arrow.setBackgroundColor(0);
		this.arrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
		this.arrow.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		this.arrow.setImageResource(R.drawable.arrow_down);

		this.content = ((FrameLayout) getChildAt(0).findViewById(R.id.update_bar_content));
		this.content.addView(this.arrow);
		
		this.progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
		//this.progressBar.setBackgroundColor(0);

		int i = getResources().getDimensionPixelSize(R.dimen.updatebar_padding);
		this.progressBar.setPadding(i, i, i, i);
		this.progressBar.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		this.content.addView(this.progressBar);
		
		this.title = ((TextView) findViewById(R.id.update_bar_title));
		//this.mOriginalTopPadding = this.content.getPaddingTop();
	}


	 private boolean move(float paramFloat, boolean paramBoolean)
	  {
	    boolean flag = true;
	    if (this.state == 6)
	    {
	      this.paddingTop = (int)(paramFloat + this.paddingTop);
	      if (!paramBoolean)
	      {
	        if (this.paddingTop <= 0)
	        {
	          if (Math.abs(this.paddingTop) > MAX_LENGHT)
	            this.paddingTop = (-MAX_LENGHT);
	        }
	        else
	          this.paddingTop = 0;
	      }
	      else if (this.paddingTop > 0)
	        this.paddingTop = (-MAX_LENGHT);
	    }
	    else
	    {
	      this.paddingTop = (int)(paramFloat + this.paddingTop);
	      if (this.paddingTop >= 0)
	      {
	        this.paddingTop = 0;
	        this.state = 1;
	        //ExpressLog.out("PullView", "move_state0:" + this.state);
	      }
	      if ((this.state == 4) || (this.state == 5))
	      {
	        if (this.state != 4)
	        {
	          if (this.state == 5)
	          {

	            this.state = 6;
	            this.progressBar.setVisibility(0);
	            this.arrow.setVisibility(4);
	            if (this.updateHandle != null)
	              this.updateHandle.onUpdate();
	          }
	        }
	        else
	        {
	          this.state = 2;
	          this.progressBar.setVisibility(4);
	          this.arrow.setVisibility(0);
	        }
	      }
	      else
	      {
	        invalidate();
	        if (Math.abs(this.paddingTop) <= MAX_LENGHT)
	        {
	          if (this.state != 2)
	          {
	            this.progressBar.setVisibility(4);
	            this.arrow.setVisibility(0);
	            if (this.state == 3)
	              this.arrow.startAnimation(this.animationDown);
	            this.state = 2;
	            //ExpressLog.error("PullView", "move_state1:" + this.state);
	          }
	        }
	        else if (this.state != 3)
	        {
	          this.state = 3;
	          this.progressBar.setVisibility(4);
	          this.arrow.setVisibility(0);
	          this.arrow.startAnimation(this.animationUp);
	        }
	      }
	    }
	    if ((Math.abs(this.paddingTop) <= 0) || (this.state == 6))
	      flag = false;
	    return flag;
	  }


	private boolean release() {
		boolean flag = false;
		if ((this.paddingTop < 0) || (!this.isAutoScroller)) {
			if (Math.abs(this.paddingTop) <= MAX_LENGHT) {
				this.state = 4;
				scrollToClose();
			} else {
				this.state = 5;
				scrollToUpdate();
			}
			// ExpressLog.out("PullView", "release state:" + this.state);
			if (Math.abs(this.paddingTop) > 0)
				flag = true;
		}
		return flag;
	}

	private void resetPadding() {
		// ExpressLog.out("setUpdateData_mOriginalTopPadding:",Integer.valueOf(this.mOriginalTopPadding));
		this.content
				.setPadding(this.content.getPaddingLeft(), 0,
						this.content.getPaddingRight(),
						this.content.getPaddingBottom());
	}

	private void scrollToClose() {
		this.flinger.startUsingDistance(-this.paddingTop, 300);
	}

	private void scrollToUpdate() {
		this.flinger.startUsingDistance(-this.paddingTop - MAX_LENGHT, 300);
	}

	protected void dispatchDraw(Canvas paramCanvas) {
		super.dispatchDraw(paramCanvas);
		StringBuilder localStringBuilder = new StringBuilder();
		View localView1 = getChildAt(0);
		View localView2 = getChildAt(1);
		switch (this.state) {
		case state_close:
			localView1.setVisibility(4);
			localView2.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
//			this.arrow.setImageResource(2130837510);
			if (this.paddingTop <= 0)
				break;
			this.paddingTop = 0;
			break;
		case state_open:
			localView2.offsetTopAndBottom(-this.paddingTop
					- localView2.getTop());
			localView1.setVisibility(0);
			localView1.offsetTopAndBottom(-MAX_LENGHT - this.paddingTop
					- localView1.getTop());
			localStringBuilder
					.append(getContext().getString(
							R.string.pull_to_refresh_pull))
					.append("\n")
					.append(getContext().getString(R.string.pull_to_refresh_at))
					.append(this.updateData);
			this.title.setText(localStringBuilder.toString());
			break;
		case state_open_max:
			localView2.offsetTopAndBottom(-this.paddingTop
					- localView2.getTop());
			localView1.setVisibility(0);
			localView1.offsetTopAndBottom(-MAX_LENGHT - this.paddingTop
					- localView1.getTop());
			localStringBuilder
					.append(getContext().getString(
							R.string.pull_to_refresh_release))
					.append("\n")
					.append(getContext().getString(R.string.pull_to_refresh_at))
					.append(this.updateData);
			this.title.setText(localStringBuilder.toString());
			break;
		case state_update:
			localView2.offsetTopAndBottom(-this.paddingTop
					- localView2.getTop());
			localView1.setVisibility(0);
			localView1.offsetTopAndBottom(-MAX_LENGHT - this.paddingTop
					- localView1.getTop());
			this.progressBar.setVisibility(0);
			this.arrow.setVisibility(4);
			localStringBuilder
					.append(getContext().getString(
							R.string.pull_to_refresh_refreshing))
					.append("\n")
					.append(getContext().getString(R.string.pull_to_refresh_at))
					.append(this.updateData);
			this.title.setText(localStringBuilder.toString());
		case state_open_release:
		case state_open_max_release:
		}
		invalidate();
	}

	public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
		boolean bool;
		if (!this.isAutoScroller) {
			bool = false;
			if ((paramMotionEvent.getAction() == 1)
					&& ((this.state == state_open) || (this.state == state_open_max)))
				bool = release();
			if (!bool)
				if ((this.isIgnore)
						|| (!this.gestureDetector
								.onTouchEvent(paramMotionEvent)))
					bool = super.dispatchTouchEvent(paramMotionEvent);
				else
					bool = true;
		} else {
			bool = true;
		}
		return bool;
	}

	public void endUpdate() {
		this.updateData = getCurDate();
		if ((this.suffix != null) && (!"".equals(this.suffix)))
			// PersonalPreference.getInstance(NewsApp.getInstance()).setLastRefreshTime(this.suffix,
			// this.updateData);
			if (this.paddingTop != 0)
				resetPadding();
		this.paddingTop = 0;
		this.state = state_close;
	}

	public String getCurDate() {
		return new SimpleDateFormat("MM-dd HH:mm").format(Long
				.valueOf(new Date().getTime()));
	}

	public void onAnimationEnd(Animation paramAnimation) {
		if ((this.state != state_close) && (this.state != state_open) && (this.state != state_open_release)){
			this.arrow.setImageResource(R.drawable.arrow_up);
		}else{
			this.arrow.setImageResource(R.drawable.arrow_down);
		}
	}

	public void onAnimationRepeat(Animation paramAnimation) {
	}

	public void onAnimationStart(Animation paramAnimation) {
	}

	public boolean onDown(MotionEvent paramMotionEvent) {
		return false;
	}

	public boolean onFling(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		getChildAt(0).layout(0, -this.paddingTop, getMeasuredWidth(), MAX_LENGHT - this.paddingTop);
		getChildAt(1).layout(0, -this.paddingTop, getMeasuredWidth(), getMeasuredHeight() - this.paddingTop);
	}

	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		// ExpressLog.out("PullView", "paddingTop:" + this.paddingTop +
		// "  distanceY:" + paramFloat2);
		Object localObject = getChildAt(1);
		boolean bool = false;
		if (!(localObject instanceof AdapterView)) {
			bool = PullView.this.move(paramFloat2, bool);
		} else {
			AdapterView<?> av = (AdapterView<?>) localObject;
			if ((av.getFirstVisiblePosition() == 0)
					&& ((this.paddingTop != 0) || (av.getChildAt(0) == null) || (av
							.getChildAt(0).getTop() == 0)))
				bool = move(paramFloat2, false);
		}
		return bool;
	}

	public void onShowPress(MotionEvent paramMotionEvent) {
	}

	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
		return false;
	}

	public void setIgnore(boolean paramBoolean) {
		this.isIgnore = paramBoolean;
	}

	public void setUpdateHandle(UpdateHandle paramUpdateHandle) {
		this.updateHandle = paramUpdateHandle;
	}

	public void setUpdateTime(String paramString) {
		if ((paramString != null) && (!"".equals(paramString))) {
			this.suffix = paramString;
			String str = this.getCurDate();
			if ((str != null) && (!"".equals(str)))
				this.updateData = str;
			else
				this.updateData = getCurDate();
		}
	}

	public void startUpdate() {
		this.paddingTop = -(1 + MAX_LENGHT);
		release();
	}

	class Flinger implements Runnable {
		private int lastFlingX;
		private Scroller scroller = new Scroller(PullView.this.getContext());

		public Flinger() {
		}

		private void startCommon() {
			PullView.this.removeCallbacks(this);
		}

		public void run() {
			PullView.this.move(-this.lastFlingX, true);
			if (!this.scroller.computeScrollOffset()) {
				PullView.this.isAutoScroller = false;
				PullView.this.removeCallbacks(this);
			} else {
				this.lastFlingX = this.scroller.getCurrX();
				// ExpressLog.out("PullView", "lastFlingX:" + this.lastFlingX);
				PullView.this.post(this);
			}
		}

		public void startUsingDistance(int paramInt1, int paramInt2) {
			startCommon();
			this.lastFlingX = 0;
			this.scroller.startScroll(0, 0, -paramInt1, 0, paramInt2);
			PullView.this.isAutoScroller = true;
			PullView.this.post(this);
		}
	}

	public static abstract interface UpdateHandle {
		/** 下拉更新时的回调接口 */
		public abstract void onUpdate();
	}
}