package cn.jdz.glib.data.gis;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by admin on 2017/10/31.
 */

public class TWMTSLayer extends TiledServiceLayer {
    private WMTSLayerInfoInitiator mInitiator;
    private TileInfo mTileInfo ;
    int mDPI=96;
    int mTileWidth=256;
    int mTileHeight=256;
    int mSpatialReferenceCode = 4610;
    int[] mShowLevels={1,2,3,4,5,6,7,8, 9, 10, 11, 12, 13, 14, 15, 16};
    private Point mOriginPoint = new Point(-180,90);
    Envelope mExtent = new Envelope(109.57763671875,20.126953125,117.3779296875,25.521240234375);
    private static final double[] res = {
            1.40625000000595,
            0.703125,
            0.3515625,
            0.17578125,
            0.087890625,
            0.0439453125,
            0.02197265625,
            0.010986328125,
            0.0054931640625,
            0.00274658203125,
            0.001373291015625,
            0.0006866455078125,
            0.00034332275390625,
            0.000171661376953125,
            0.0000858306884765625,
            0.0000429153442382813,
            0.0000214576721191406,
            0.0000107288360595703,
            0.00000536441802978516,
            0.00000268220901489258,
            0.00000134110450744629
    };
    private static final double[] scale = {
            590995186.12,
            295497593.05875,
            147748796.529375,
            73874398.2646875,
            36937199.1323438,
            18468599.5661719,
            9234299.78308594,
            4617149.89154297,
            2308574.94577148,
            1154287.47288574,
            577143.736442871,
            288571.868221436,
            144285.934110718,
            72142.9670553589,
            36071.4835276794,
            18035.7417638397,
            9017.87088191986,
            4508.93544095993,
            2254.46772047997,
            1127.23386023998,
            563.616930119991
    };

    public TWMTSLayer(String url) {
        this(url,true);
    }

    public TWMTSLayer(String url,boolean initLayer) {
        super("");
        mInitiator = new WMTSLayerInfoInitiator(url);
        setCredentials(null);
        if (initLayer)
            try {
                getServiceExecutor().submit(new Runnable() {
                    public final void run() {
                        a.initLayer();
                    }
                    final TWMTSLayer a;
                    {
                        a = TWMTSLayer.this;
                    }
                });
            } catch (RejectedExecutionException _ex) {
            }
    }

    protected void initLayer() {
        mInitiator.init();
        mTileInfo = new TileInfo(mOriginPoint,scale,res,21,mDPI,mTileWidth,mTileHeight);
        this.setTileInfo(mTileInfo);
        this.setFullExtent(mExtent);
        this.setDefaultSpatialReference(SpatialReference.create(mSpatialReferenceCode));
        super.initLayer();
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        for(int i :mShowLevels){
            if(i==level){
                String url = mInitiator.getTileUrl(level, row, col);
                Map<String, String> map = null;
                return com.esri.core.internal.io.handler.a.a(url, map);
            }
        }
        return new byte[0];
    }
}
