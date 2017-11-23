package cn.jdz.glib.data.gis;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 通过url获取wmts服务的信息，用于构建WMTSLayer图层对象
 * Created by admin on 2017/10/31.
 */

public class WMTSLayerInfoInitiator {
    private String mUrl;
    String mLayername;
    String mLayerID;
    String mStyle;
    String mTileMatrix;
    String mDisplayLevels;
    String mImageFormat;
    int mSpatialReferenceCode;
    Envelope mExtent;
    private String mVersion="1.0.0";

    Namespace xmlns_ogc = Namespace.get("http://www.opengis.net/ows/1.1");
    Namespace xmlns_wmts = Namespace.get("http://www.opengis.net/wmts/1.0");

    public WMTSLayerInfoInitiator(){
    }

    public WMTSLayerInfoInitiator(String url){
        setUrl(url);
    }

    public WMTSLayerInfoInitiator setUrl(String url){
        mUrl = url.contains("?")?url:url+"?";
        return this;
    }

    /**
     * 构造切片的获取地址
     * @param level
     * @param row
     * @param col
     * @return
     */
    public String getTileUrl(int level, int row, int col) {
        String str = "%sSERVICE=WMTS&REQUEST=GetTile&VERSION=%s&LAYER=%s&STYLE=%s&TILEMATRIXSET=%s&TILEMATRIX=%s&TILEROW=%s&TILECOL=%s&FORMAT=%s";
        str = String.format(str, this.mUrl, this.mVersion, this.mLayerID, this.mStyle, this.mTileMatrix, level, row, col, this.mImageFormat);
        return str;
    }

    /**
     * 通过url获取wmts服务配置信息
     * @return
     */
    public void init(){
        Document doc = null;
        URL getUrl = null;
        try {
            getUrl = new URL(this.mUrl + "SERVICE=WMTS&VERSION=" + this.mVersion + "&REQUEST=GetCapabilities");
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            SAXReader saxReader = new SAXReader();
            doc = saxReader.read(reader);
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化图层信息失败！");
        }
        Element root = doc.getRootElement();
        Element Contents = root.element("Contents");
        Element LayerEle = Contents.element("Layer");

        // 获取标题
        mLayername = LayerEle.element(DocumentHelper.createQName("Title", xmlns_ogc)).getText();

        // 获取图层ID号
        mLayerID = LayerEle.element(DocumentHelper.createQName("Identifier", xmlns_ogc)).getText();

        // 获取图层样式
        Element style_element = LayerEle.element(DocumentHelper.createQName("Style", xmlns_wmts));
        mStyle = style_element.element(DocumentHelper.createQName("Identifier", xmlns_ogc)).getText();

        // 获取图片格式
        mImageFormat = LayerEle.element(DocumentHelper.createQName("Format", xmlns_wmts)).getText();

        // 获取图层范围
        Element BoundingBoxEle = LayerEle.element(DocumentHelper.createQName("BoundingBox", xmlns_ogc));
        String LowerCornerStr = BoundingBoxEle.element(DocumentHelper.createQName("LowerCorner", xmlns_ogc)).getText();
        String UpperCornerStr = BoundingBoxEle.element(DocumentHelper.createQName("UpperCorner", xmlns_ogc)).getText();
        String[] strs = LowerCornerStr.split(" ");
        Point lowerPoint = new Point(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]));
        strs = UpperCornerStr.split(" ");
        Point upperPoint = new Point(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]));
        if (mExtent == null)
        {
            mExtent = new Envelope(lowerPoint.getX(), lowerPoint.getY(), upperPoint.getX(), upperPoint.getY());
        }

        Element TileMatrixSetEle = Contents.element(DocumentHelper.createQName("TileMatrixSet", xmlns_wmts));
        // 获取金字塔名称
        mTileMatrix = TileMatrixSetEle.element(DocumentHelper.createQName("Identifier", xmlns_ogc)).getText();

        // 获取空间参考
        // urn:ogc:def:crs:EPSG::4490
        String str = TileMatrixSetEle.element(DocumentHelper.createQName("SupportedCRS", xmlns_ogc)).getText();
        if (str.contains("EPSG"))
        {
            int wkid = Integer.parseInt(str.substring(str.lastIndexOf(':') + 1));
            mSpatialReferenceCode = wkid;
        }

        // 获取显示级别
        if (mDisplayLevels == null || mDisplayLevels.isEmpty())
        {
            String display_levels = "";
            List<Element> matrix_level = TileMatrixSetEle.elements(DocumentHelper.createQName("TileMatrix", xmlns_wmts));
            for (Element xEle : matrix_level)
            {
                String level = xEle.element(DocumentHelper.createQName("Identifier", xmlns_ogc)).getText();
                display_levels += "," + level;
            }
            if (!display_levels.isEmpty())
            {
                display_levels = display_levels.substring(1);
            }
            mDisplayLevels = display_levels;
        }
    }
}
