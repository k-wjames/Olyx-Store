package ke.co.ideagalore.olyxstore.commons;

import android.app.Activity;
import android.util.Patterns;
import android.widget.EditText;

import ke.co.ideagalore.olyxstore.R;

public class ValidateFields {

    CustomDialogs customDialogs=new CustomDialogs();

    public boolean validateEditTextFields(Activity activity, EditText editText) {
        String input = editText.getText().toString();
        if (!input.isEmpty()) {
            return true;
        } else {
            editText.setHint("This field cannot be empty ");
            editText.setHintTextColor(activity.getResources().getColor(R.color.accent));
            return false;
        }
    }

    public boolean validateEmailAddress(Activity activity, EditText editText) {
        String email = editText.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText.setHint("Please use a valid email address");
            editText.setHintTextColor(activity.getResources().getColor(R.color.accent));
            return false;
        } else {
            editText.setHint("This field cannot be empty");
            editText.setHintTextColor(activity.getResources().getColor(R.color.accent));
            return false;
        }
    }

}
