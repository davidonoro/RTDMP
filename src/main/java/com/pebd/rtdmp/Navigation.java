package com.pebd.rtdmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 10/12/15.
 */
public class Navigation implements Serializable{


    private String user;
    private String pais;
    private String fecha;
    private String url;
    private String dominio;
    private List<String> categorias;

    public Navigation(){
        categorias = new ArrayList<String>();
    }

    public Navigation(String user, String pais, String fecha, String dominio,String url){
        this.user = user;
        this.pais = pais;
        this.fecha = fecha;
        this.url = url;
        this.dominio = dominio;
        categorias = new ArrayList<String>();
    }

    public void addCategoria(String cat){
        if(!categorias.contains(cat)){
            categorias.add(cat);
        }
    }

    public String printCategorias(){
        return categorias.toString();
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }
}
