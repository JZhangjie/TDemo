package cn.xinxizhan.test.tdemo.data.model;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.popup.PopupUtil;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.xinxizhan.test.tdemo.data.base.DBManager;
import cn.xinxizhan.test.tdemo.utils.FileHelper;
import cn.xinxizhan.test.tdemo.utils.HttpHelper;
import cn.xinxizhan.test.tdemo.utils.PathHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by admin on 2017/10/9.
 */

public class DBFile extends GraphicsLayer {
    private DBFileStatus status = DBFileStatus.NOTDOWMLOAD;
    private String name;
    private String url;
    private String dbpath;
    private String zippath;
    private DBManager<DBCase> manager;

    public DBFile(){
        super(RenderingMode.STATIC);
    }

    //region 属性访问

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public DBFileStatus getStatus() {
        return status;
    }

    public void setStatus(DBFileStatus status) {
        this.status = status;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDbpath() {
        return dbpath;
    }

    public void setDbpath(String dbpath) {
        this.dbpath = dbpath;
    }

    public String getZippath() {
        return zippath;
    }

    public void setZippath(String zippath) {
        this.zippath = zippath;
    }

    public DBManager<DBCase> getManager() {
        return manager;
    }

    public void setManager(DBManager<DBCase> manager) {
        this.manager = manager;
    }

    //endregion

    public DBCase getCase(Object obj){
        if(manager!=null)
            return manager.getByUnique(obj);
        return null;
    }

    public DBCase getDefaultCase(){
        if(manager!=null && manager.size()>0)
            return manager.get(0);
        return null;
    }

    public DBCase getCaseByGraphic(Graphic graphic){
        for(DBCase dbCase:manager){
            if(dbCase.getGraphic().getUid()==graphic.getUid()){
                return dbCase;
            }
        }
        return null;
    }

    public DBCase getCaseByGraphic(int graphicid){
        Graphic graphic = getGraphic(graphicid);
        if(graphic==null)
            return null;
        for(DBCase dbCase:manager){
            if(dbCase.getGraphic().getUid()==graphic.getUid()){
                return dbCase;
            }
        }
        return null;
    }

    public Observable<String> download(){
        return HttpHelper.getFile(url,new File(dbpath)).map(new Function<File, String>() {
            @Override
            public String apply(@NonNull File file) throws Exception {
                return file.getName();
            }
        });
    }

    public Observable<Integer> load(){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(loadData());
            }
        });
    }

    private int loadData() throws IOException {
        if(manager==null){
            manager = new DBManager<>(dbpath,"gddc","BSM",DBCase.class);
            int r = manager.load();
            if(r>0){
                for(int i=0;i<manager.size();i++){
                    DBCase dbCase = manager.get(i);
                    JsonFactory f = new JsonFactory();
                    JsonParser jp = f.createJsonParser(dbCase.getShape());
                    Polygon p = (Polygon) GeometryEngine.jsonToGeometry(jp).getGeometry();
                    Graphic g = new Graphic(p, getSymbol(dbCase.getSfydc()));
                    int id =DBFile.this.addGraphic(g);
                    dbCase.setGraphic(DBFile.this.getGraphic(id));
                }
            }
            return r;
        }
        return 0;
    }

    public Observable<String> zip(){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                File zipFile = new File(zippath);
                if(zipFile.exists()){
                    zipFile.delete();
                }
                ZipOutputStream zipOutputStream = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile),new CRC32()));
                File db = new File(getDbpath());
                if(db.exists()){
                    zip(zipOutputStream,db,db.getName());
                }
                File images = new File(PathHelper.getImagePath().getPath()+"/"+getName());
                if(images.exists()){
                    for(File image :images.listFiles()){
                        zip(zipOutputStream,image,images.getName() + File.separator + image.getName());
                    }
                }
                zipOutputStream.flush();
                zipOutputStream.close();
                emitter.onNext(zippath);
            }
        });
    }
    private void zip(ZipOutputStream outputStream, File file, String name){
        try {
            if(file.isFile()){
                outputStream.putNextEntry(new ZipEntry(name));
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                while (fileInputStream.read(buffer)!=-1){
                    outputStream.write(buffer,0,buffer.length);
                }
                fileInputStream.close();
            }
            else if(file.isDirectory()){
                for(File f :file.listFiles()){
                    zip(outputStream,f,name+File.separator+f.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Observable<String> upload(){
        File file = new File(zippath);
        if(file==null || !file.exists())
            return null;
        HashMap<String,String> map = new HashMap<>();
        map.put("name",file.getName());
        return HttpHelper.postFile(url,file,"name",map);
    }

    public boolean update(DBCase dbCase){
        if(manager!=null && manager.contains(dbCase)){
            boolean result = manager.update(dbCase);
            if(result)
                updateGraphic(dbCase.getGraphic().getUid(),getSymbol(dbCase.getSfydc()));
            return result;
        }
        return false;
    }

    //符号
    private Symbol getSymbol(int sybmolFieldValue){
        SimpleFillSymbol fillSymbol;
        switch (sybmolFieldValue) {
            case 0:
                fillSymbol = new SimpleFillSymbol(0x55FF0000, SimpleFillSymbol.STYLE.SOLID);
                break;
            case 1:
                fillSymbol = new SimpleFillSymbol(0x5500ff00, SimpleFillSymbol.STYLE.SOLID);
                break;
            default:
                fillSymbol = new SimpleFillSymbol(Color.RED, SimpleFillSymbol.STYLE.CROSS);
        }
        return fillSymbol;
    }
}
