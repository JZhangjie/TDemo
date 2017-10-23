package cn.xinxizhan.test.tdemo.components;

import java.net.PortUnreachableException;

/**
 * Created by admin on 2017/10/10.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
