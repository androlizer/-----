package org.chinamil.ui.library;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView
{
  public AlwaysMarqueeTextView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public AlwaysMarqueeTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public AlwaysMarqueeTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private void init()
  {
    setSingleLine(true);
    TextUtils.TruncateAt localTruncateAt = TextUtils.TruncateAt.MARQUEE;
    setEllipsize(localTruncateAt);
    setMarqueeRepeatLimit(-1);
  }

  public boolean isFocused()
  {
    return true;
  }
}