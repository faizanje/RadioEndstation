package com.lokke.radio.endstation.ui.songrequest;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.lokke.radio.endstation.BR;
import com.lokke.radio.endstation.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SongRequest extends BaseObservable {

    public ObservableField<Integer> nameError = new ObservableField<>();
    public ObservableField<Integer> request1Error = new ObservableField<>();
    public ObservableField<Integer> request2Error = new ObservableField<>();
    private String name, request1, request2, message;

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.valid);

    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.valid);

    }

    public String getRequest1() {
        return request1;
    }

    public void setRequest1(String request1) {
        this.request1 = request1;
        notifyPropertyChanged(BR.valid);
    }

    public String getRequest2() {
        return request2;
    }

    public void setRequest2(String request2) {
        this.request2 = request2;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public boolean isValid() {
        boolean valid = isNameValid(false);
        valid = isRequest1Valid(false) && valid;
        valid = isRequest2Valid(false) && valid;
        return valid;
    }

    public boolean isNameValid(boolean setMessage) {
        if (name != null && name.length() > 0) {
            nameError.set(null);
            return true;
        } else {
            if (setMessage) {
                nameError.set(R.string.error_field_empty);
            }
            return false;
        }
    }

    public boolean isRequest1Valid(boolean setMessage) {
        if (request1 != null && request1.length() > 0) {
            request1Error.set(null);
            return true;
        } else {
            if (setMessage) {
                request1Error.set(R.string.error_field_empty);
            }
            return false;
        }
    }

    public boolean isRequest2Valid(boolean setMessage) {
        if (request2 != null && request2.length() > 0) {
            request2Error.set(null);
            return true;
        } else {
            if (setMessage) {
                request2Error.set(R.string.error_field_empty);
            }
            return false;
        }
    }

    public JSONObject getContent(){
        JSONObject jsonObject = new JSONObject();
        String content = String.format("Name: %s\nRequest 1: %s\nRequest 2: %s\nMessage: %s", name,request1,request2,message);
        try {
            jsonObject.put("content",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public HashMap<String, Object> body(){
        HashMap<String, Object> hashMap = new HashMap<>();
        String content = String.format("Name: %s\nRequest 1: %s\nRequest 2: %s\nMessage: %s", name,request1,request2,message);
        hashMap.put("content", content);
        return hashMap;
    }


}
