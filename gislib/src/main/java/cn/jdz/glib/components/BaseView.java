package cn.jdz.glib.components;

/**
 * Created by admin on 2017/10/10.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
