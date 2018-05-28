package security.zw.com.securitycheck.view;

import java.util.ArrayList;

import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;

/**
 * Created by wangshu on 17/8/24.
 */

public interface MyProjectView {

    void getListSucc(ArrayList<ProjectInfo> projectInfos, boolean has_more, int page);

    void getListFailed(int code, String error);


    void getProjectSucc(ProjectDetail detail);

    void getProjectFailed(int code, String error);

}
