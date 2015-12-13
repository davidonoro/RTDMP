package com.pebd.rtdmp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 10/12/15.
 */
public class Navigation {


    private String user;
    private String pais;
    private String fecha;
    private String url;
    private List<String> categorias;

    public Navigation(String user, String pais, String fecha, String url){
        this.user = user;
        this.pais = pais;
        this.fecha = fecha;
        this.url = url;
        categorias = new ArrayList<String>();
    }

    public void addCategoria(String cat){
        if(!categorias.contains(cat)){
            categorias.add(cat);
        }
    }

    public String getCategorias(){
        return categorias.toString();
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
}
