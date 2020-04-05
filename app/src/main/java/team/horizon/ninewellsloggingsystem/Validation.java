package team.horizon.ninewellsloggingsystem;

import java.io.File;

class Validation {
    public boolean CheckIfEditTextEmpty(String text){
        if(text.equals("")){
            return true;
        }
        return false;
    }

    public boolean CheckIfEditTextIsDefault(String key, String value){
        if(key.equals(value)){
            return true;
        }
        return false;
    }
    public boolean CheckIfEnoughChars(String text, int length){
        if(text.length() < length){
            return false;
        }
        return true;
    }

    public boolean CheckIfFileExists(File path){
        if(path.exists()){
            return true;
        }
        return false;
    }
}
