package tcc.meuscout.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ranking implements Serializable, Parcelable {
    private String pos_sigla, rank;
    private Integer id;
    private Double gols90, ass90, ch90, chcer90, chcerpct, chgol, chcergol, penpct,
            des90, cor90, bloq90, falsof, falcom, jgsca, jgscv, defpct, chcer90gol, golsof90, cs, def90, pendefpct;

    public Ranking(Parcel in) {
        pos_sigla = in.readString();
        rank = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            gols90 = null;
        } else {
            gols90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            ass90 = null;
        } else {
            ass90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            ch90 = null;
        } else {
            ch90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            chcer90 = null;
        } else {
            chcer90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            chcerpct = null;
        } else {
            chcerpct = in.readDouble();
        }
        if (in.readByte() == 0) {
            chgol = null;
        } else {
            chgol = in.readDouble();
        }
        if (in.readByte() == 0) {
            chcergol = null;
        } else {
            chcergol = in.readDouble();
        }
        if (in.readByte() == 0) {
            penpct = null;
        } else {
            penpct = in.readDouble();
        }
        if (in.readByte() == 0) {
            des90 = null;
        } else {
            des90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            cor90 = null;
        } else {
            cor90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            bloq90 = null;
        } else {
            bloq90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            falsof = null;
        } else {
            falsof = in.readDouble();
        }
        if (in.readByte() == 0) {
            falcom = null;
        } else {
            falcom = in.readDouble();
        }
        if (in.readByte() == 0) {
            jgsca = null;
        } else {
            jgsca = in.readDouble();
        }
        if (in.readByte() == 0) {
            jgscv = null;
        } else {
            jgscv = in.readDouble();
        }
        if (in.readByte() == 0) {
            defpct = null;
        } else {
            defpct = in.readDouble();
        }
        if (in.readByte() == 0) {
            chcer90gol = null;
        } else {
            chcer90gol = in.readDouble();
        }
        if (in.readByte() == 0) {
            golsof90 = null;
        } else {
            golsof90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            cs = null;
        } else {
            cs = in.readDouble();
        }
        if (in.readByte() == 0) {
            def90 = null;
        } else {
            def90 = in.readDouble();
        }
        if (in.readByte() == 0) {
            pendefpct = null;
        } else {
            pendefpct = in.readDouble();
        }
    }

    public static final Creator<Ranking> CREATOR = new Creator<Ranking>() {
        @Override
        public Ranking createFromParcel(Parcel in) {
            return new Ranking(in);
        }

        @Override
        public Ranking[] newArray(int size) {
            return new Ranking[size];
        }
    };

    public Ranking() {

    }

    public String getPos_sigla() {
        return pos_sigla;
    }

    public void setPos_sigla(String pos_sigla) {
        this.pos_sigla = pos_sigla;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getGols90() {
        return gols90;
    }

    public void setGols90(Double gols90) {
        this.gols90 = gols90;
    }

    public Double getAss90() {
        return ass90;
    }

    public void setAss90(Double ass90) {
        this.ass90 = ass90;
    }

    public Double getCh90() {
        return ch90;
    }

    public void setCh90(Double ch90) {
        this.ch90 = ch90;
    }

    public Double getChcer90() {
        return chcer90;
    }

    public void setChcer90(Double chcer90) {
        this.chcer90 = chcer90;
    }

    public Double getChcerpct() {
        return chcerpct;
    }

    public void setChcerpct(Double chcerpct) {
        this.chcerpct = chcerpct;
    }

    public Double getChgol() {
        return chgol;
    }

    public void setChgol(Double chgol) {
        this.chgol = chgol;
    }

    public Double getChcergol() {
        return chcergol;
    }

    public void setChcergol(Double chcergol) {
        this.chcergol = chcergol;
    }

    public Double getPenpct() {
        return penpct;
    }

    public void setPenpct(Double penpct) {
        this.penpct = penpct;
    }

    public Double getDes90() {
        return des90;
    }

    public void setDes90(Double des90) {
        this.des90 = des90;
    }

    public Double getCor90() {
        return cor90;
    }

    public void setCor90(Double cor90) {
        this.cor90 = cor90;
    }

    public Double getBloq90() {
        return bloq90;
    }

    public void setBloq90(Double bloq90) {
        this.bloq90 = bloq90;
    }

    public Double getFalsof() {
        return falsof;
    }

    public void setFalsof(Double falsof) {
        this.falsof = falsof;
    }

    public Double getFalcom() {
        return falcom;
    }

    public void setFalcom(Double falcom) {
        this.falcom = falcom;
    }

    public Double getJgsca() {
        return jgsca;
    }

    public void setJgsca(Double jgsca) {
        this.jgsca = jgsca;
    }

    public Double getJgscv() {
        return jgscv;
    }

    public void setJgscv(Double jgscv) {
        this.jgscv = jgscv;
    }

    public Double getDefpct() {
        return defpct;
    }

    public void setDefpct(Double defpct) {
        this.defpct = defpct;
    }

    public Double getChcer90gol() {
        return chcer90gol;
    }

    public void setChcer90gol(Double chcer90gol) {
        this.chcer90gol = chcer90gol;
    }

    public Double getGolsof90() {
        return golsof90;
    }

    public void setGolsof90(Double golsof90) {
        this.golsof90 = golsof90;
    }

    public Double getCs() {
        return cs;
    }

    public void setCs(Double cs) {
        this.cs = cs;
    }

    public Double getDef90() {
        return def90;
    }

    public void setDef90(Double def90) {
        this.def90 = def90;
    }

    public Double getPendefpct() {
        return pendefpct;
    }

    public void setPendefpct(Double pendefpct) {
        this.pendefpct = pendefpct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pos_sigla);
        dest.writeString(rank);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (gols90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(gols90);
        }
        if (ass90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(ass90);
        }
        if (ch90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(ch90);
        }
        if (chcer90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(chcer90);
        }
        if (chcerpct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(chcerpct);
        }
        if (chgol == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(chgol);
        }
        if (chcergol == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(chcergol);
        }
        if (penpct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(penpct);
        }
        if (des90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(des90);
        }
        if (cor90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(cor90);
        }
        if (bloq90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(bloq90);
        }
        if (falsof == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(falsof);
        }
        if (falcom == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(falcom);
        }
        if (jgsca == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(jgsca);
        }
        if (jgscv == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(jgscv);
        }
        if (defpct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(defpct);
        }
        if (chcer90gol == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(chcer90gol);
        }
        if (golsof90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(golsof90);
        }
        if (cs == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(cs);
        }
        if (def90 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(def90);
        }
        if (pendefpct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(pendefpct);
        }
    }
}
