package com.sergio.lotto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sergio.lotto.service.LotteryNumberService;

public class ChatActivityFragment extends Fragment {
    EditText edtMessage;
    String userName = "user1";
    public ChatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        Button btnServiceStopped = (Button) v.findViewById(R.id.btnServiceStopped);
        btnServiceStopped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending to Chat Service: Leave", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                stopService();
            }
        });


        Button btnGenerateNumber = (Button) v.findViewById(R.id.btnGenerateNumber);
        btnGenerateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New Message Arrived...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                generateNumber();
            }
        });

        edtMessage = (EditText) v.findViewById(R.id.edtMessage);

        loadUserNameFromPreferences();

        return v;
    }

    private void loadUserNameFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName = prefs.getString(Constants.KEY_USER_NAME, "Name");
    }

    private void stopService(){
        Bundle data = new Bundle();
        data.putInt(LotteryNumberService.MSG_CMD, LotteryNumberService.CMD_STOP_SERVICE);
        Intent intent = new Intent(getContext(), LotteryNumberService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }

    //radom number need to be add here
    private void generateNumber(){
        Bundle data = new Bundle();
        data.putInt(LotteryNumberService.MSG_CMD, LotteryNumberService.CMD_GENERATE_NUMBER);
        Intent intent = new Intent(getContext(), LotteryNumberService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }
}