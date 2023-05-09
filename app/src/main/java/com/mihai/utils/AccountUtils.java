package com.mihai.utils;
import com.mihai.models.Account;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.Base64;
import java.util.List;

public class AccountUtils {
    public static int Authenticate(String address, String password) {
        List<Account> accountList = Select.from(Account.class).where(Condition.prop("address").eq(address)).list();
        if(accountList.stream().count() == 1) {
            // authorize user account
            if(password.compareTo(accountList.get(0).getPassword()) == 0) {
                Session.Authorize(accountList.get(0));
                return 1;
            }
            else
                /* passwords does not match */
                return 0;
        }

        // must to sign up
        return -1;
    }
}
