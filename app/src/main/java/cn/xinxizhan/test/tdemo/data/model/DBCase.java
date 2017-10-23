package cn.xinxizhan.test.tdemo.data.model;

import com.esri.core.map.Graphic;

import java.util.Date;

import cn.xinxizhan.test.tdemo.data.common.Column;
import cn.xinxizhan.test.tdemo.data.common.ColumnAccessMode;

/**
 * Created by admin on 2017/10/9.
 */

public class DBCase {
    @Column(ColumnName = "FID",ColumnType = int.class)
    private int fid;
    @Column(ColumnName = "BSM",ColumnType = String.class)
    private String bsm;
    @Column(ColumnName = "Shape",ColumnType = String.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private String shape;
    private Graphic graphic;
    @Column(ColumnName = "XZQDM",ColumnType = String.class)
    private String xzqdm;
    @Column(ColumnName = "XZQMC",ColumnType = String.class)
    private String xzqmc;
    @Column(ColumnName = "CC",ColumnType = String.class)
    private String cc;
    @Column(ColumnName = "TAG",ColumnType = int.class)
    private int tag;
    @Column(ColumnName = "MJ",ColumnType = double.class)
    private double mj;
    @Column(ColumnName = "MEMO",ColumnType = String.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private String memo;
    @Column(ColumnName = "SFGD",ColumnType = int.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private int sfgd;
    @Column(ColumnName = "SJCC",ColumnType = String.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private String sjcc;
    @Column(ColumnName = "SFYDC",ColumnType = int.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private int sfydc;
    @Column(ColumnName = "DCR",ColumnType = String.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private String dcr;
    @Column(ColumnName = "DCSJ",ColumnType = Date.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private Date dcsj;
    @Column(ColumnName = "SFLH",ColumnType = int.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private int sflh;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getBsm() {
        return bsm;
    }

    public void setBsm(String bsm) {
        this.bsm = bsm;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getXzqdm() {
        return xzqdm;
    }

    public void setXzqdm(String xzqdm) {
        this.xzqdm = xzqdm;
    }

    public String getXzqmc() {
        return xzqmc;
    }

    public void setXzqmc(String xzqmc) {
        this.xzqmc = xzqmc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public double getMj() {
        return mj;
    }

    public void setMj(double mj) {
        this.mj = mj;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getSfgd() {
        return sfgd;
    }

    public void setSfgd(int sfgd) {
        this.sfgd = sfgd;
    }

    public String getSjcc() {
        return sjcc;
    }

    public void setSjcc(String sjcc) {
        this.sjcc = sjcc;
    }

    public int getSfydc() {
        return sfydc;
    }

    public void setSfydc(int sfydc) {
        this.sfydc = sfydc;
    }

    public String getDcr() {
        return dcr;
    }

    public void setDcr(String dcr) {
        this.dcr = dcr;
    }

    public Date getDcsj() {
        return dcsj;
    }

    public void setDcsj(Date dcsj) {
        this.dcsj = dcsj;
    }

    public int getSflh() {
        return sflh;
    }

    public void setSflh(int sflh) {
        this.sflh = sflh;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }
}
