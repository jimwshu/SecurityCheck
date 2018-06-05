package security.zw.com.securitycheck.view;

import java.util.ArrayList;

import security.zw.com.securitycheck.bean.CheckItem;
import security.zw.com.securitycheck.bean.CheckItemDetail;
import security.zw.com.securitycheck.bean.DecreaseItem;

/**
 * Created by wangshu on 17/8/24.
 */

public interface CheckItemView {

    void getCheckItemSucc(ArrayList<CheckItem> items);

    void getCheckItemFailed(int code, String error);

    void getCheckItemDetailSucc(CheckItemDetail detail);

    void getCheckItemDetailFailed(int code, String error);

}
