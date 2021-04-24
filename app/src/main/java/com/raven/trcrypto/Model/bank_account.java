package com.raven.trcrypto.Model;

public class bank_account {

    private String nama;
    private String rekening;
    private String logo;
    private boolean dipilih = false;
    public bank_account(String nama, String rekening, String logo,boolean dipilih){
        this.nama = nama;
        this.rekening = rekening;
        this.logo = logo;
        this.dipilih = dipilih;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



}
