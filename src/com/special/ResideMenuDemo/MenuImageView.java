package com.special.ResideMenuDemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MenuImageView extends ImageView {
	/**
	 * MenuImageView����Ŀ��
	 */
	private int vWidth;
	/**
	 * MenuImageView����ĸ߶�
	 */
	private int vHeight;
	/**
	 * ������С��־��̽��б�־
	 */
	private boolean isReduce = false;
	/**
	 * ����С�ָ������̽��б�־
	 */
	private boolean isRecover = false;
	/**
	 * ��ű���
	 * 
	 */
	private float minScal = 0.95f;

	public MenuImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ��XML�ļ����ÿռ��ʱ����Ҫ����AttributeSet�������������Ȼ�ᱨ��
	 * 
	 * @param context
	 * @param attrs
	 */
	public MenuImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.onDraw(canvas);
			initi();
		

		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
	}

	private void initi() {
		vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		Log.i("vWidth + vWidth", vWidth + " " + vHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.onTouchEvent(event);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.i("ACTION_DOWN", "ACTION_DOWN");
			handler.sendEmptyMessage(1);
			break;
		case MotionEvent.ACTION_UP:
			Log.i("ACTION_UP", "ACTION_UP");
			handler.sendEmptyMessage(2);

			break;

		default:
			break;
		}
		return true;// return super.onTouchEvent(event);�Ļ���׽����UP����
	}

	Handler handler = new Handler() {
		float s = 0;// ��ű���
		int count = 0;// ������
		Matrix matrix = new Matrix();

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			matrix.set(getImageMatrix());
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (isRecover) {
					// ���ImageView���ڴ�Сͼ�ָ�����ͼʱ�򣬽���ȴ�
					handler.sendEmptyMessage(1);
				} else {
					isReduce = true;
					// ����}�ο�����Ϊ���ǰ�minScal���Ĵ���ţ��ǵ���Ź�̱���
					count = 0;
					s = (float) Math.sqrt(Math.sqrt(minScal));
					doScale(s, matrix);
					handler.sendEmptyMessage(3);
					Log.i("postScale", "��С" + " ����" + s + matrix.toString());
				}

			} else if (msg.what == 2) {
				// ��1ȥ����õ�����4��ԭ
				if (isReduce) {
					handler.sendEmptyMessage(2);
				} else {
					isRecover = true;
					count = 0;
					s = (float) (Math.sqrt(Math.sqrt(1f / minScal)));
					doScale(s, matrix);
					handler.sendEmptyMessage(3);
					Log.i("postScale", "�ָ�");
				}

			} else if (msg.what == 3) {
				// ������Ĵν��У��ﵽ�����Ե����Ч��
				doScale(s, matrix);
				if (count < 4) {
					handler.sendEmptyMessage(3);
				} else {
					// ��Ž���󣬰�}��״̬������Ϊfalse������һ���෴����--��ţ����Խ������
					isReduce = false;
					isRecover = false;

				}
				count++;
				Log.i("count", "count " + count);
			}
		}

	};

	private void doScale(float size, Matrix matrix) {
		matrix.postScale(size, size, (int) vWidth * 0.85f, (int) vHeight * 0.85f);
		MenuImageView.this.setImageMatrix(matrix);
		Log.i("scale", "scale " + size);
	}

}
