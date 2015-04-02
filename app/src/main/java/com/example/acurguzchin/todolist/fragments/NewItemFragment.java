package com.example.acurguzchin.todolist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.acurguzchin.todolist.ClearableEditText;
import com.example.acurguzchin.todolist.R;

/**
 * Created by acurguzchin on 31.03.15.
 */
public class NewItemFragment extends Fragment {
    private OnNewItemAddedListener onNewItemAddedListener;

    public interface OnNewItemAddedListener {
        public void onNewItemAdded(String newItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_item_fragment, container, false);

        final EditText myEditText = ((ClearableEditText) view.findViewById(R.id.myEditText)).getEditText();
        myEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
                            keyCode == KeyEvent.KEYCODE_ENTER) {
                        String newItem = myEditText.getText().toString();
                        onNewItemAddedListener.onNewItemAdded(newItem);
                        myEditText.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onNewItemAddedListener = (OnNewItemAddedListener) getActivity();
    }
}
