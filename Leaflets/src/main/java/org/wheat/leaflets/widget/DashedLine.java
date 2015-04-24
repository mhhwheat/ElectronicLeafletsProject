package org.wheat.leaflets.widget;


import org.wheat.leaflets.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

//���߿ؼ�
public class DashedLine extends View
{
	private Paint paint=null;
	private Path path=null;
	//�������
	private int strokeWith=2;
	public DashedLine(Context context)
	{
		this(context, null);
	}
	public DashedLine(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		//ͨ��R.styleable.dashedline���������attrs.xml�ж����
		//<declare-styleable name="dashedline"> TypedArray
		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.dashedline);
		
		//������attrs.xml��<declare-styleable name="dashedline">�ڵ���
    	//�����<attr name="lineColor" format="color" />
        //��ʾ���������ΪlineColor����Ϊcolor�����û��ڲ����ļ��ж������趨ֵʱ
        //��ͨ��TypedArray�������ֵ���û�������ֵ�ǲ���Ĭ��ֵ0XFF00000
		int lineColor=a.getColor(R.styleable.dashedline_lineColor, 0XFFC0C0C0);
		a.recycle();
		this.paint=new Paint();
		this.path=new Path();
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setColor(lineColor);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(strokeWith);
	}
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		this.path.moveTo(0.0F, 0.0F);
		this.path.lineTo(getMeasuredWidth(), 0.0F);
		canvas.drawPath(this.path, this.paint);
	}
	

}
