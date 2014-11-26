package com.sina.weibo.sdk.api.share.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * 发微博编辑框 
 */
@SuppressWarnings("unused")
public class EditBlogView extends EditText{

	private Context ctx;
	private List<OnSelectionListener> listeners;
	private int count; 
	private OnEnterListener mOnEnterListener;
	private boolean canSelectionChanged = true;
	
	public EditBlogView(Context context) {
		super(context);
		init();
	}

	public EditBlogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public EditBlogView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		ctx = getContext();
		listeners = new ArrayList<OnSelectionListener>();
	}
	
	public void setOnSelectionListener(OnSelectionListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		super.onSelectionChanged(selStart, selEnd);
	    if(!canSelectionChanged 
	    		|| listeners == null 
	    		|| listeners.isEmpty()) {
			return;
	    }
		for(OnSelectionListener l : listeners) {
			l.onSelectionChanged(selStart,selEnd);
		}
	}

	public void enableSelectionChanged(boolean enable) {
		this.canSelectionChanged = enable;
	}
	
	public void setOnEnterListener(OnEnterListener listener){
	    this.mOnEnterListener = listener;
	}
	
	public interface OnSelectionListener {
		
        void onSelectionChanged(int selStart, int selEnd);
        
    }
	
	public interface OnEnterListener {
        
        void onEnterKey();
        
    }

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode==KeyEvent.KEYCODE_ENTER && mOnEnterListener != null) {       	
            mOnEnterListener.onEnterKey();
        }
        // Handle all other keys in the default way
        return super.onKeyDown(keyCode, event);		
    }
	
	/**
	 * 
	 * 当每一行的末尾是表情图片，并且光标处在该表情后面时，
	 * 需要对光标位置做修正
	 */
    public int correctPosition(int pos) {
        if (pos == -1) {
            return pos;
        }

        Editable editable = getText();
        int length = editable.length();
        if (pos >= length) {
            return pos;
        }

        Object[] objs= editable.getSpans(pos, pos, ImageSpan.class);
        if (objs != null && objs.length != 0) {
            if (pos != editable.getSpanStart(objs[0])) {
                return editable.getSpanEnd(objs[0]);
            }
        }

        return pos;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = new InputConnectionWrapper(
                super.onCreateInputConnection(outAttrs), false) {

            @Override
            public boolean commitText(CharSequence text, int newCursorPosition) {
                Editable content = getEditableText();
                final String oldText = new String(content.toString());
                final int start = Selection.getSelectionStart(content);
                final int end = Selection.getSelectionEnd(content);
                if (start != -1 && end != -1) {
                    int correctStart = correctPosition(start);
                    int correctEnd = correctPosition(end);
                    
                    if (correctStart > correctEnd) {
                        int temp = correctStart;
                        correctStart = correctEnd;
                        correctEnd = temp;
                    }
                    if (correctStart != start || correctEnd != end) {
                        Selection.setSelection(content, correctStart, correctEnd);
                    }
                    if (correctStart != correctEnd) {
                        getText().delete(correctStart, correctEnd);
                    }
                }

                return super.commitText(text, newCursorPosition);
            }

            @Override
            public boolean setComposingText(CharSequence text,
                    int newCursorPosition) {
                Editable content = getEditableText();
                final String oldText = new String(content.toString());
                final int start = Selection.getSelectionStart(content);
                final int end = Selection.getSelectionEnd(content);
                
                if (start != -1 && end != -1) {
                    int correctStart = correctPosition(start);
                    int correctEnd = correctPosition(end);
                    
                    if (correctStart > correctEnd) {
                        int temp = correctStart;
                        correctStart = correctEnd;
                        correctEnd = temp;
                    }
                    if (correctStart != start || correctEnd != end) {
                        Selection.setSelection(content, correctStart, correctEnd);
                    }
                    if (correctStart != correctEnd) {
                        getText().delete(correctStart, correctEnd);
                    }
                }

                return super.setComposingText(text, newCursorPosition);
            }
        };
        return inputConnection;
    }
}
