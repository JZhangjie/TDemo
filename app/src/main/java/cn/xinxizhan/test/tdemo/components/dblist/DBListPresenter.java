package cn.xinxizhan.test.tdemo.components.dblist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jdz.glib.data.dbaccess.sqlite.SqliteManager;
import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;
import cn.xinxizhan.test.tdemo.data.model.DBFile;
import cn.xinxizhan.test.tdemo.data.model.DBFileStatus;
import cn.xinxizhan.test.tdemo.utils.FileHelper;
import cn.xinxizhan.test.tdemo.utils.HttpHelper;
import cn.xinxizhan.test.tdemo.utils.PathHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2017/10/10.
 */

public class DBListPresenter implements DBListContract.Presenter {

    private DBListContract.View mView;
    private DBListContract.Callback mCallback;
    private boolean isFirst = true;
    private List<DBFile> mDBFiles;

    public DBListPresenter(DBListContract.View view, DBListContract.Callback callback) {
        this.mView = view;
        this.mCallback = callback;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (isFirst) {
            this.mDBFiles = new ArrayList<>();
            init();
            isFirst = false;
        }
        this.mView.setAdapterData(this.mDBFiles);
        this.mView.refreshAdapter();
    }

    private void init() {
        getDBFilesFromLocal().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<DBFile>>() {
                    @Override
                    public void accept(@NonNull List<DBFile> dbFiles) throws Exception {
                        mView.refreshAdapter();
                        mView.stopRefresh();
                    }
                });
    }

    @Override
    public void getDBFiles() {
        getDBFilesFromRemote().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<DBFile>>() {
                    @Override
                    public void accept(@NonNull List<DBFile> dbFiles) throws Exception {
                        mView.refreshAdapter();
                        mView.stopRefresh();
                    }
                });
    }

    private Observable<List<DBFile>> getDBFilesFromRemote() {
        return HttpHelper.getString(PathHelper.getDBFilesUrl()).map(new Function<String, List<DBFile>>() {
            @Override
            public List<DBFile> apply(@NonNull String s) throws Exception {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("datas");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    String name = o.getString("storname").trim();
                    String dbpath = FileHelper.getPath(ApplicationConstants.DBPATH + "/" + ApplicationConstants.USER.getXzqhdm() + "/" + name).getAbsolutePath();
                    if (!name.endsWith(SqliteManager.POSTFIX)) {
                        continue;
                    }
                    String link = ApplicationConstants.HOSTURL + o.getString("link");
                    DBFile dbFile = null;
                    for (DBFile tempdb : mDBFiles) {
                        if (tempdb.getDbpath().equals(dbpath)) {
                            dbFile = tempdb;
                            dbFile.setUrl(link);
                            break;
                        }
                    }
                    if (dbFile == null) {
                        File file = new File(dbpath);
                        dbFile = new DBFile();
                        dbFile.setUrl(link);
                        dbFile.setDbpath(dbpath);
                        dbFile.setName(name.substring(0, name.indexOf(".")));
                        dbFile.setZippath(file.getParent() + "/" + dbFile.getName() + ".zip");
                        mDBFiles.add(dbFile);
                    }
                }
                return mDBFiles;
            }
        });
    }

    private Observable<List<DBFile>> getDBFilesFromLocal() {
        return Observable.create(new ObservableOnSubscribe<List<DBFile>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBFile>> e) throws Exception {
                File dbs = PathHelper.getDBFilesPath();
                if (dbs.isDirectory() && dbs.listFiles() != null) {
                    for (File file : dbs.listFiles()) {
                        DBFile dbFile = null;
                        if (file.isFile() && file.getName().endsWith(SqliteManager.POSTFIX)) {
                            for (DBFile tempdb : mDBFiles) {
                                if (tempdb.getDbpath().equals(file.getPath())) {
                                    if (tempdb.getStatus() == DBFileStatus.NOTDOWMLOAD)
                                        tempdb.setStatus(DBFileStatus.DOWNLOADED);
                                    dbFile = tempdb;
                                    break;
                                }
                            }
                            if (dbFile == null) {
                                dbFile = new DBFile();
                                dbFile.setDbpath(file.getPath());
                                String name = file.getName();
                                dbFile.setStatus(DBFileStatus.DOWNLOADED);
                                dbFile.setName(name.substring(0, name.indexOf(".")));
                                dbFile.setZippath(file.getParent() + "/" + dbFile.getName() + ".zip");
                                mDBFiles.add(dbFile);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void downloadDBFile(final DBFile dbFile) {
        dbFile.setStatus(DBFileStatus.LOADING);
        this.mView.refreshAdapter();
        dbFile.download().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        dbFile.setStatus(DBFileStatus.DOWNLOADED);
                        mView.refreshAdapter();
                    }
                });
    }

    @Override
    public void toggleStatusDBFile(final DBFile dbFile) {
        if (dbFile.getStatus() == DBFileStatus.USING) {
            dbFile.setStatus(DBFileStatus.LOADED);
            mView.refreshAdapter();
            mCallback.onDBListToggleClick(null);
            return;
        }
        for (DBFile tempdbfile : mDBFiles) {
            if (tempdbfile.getStatus() == DBFileStatus.USING)
                tempdbfile.setStatus(DBFileStatus.LOADED);
        }
        dbFile.setStatus(DBFileStatus.LOADING);
        mView.refreshAdapter();
        dbFile.load().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        dbFile.setStatus(DBFileStatus.USING);
                        mView.refreshAdapter();
                        mCallback.onDBListToggleClick(dbFile);
                    }
                });
    }

    @Override
    public void deleteDBFile(DBFile dbFile) {
        this.mDBFiles.remove(dbFile);
        this.mView.refreshAdapter();
        File file = new File(dbFile.getDbpath());
        file.delete();
        this.mCallback.onDBListDeleteClick(dbFile);
    }

    @Override
    public void uploadDBFile(final DBFile dbFile) {
        if (dbFile != null && (dbFile.getStatus() == DBFileStatus.USING || dbFile.getStatus() == DBFileStatus.LOADED)) {
            dbFile.setStatus(DBFileStatus.LOADING);
            mView.refreshAdapter();
            dbFile.zip().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@NonNull String s) throws Exception {
                            dbFile.upload().observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Observer<String>() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {
                                        }

                                        @Override
                                        public void onNext(@NonNull String s) {
                                            dbFile.setStatus(DBFileStatus.UPLOADED);
                                            mView.refreshAdapter();
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            mView.showToastMessage("上传失败！");
                                        }

                                        @Override
                                        public void onComplete() {
                                        }
                                    });
                        }
                    });
        } else {
            mView.showToastMessage("加载db之后再上传。");
        }
    }

    @Override
    public boolean backPressed() {
        return mView.backPressed();
    }
}
