package p5.bank;

import javafx.scene.control.Label;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringHelper {

    public static String trimAccountName(Label label) {
        String accountString = label.toString();
        accountString = accountString.substring(accountString.indexOf("'")+1);
        accountString.trim();
        accountString = accountString.replaceAll("'", "");
        return accountString;
    }

    public static boolean checkValid(String string) {
        if(Character.isDigit(string.charAt(0)))
            return false;
        Pattern p = Pattern.compile("[^A-Za-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return !m.find();
    }
}
