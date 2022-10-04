/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.model.ext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Private
 * @param <T>
 */
public final class AngularModel<T> {

    private String note;
    private ArrayList<T> datas;
    private ArrayList<HashMap> remap;
    private Integer totalRow;
    private ResponResult result;
    private ArrayList<ModelEnum> myEnum;
    HashMap<String, Boolean> roles;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<T> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<T> datas) {
        this.datas = datas;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = gson.toJson(this);
        return str;
    }

    public ResponResult getResult() {
        return result;
    }

    public void setResult(ResponResult result) {
        this.result = result;
    }

    public ArrayList<ModelEnum> getMyEnum() {
        return myEnum;
    }

    public void setMyEnum(ArrayList<ModelEnum> myEnum) {
        this.myEnum = myEnum;
    }

    public HashMap<String, Boolean> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<String, Boolean> roles) {
        this.roles = roles;
    }

    public ArrayList<HashMap> getRemap() {
        return remap;
    }

    public void setRemap(ArrayList<HashMap> remap) {
        this.remap = remap;
    }

}
