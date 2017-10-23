package cn.xinxizhan.test.tdemo.components.casedetail;

import android.graphics.Bitmap;

import com.esri.core.map.Graphic;

import java.io.File;

import cn.xinxizhan.test.tdemo.components.BasePresenter;
import cn.xinxizhan.test.tdemo.components.BaseView;
import cn.xinxizhan.test.tdemo.components.dblist.DBListContract;
import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.data.model.DBFile;
import io.reactivex.Observable;

/**
 * Created by admin on 2017/10/11.
 */

public interface CaseDetailContract {

    interface Presenter extends BasePresenter{

        void setCurrentModel(DBFile currentDBFile);

        void setCurrentModel(DBCase currentDBCase);

        void setCurrentModel(int caseGraphicId);

        DBCase getCurrentDBCase();

        void saveDBCase();

        void showDBCase();

        String getDLMCFromDLBM(String dlbm);

        String getDCRAliasname();

        void showImages();

        void capture();

        void addImage(File file);

        void zoomToCurrentCase();

        void showCaseList();

        boolean canEdit();

    }

    interface View extends BaseView<CaseDetailContract.Presenter>{

        void showToastMessage(String message);

        void showCase(DBCase showDBCase);

        DBCase getCaseFromView();

        void capture(File file);

        void addImageView(Bitmap bitmap,File file);

        void clearImages();

    }


    interface Callback{

        void zoomToCurrentCase(Graphic graphic);

        void showCaseList();

    }

}
