package cn.xinxizhan.test.tdemo.data.model;

import com.esri.core.map.Graphic;

import java.util.Date;

import cn.jdz.glib.data.dbaccess.Column;
import cn.jdz.glib.data.dbaccess.ColumnAccessMode;


/**
 * Created by admin on 2017/10/9.
 */

public class DBCase {
    @Column(ColumnName = "FID",ColumnType = "text")
    private int fid;
    @Column(ColumnName = "Shape",ColumnType = "text",AccessMode = ColumnAccessMode.ReadAndWrite)
    private String shape;
    @Column(ColumnName = "BSM",ColumnType = "text")
    private String bsm;
    private Graphic graphic;
    @Column(ColumnName = "CC",ColumnType = "text")
    private String cc;
    @Column(ColumnName = "TAG",ColumnType = "text")
    private int tag;
    @Column(ColumnName = "MJ",ColumnType = "real")
    private double mj;
    @Column(ColumnName = "XZQDM",ColumnType = "text")
    private String xzqdm;
    @Column(ColumnName = "XZQMC",ColumnType = "text")
    private String xzqmc;
    @Column(ColumnName = "DKLX",ColumnType = "text")
    private String dklx;
    @Column(ColumnName = "PDDL",ColumnType = "text")
    private String pddl;
    @Column(ColumnName = "SFYDC",ColumnType = "integer",AccessMode = ColumnAccessMode.ReadAndWrite)
    private int sfydc;
    @Column(ColumnName = "SJCC",ColumnType = "text",AccessMode = ColumnAccessMode.ReadAndWrite)
    private String sjcc;
    @Column(ColumnName = "SFGD",ColumnType = "integer",AccessMode = ColumnAccessMode.ReadAndWrite)
    private int sfgd;
    @Column(ColumnName = "SFLH",ColumnType = "integer",AccessMode = ColumnAccessMode.ReadAndWrite)
    private int sflh;
    @Column(ColumnName = "YBSM",ColumnType = "text")
    private String ybsm;
    @Column(ColumnName = "DCR",ColumnType = "text",AccessMode = ColumnAccessMode.ReadAndWrite)
    private String dcr;
    @Column(ColumnName = "DCSJ",ColumnType = "datetime",AccessMode = ColumnAccessMode.ReadAndWrite)
    private Date dcsj;
    @Column(ColumnName = "MEMO",ColumnType = "text",AccessMode = ColumnAccessMode.ReadAndWrite)
    private String memo;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getBsm() {
        return bsm;
    }

    public void setBsm(String bsm) {
        this.bsm = bsm;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
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

    public String getDklx() {
        return dklx;
    }

    public void setDklx(String dklx) {
        this.dklx = dklx;
    }

    public String getPddl() {
        return pddl;
    }

    public void setPddl(String pddl) {
        this.pddl = pddl;
    }

    public int getSfydc() {
        return sfydc;
    }

    public void setSfydc(int sfydc) {
        this.sfydc = sfydc;
    }

    public String getSjcc() {
        return sjcc;
    }

    public void setSjcc(String sjcc) {
        this.sjcc = sjcc;
    }

    public int getSfgd() {
        return sfgd;
    }

    public void setSfgd(int sfgd) {
        this.sfgd = sfgd;
    }

    public int getSflh() {
        return sflh;
    }

    public void setSflh(int sflh) {
        this.sflh = sflh;
    }

    public String getYbsm() {
        return ybsm;
    }

    public void setYbsm(String ybsm) {
        this.ybsm = ybsm;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
