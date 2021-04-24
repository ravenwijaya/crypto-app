package com.raven.trcrypto.Model;

public class bank_item {
    private String nama_bank;
    private int logo_bank;

    public bank_item(String nama, int logo){
        this.nama_bank = nama;
        this.logo_bank = logo;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public void setLogo_bank(int logo_bank) {
        this.logo_bank = logo_bank;
    }


    public int getLogo_bank() {
        return logo_bank;
    }


}
