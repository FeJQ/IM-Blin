package com.fejq.blin.view.fragment.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fejq.blin.R;
import com.fejq.blin.view.activity.ChatActivity;


public class ChatInputFragment extends Fragment
{


    public ChatInputFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ChatActivity activity = (ChatActivity)getActivity();
        Button buttonSend = activity.findViewById(R.id.btn_chat_send);
        EditText editTextContent = activity.findViewById(R.id.edt_chat_content);
        editTextContent.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String cotnent = String.valueOf(s);
                if (cotnent.isEmpty())
                {
                    buttonSend.setEnabled(false);
                }
                else
                {
                    buttonSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        buttonSend.setOnClickListener(v -> {
            activity.getViewModel().onMessageSend();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chat_input, container, false);
    }
}