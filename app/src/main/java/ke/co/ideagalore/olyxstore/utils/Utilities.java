package ke.co.ideagalore.olyxstore.utils;

import android.widget.EditText;
import android.widget.Toast;

public class Utilities {

    public boolean validateEditTexts(EditText editText, String message) {
        String s = editText.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(editText.getContext(), message + " field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
