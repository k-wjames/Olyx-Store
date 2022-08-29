package ke.co.ideagalore.olyxstore.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import ke.co.ideagalore.olyxstore.R;

public class Dialogs {

    Dialog myDialog;

    public void showProgressDialog(Context context, String message) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.progress_dialog);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = myDialog.findViewById(R.id.tv_message);
        textView.setText(message);
        myDialog.show();
    }

    public void dismissProgressDialog(){
        myDialog.dismiss();
    }

}
