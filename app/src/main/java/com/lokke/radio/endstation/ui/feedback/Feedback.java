package com.lokke.radio.endstation.ui.feedback;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.google.gson.annotations.SerializedName;
import com.lokke.radio.endstation.BR;
import com.lokke.radio.endstation.R;

public class Feedback extends BaseObservable {

    private static final String TAG = "Feedback";
    @SerializedName("email")
    private String email;

    @SerializedName("title")
    private String subject;

    @SerializedName("description")
    private String message;

    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> subjectError = new ObservableField<>();
    public ObservableField<Integer> messageError = new ObservableField<>();

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }


    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
    }

    public void setSubject(String subject) {
        this.subject = subject;
        notifyPropertyChanged(BR.valid);
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public boolean isValid() {
        boolean valid = isEmailValid(false);
        valid = isSubjectValid(false) && valid;
        valid = isMessageValid(false) && valid;
        return valid;
    }

    public boolean isEmailValid(boolean setMessage) {
        // Minimum a@b.c
        if (email != null && email.length() > 5) {
            int indexOfAt = email.indexOf("@");
            int indexOfDot = email.lastIndexOf(".");
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length() - 1) {
                emailError.set(null);
                return true;
            } else {
                if (setMessage)
                    emailError.set(R.string.error_format_invalid);
                return false;
            }
        }
        if (setMessage)
            emailError.set(R.string.error_too_short);
        return false;
    }

    public boolean isSubjectValid(boolean setMessage) {
        if (subject != null && subject.length() > 0) {
            subjectError.set(null);
            return true;
        } else {
            if (setMessage) {
                Log.e(TAG, "isSubjectValid: set message true, empty field" );
                subjectError.set(R.string.error_field_empty);
            }
            return false;
        }
    }

    public boolean isMessageValid(boolean setMessage) {
        if (message != null && message.length() > 0) {
            messageError.set(null);
            return true;
        } else {
            if (setMessage)
                messageError.set(R.string.error_field_empty);
            return false;
        }
    }
}
