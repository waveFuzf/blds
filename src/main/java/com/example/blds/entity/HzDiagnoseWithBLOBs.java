package com.example.blds.entity;

import lombok.Data;

@Data
public class HzDiagnoseWithBLOBs extends HzDiagnose {
    private String diagnose;

    private String mirrorView;

    private String immuneTag;

    private String immuneRemark;

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose == null ? null : diagnose.trim();
    }

    public String getMirrorView() {
        return mirrorView;
    }

    public void setMirrorView(String mirrorView) {
        this.mirrorView = mirrorView == null ? null : mirrorView.trim();
    }

    public String getImmuneTag() {
        return immuneTag;
    }

    public void setImmuneTag(String immuneTag) {
        this.immuneTag = immuneTag == null ? null : immuneTag.trim();
    }

    public String getImmuneRemark() {
        return immuneRemark;
    }

    public void setImmuneRemark(String immuneRemark) {
        this.immuneRemark = immuneRemark == null ? null : immuneRemark.trim();
    }
}