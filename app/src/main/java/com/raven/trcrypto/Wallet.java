package com.raven.trcrypto;

public class Wallet {
    private String rp;
    private String btc;
    private String doge;
    private String ada;
    private String dot;
    private String uni;
    private String link;
    private String aave;
    private String rune;
    private String sushi;
    public Wallet() {

    }
    public Wallet(String rp, String btc, String doge, String ada, String dot, String uni, String link, String aave, String rune, String sushi) {
        this.rp = rp;
        this.btc = btc;
        this.doge = doge;
        this.ada = ada;
        this.dot = dot;
        this.uni = uni;
        this.link = link;
        this.aave = aave;
        this.rune = rune;
        this.sushi = sushi;
    }

    public String getRp() {
        return rp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getDoge() {
        return doge;
    }

    public void setDoge(String doge) {
        this.doge = doge;
    }

    public String getAda() {
        return ada;
    }

    public void setAda(String ada) {
        this.ada = ada;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAave() {
        return aave;
    }

    public void setAave(String aave) {
        this.aave = aave;
    }

    public String getRune() {
        return rune;
    }

    public void setRune(String rune) {
        this.rune = rune;
    }

    public String getSushi() {
        return sushi;
    }

    public void setSushi(String sushi) {
        this.sushi = sushi;
    }
}
