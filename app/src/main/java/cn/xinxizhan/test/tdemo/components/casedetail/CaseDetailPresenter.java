package cn.xinxizhan.test.tdemo.components.casedetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.w3c.dom.ProcessingInstruction;

import java.io.File;
import java.io.Flushable;
import java.util.Arrays;
import java.util.Date;

import cn.xinxizhan.test.tdemo.R;
import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.base.KeyValueItem;
import cn.xinxizhan.test.tdemo.data.model.DBCase;
import cn.xinxizhan.test.tdemo.data.model.DBFile;
import cn.xinxizhan.test.tdemo.data.model.User;
import cn.xinxizhan.test.tdemo.utils.DLBMHelper;
import cn.xinxizhan.test.tdemo.utils.FileHelper;
import cn.xinxizhan.test.tdemo.utils.PathHelper;
import cn.xinxizhan.test.tdemo.utils.StringHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 图斑详情管理类
 * Created by admin on 2017/10/13.
 */

public class CaseDetailPresenter implements CaseDetailContract.Presenter {

    private DBFile mCurrentDBFile;
    private DBCase mCurrentDBCase;
    private CaseDetailContract.View mView;
    private CaseDetailContract.Callback mCallback;
    private boolean isFirst=true;
    public static final String IMAGEPOSTFIX=".jpg";

    public CaseDetailPresenter(CaseDetailContract.View view,CaseDetailContract.Callback callback){
        this.mView = view;
        this.mView.setPresenter(this);
        this.mCallback =callback;
    }

    @Override
    public void start() {
        this.mView.showCase(this.mCurrentDBCase);
    }

    @Override
    public void setCurrentModel(DBFile currentDBFile) {
        this.mCurrentDBFile = currentDBFile;
    }

    @Override
    public void setCurrentModel(DBCase currentDBCase) {
        this.mCurrentDBCase = currentDBCase;
        showDBCase();
    }

    @Override
    public void setCurrentModel(int caseGraphicId) {
        if(this.mCurrentDBFile !=null){
            this.mCurrentDBCase = mCurrentDBFile.getCaseByGraphic(caseGraphicId);
            showDBCase();
        }
    }

    @Override
    public DBCase getCurrentDBCase() {
        return mCurrentDBCase;
    }

    @Override
    public void saveDBCase() {
        if(canEdit()){
            DBCase temp = mView.getCaseFromView();
            if(temp.getBsm()==null || temp.getBsm().equals("") || temp.getSjcc()==null || temp.getSjcc().equals("") || !hasImages())
            {
                mView.showToastMessage("信息不完整,或未拍照片");
                return;
            }
            DBCase mCaseModel = mCurrentDBCase;
            mCaseModel.setDcr(ApplicationConstants.USER.getUsername());
            mCaseModel.setMemo(temp.getMemo());
            mCaseModel.setDcsj(new Date());
            mCaseModel.setSfgd(temp.getSfgd());
            mCaseModel.setSflh(temp.getSflh());
            mCaseModel.setSjcc(temp.getSjcc());
            mCaseModel.setSfydc(1);
            if(mCurrentDBFile.update(mCurrentDBCase)){
                mView.showToastMessage("保存成功！");
            };
        }
        else {
            mView.showToastMessage("未选中文件或图斑");
        }
    }

    private boolean hasImages(){
        if(mCurrentDBFile!=null && mCurrentDBCase != null){
            File images = new File(PathHelper.getImagePath()+"/"+mCurrentDBCase.getBsm());
            if(images.exists() && images.isDirectory()){
                for(File image:images.listFiles()){
                    if(image.getName().endsWith(IMAGEPOSTFIX))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void showDBCase() {
        this.mView.showCase(this.mCurrentDBCase);
        showImages();
    }

    @Override
    public String getDLMCFromDLBM(String dlbm) {
        return DLBMHelper.FindValueByCode(dlbm,ApplicationConstants.CCBMList);
    }

    @Override
    public String getDCRAliasname() {
        User user = ApplicationConstants.HISTORYUSERS.getByUsername(mCurrentDBCase.getDcr());
        if(user !=null)
            return user.getAliasname();
        return "";
    }

    /**
     * 显示当前图斑的所有图片的缩略图
     */
    @Override
    public void showImages() {
        mView.clearImages();
        if(mCurrentDBFile!=null && mCurrentDBCase != null){
            File images = new File(PathHelper.getImagePath()+"/"+mCurrentDBCase.getBsm());

            if(images.exists() && images.isDirectory()){
                File[] templist = images.listFiles();
                Arrays.sort(templist);
                for(final File image: templist){
                    addImageView(image);
                }
            }
        }
    }

    /**
     * 添加图片缩略图
     * @param file
     */
    private void addImageView(final File file){
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=16;
                Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());

                e.onNext(bt);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        mView.addImageView(bitmap,file);
                    }
                });
    }

    /**
     * 拍照
     */
    @Override
    public void capture() {
        if(mCurrentDBFile != null && mCurrentDBCase != null)
        {
            File newImage = FileHelper.getPath(PathHelper.getImagePath(),"/"+ getCurrentDBCase().getBsm() +"/"+ StringHelper.getImageName());
            mView.capture(newImage);
        }
    }

    /**
     * 对拍摄的照片，异步压缩、添加缩略图
     * @param file
     */
    @Override
    public void addImage(final File file) {
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> e) throws Exception {
                FileHelper.compressImage(file);
                e.onNext(file);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File bitmap) throws Exception {
                        addImageView(file);
                    }
                });
    }

    @Override
    public void zoomToCurrentCase() {
        if(canEdit())
            mCallback.zoomToCurrentCase(mCurrentDBCase.getGraphic());
    }

    @Override
    public void showCaseList() {
        mCallback.showCaseList();
    }

    @Override
    public boolean canEdit() {
        return mCurrentDBFile!=null && mCurrentDBCase != null;
    }
}
