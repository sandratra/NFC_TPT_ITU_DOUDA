package com.sharingame.utility;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sharingame.sharg.R;

public class DialogHelper {

    public static final String DIALOG_ERROR = "Erreur";
    public static final String DIALOG_WARNING = "Avertissement";
    public static final String DIALOG_INFO = "Information";

    private Dialog dialog;

    public DialogHelper(Context context){
        dialog = new Dialog(context);
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void showDialog(int v, String title, String message, final View.OnClickListener on_ok){
        dialog.setContentView(v);
        Button ok = dialog.findViewById(R.id.popup_btn_ok);
        TextView _title = dialog.findViewById(R.id.popup_title);
        TextView _message = dialog.findViewById(R.id.popup_message);
        _title.setText(title);
        _message.setText(message);
        if(on_ok == null){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        else ok.setOnClickListener(on_ok);
        dialog.show();
    }
}
