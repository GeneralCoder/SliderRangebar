package com.captain.sliderrangebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author captain ˫�򻬶�ѡ��ؼ�
 */
public class SliderRangeBar extends View {

	private Drawable bgDrawable, underDrawable, rangeDrawable, leftSliderDrawable, rightSliderDrawable;
	private int mWidth, mHight, nSliderBarWidth;
	private int nScaleFontSize = 13, nTitleFontSize = 16;
	private Context mContext;
	private String[] nScale;// �̶�ֵ
	private String nTitle;
	private int[] nScaleOffset;// �̶�ֵ����λ��
	private boolean isLeftSliderPressed, isRightSliderPressed;
	private int nMinSliderOffset, nMaxSliderOffset;
	private OnSliderRangeBarChangeListener onSliderRangeBarChangeListener;

	public SliderRangeBar(Context context) {
		this(context, null);
	}

	public SliderRangeBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SliderRangeBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		mContext = context;
		bgDrawable = context.getResources().getDrawable(R.drawable.slider_bar_bg);
		underDrawable = context.getResources().getDrawable(R.drawable.slider_bar_under);
		rangeDrawable = context.getResources().getDrawable(R.drawable.slider_bar_range);
		leftSliderDrawable = context.getResources().getDrawable(R.drawable.slider_bar_block);
		rightSliderDrawable = context.getResources().getDrawable(R.drawable.slider_bar_block);
		mWidth = bgDrawable.getIntrinsicWidth();
		mHight = rightSliderDrawable.getIntrinsicHeight() + 120;
		nSliderBarWidth = leftSliderDrawable.getIntrinsicWidth();
		nScale = new String[] { "100", "200", "300", "400", "500" };// default scale value
		nScaleOffset = new int[nScale.length];
		nMinSliderOffset = 0;// defalut min value
		nMaxSliderOffset = mWidth - nSliderBarWidth;// defalut max value
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SliderRangeBar, defStyle, 0);
		if (a != null) {
			nTitle = a.getString(R.styleable.SliderRangeBar_title);
		}
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint titlePaint = new Paint();
		titlePaint.setColor(Color.parseColor("#333333"));
		titlePaint.setTextSize(getPixelFromDip(mContext.getResources().getDisplayMetrics(), nTitleFontSize));
		FontMetrics fontMetrics = titlePaint.getFontMetrics();
		float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
		float offY = fontTotalHeight - fontMetrics.bottom;
		if (!TextUtils.isEmpty(nTitle)) {
			int fontWidth = (int) titlePaint.measureText(nTitle);
			canvas.drawText(nTitle, mWidth / 2 - fontWidth / 2, offY, titlePaint);
		}
		int bgDrawable_offy = (int) (offY + 28);
		bgDrawable.setBounds(0, (int) (offY + 28), bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight() + bgDrawable_offy);
		bgDrawable.draw(canvas);

		int underDrawable_offy = bgDrawable_offy + bgDrawable.getIntrinsicHeight() / 2 - underDrawable.getIntrinsicHeight() / 2;
		underDrawable.setBounds(0, underDrawable_offy, underDrawable.getIntrinsicWidth(), underDrawable.getIntrinsicHeight() + underDrawable_offy);
		underDrawable.draw(canvas);

		rangeDrawable.setBounds(nMinSliderOffset + nSliderBarWidth / 2, underDrawable_offy, nMaxSliderOffset + nSliderBarWidth / 2,
				rangeDrawable.getIntrinsicHeight() + underDrawable_offy);
		rangeDrawable.draw(canvas);

		leftSliderDrawable.setBounds(nMinSliderOffset, bgDrawable_offy - 12, nMinSliderOffset + nSliderBarWidth, leftSliderDrawable.getIntrinsicHeight()
				+ bgDrawable_offy - 12);
		leftSliderDrawable.draw(canvas);

		rightSliderDrawable.setBounds(nMaxSliderOffset, underDrawable_offy - 12, nMaxSliderOffset + nSliderBarWidth, rightSliderDrawable.getIntrinsicHeight()
				+ bgDrawable_offy - 12);
		rightSliderDrawable.draw(canvas);

		Paint scalePaint = new Paint();
		scalePaint.setTextSize(getPixelFromDip(mContext.getResources().getDisplayMetrics(), nScaleFontSize));
		scalePaint.setColor(Color.parseColor("#666666"));
		if (nScale == null || nScale.length < 2) {
			throw new RuntimeException("�̶�ֵ����Ϊnull�ҳ��Ȳ���С��2");
		}
		int dis = (bgDrawable.getIntrinsicWidth() - nSliderBarWidth) / (nScale.length - 1);
		// ��ʼ���̶�ֵ
		for (int i = 0; i < nScale.length; i++) {
			String scaleString = nScale[i];
			int fontWidth = (int) scalePaint.measureText(scaleString);
			nScaleOffset[i] = i * dis;
			canvas.drawText(nScale[i], nSliderBarWidth / 2 + i * dis - fontWidth / 2, nSliderBarWidth + bgDrawable_offy + 30, scalePaint);
		}
	}

	// �Ƿ��ڰ�ѹ״̬
	boolean isActionDown = true;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (!isEnabled())
			return false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isActionDown = true;
			break;
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			if (isActionDown && isLeftSliderPressed && !isRightSliderPressed) {
				nMinSliderOffset = x;

				if (x >= nMaxSliderOffset - nSliderBarWidth) {// �����󻬿鲻�����һ���
					nMinSliderOffset = nMaxSliderOffset - nSliderBarWidth;
				} else if (x >= mWidth - nSliderBarWidth) {
					nMinSliderOffset = mWidth - nSliderBarWidth;
				} else if (x <= nSliderBarWidth / 2.0) {
					nMinSliderOffset = 0;
				} else {
					nMinSliderOffset = x;
				}
			}
			if (isActionDown && !isLeftSliderPressed && isRightSliderPressed) {
				nMaxSliderOffset = x;

				if (x >= mWidth - nSliderBarWidth) {
					nMaxSliderOffset = mWidth - nSliderBarWidth;
				} else if (x <= nMinSliderOffset + nSliderBarWidth) {// �����һ��鲻�����󻬿�
					nMaxSliderOffset = nMinSliderOffset + nSliderBarWidth;
				} else if (x <= nSliderBarWidth / 2.0) {
					nMaxSliderOffset = 0;
				} else {
					nMaxSliderOffset = x;
					// Log.d("tag", "right  [else] " + nMaxSliderOffset);
				}
			}
			if (!isRightSliderPressed && isLeftSliderSelected(event)) {
				isLeftSliderPressed = true;
			} else if (!isLeftSliderPressed && isRightSliderSelected(event)) {
				isRightSliderPressed = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			// ����̶ȶ���
			if (isLeftSliderPressed) {
				int nearstLeftSliderIndex = calcNearestScale(nMinSliderOffset);
				nMinSliderOffset = nScaleOffset[nearstLeftSliderIndex];
				// �󻬿����̶����һ���ǰһ�̶�
				if (nMinSliderOffset >= nMaxSliderOffset - nSliderBarWidth) {
					nMinSliderOffset = nScaleOffset[nearstLeftSliderIndex - 1];
				}
			} else if (isRightSliderPressed) {
				int nearstRightSliderIndex = calcNearestScale(nMaxSliderOffset);
				nMaxSliderOffset = nScaleOffset[nearstRightSliderIndex];
				// �һ�����С�̶����󻬿��һ�̶�
				if (nMaxSliderOffset <= nMinSliderOffset + nSliderBarWidth) {
					nMaxSliderOffset = nScaleOffset[nearstRightSliderIndex + 1];
				}
			}
			if (onSliderRangeBarChangeListener != null) {
				onSliderRangeBarChangeListener.onChange(this, calcNearestScale(nMinSliderOffset), calcNearestScale(nMaxSliderOffset));
			}
			isLeftSliderPressed = false;
			isRightSliderPressed = false;
			isActionDown = false;
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}

	/**
	 * @param nScale
	 *            ���ÿ̶���
	 */
	public void initScale(String[] nScale) {
		this.nScale = nScale;
		nScaleOffset = new int[nScale.length];
		invalidate();
	}

	/**
	 * �����̶�����
	 * 
	 * @param minIndex
	 * @param maxIndex
	 */
	public void setScaleValue(int minIndex, int maxIndex) {
		if (minIndex < 0) {
			throw new RuntimeException("minIndex����С��0");
		}
		if (maxIndex > nScale.length - 1) {
			throw new RuntimeException("maxIndex���ܴ��ڿ̶��ܳ���");
		}
		if (minIndex >= maxIndex) {
			throw new RuntimeException("maxIndex ����С�ڵ��� minIndex");
		}
		Paint scalePaint = new Paint();
		scalePaint.setTextSize(nScaleFontSize);
		int dis = (bgDrawable.getIntrinsicWidth() - nSliderBarWidth) / (nScale.length - 1);
		for (int i = 0; i < nScale.length; i++) {
			nScaleOffset[i] = dis * i;
		}
		nMinSliderOffset = nScaleOffset[minIndex];
		nMaxSliderOffset = nScaleOffset[maxIndex];
		invalidate();
	}

	/**
	 * ���ؾ��������һ���̶�index
	 * 
	 * @param sliderOffset
	 * @return
	 */
	private int calcNearestScale(int sliderOffset) {
		int minIndex = 0;
		int minDis = mWidth / nScale.length;
		for (int i = 0; i < nScale.length; i++) {
			int dis = Math.abs(sliderOffset - nScaleOffset[i]);
			if (dis <= minDis) {
				minDis = dis;
				minIndex = i;
			}
		}
		return minIndex;
	}

	private boolean isLeftSliderSelected(MotionEvent event) {
		if (event.getX() >= nMinSliderOffset - nSliderBarWidth / 2.0 && event.getX() <= nMinSliderOffset + nSliderBarWidth) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isRightSliderSelected(MotionEvent event) {
		if (event.getX() >= nMaxSliderOffset - nSliderBarWidth / 2.0 && event.getX() <= nMaxSliderOffset + nSliderBarWidth) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(mWidth, mHight);
	}

	/**
	 * @return the nScale
	 */
	public String[] getScale() {
		return nScale;
	}

	/**
	 * @return the onSliderRangeBarChangeListener
	 */
	public OnSliderRangeBarChangeListener getOnSliderRangeBarChangeListener() {
		return onSliderRangeBarChangeListener;
	}

	/**
	 * @param onSliderRangeBarChangeListener
	 *            the onSliderRangeBarChangeListener to set
	 */
	public void setOnSliderRangeBarChangeListener(OnSliderRangeBarChangeListener onSliderRangeBarChangeListener) {
		this.onSliderRangeBarChangeListener = onSliderRangeBarChangeListener;
	}

	/**
	 * @return the nTitle
	 */
	public String getTitle() {
		return nTitle;
	}

	/**
	 * @param nTitle
	 *            the nTitle to set
	 */
	public void setTitle(String nTitle) {
		this.nTitle = nTitle;
	}

	public interface OnSliderRangeBarChangeListener {
		void onChange(SliderRangeBar sliderRangeBar, int minIndex, int maxIndex);
	}

	/**
	 * Dipת��Ϊʵ����Ļ������ֵ
	 * 
	 * @param dm
	 *            �豸��ʾ��������
	 * @param dip
	 *            dipֵ
	 * @return ƥ�䵱ǰ��Ļ������ֵ
	 */
	public static int getPixelFromDip(DisplayMetrics dm, float dip) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + 0.5f);
	}
}
