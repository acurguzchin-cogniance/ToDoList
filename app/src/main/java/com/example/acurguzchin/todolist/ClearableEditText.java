package com.example.acurguzchin.todolist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by acurguzchin on 31.03.15.
 */
public class ClearableEditText extends LinearLayout {
    private EditText editText;

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EditText getEditText() {
        return editText;
    }

    private void init(Context context) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.clearable_edit_text, this, true);
        editText = (EditText) view.findViewById(R.id.editText);
        Button button = (Button) view.findViewById(R.id.clearButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }
}
