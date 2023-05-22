package com.melodybeauty.melody_mobile.Model;

public class User {
    private String id;
    private String name;
    private String image;
    private String email;
    private String jenisKelamin;
    private String jenisKulit;
    private String tanggalLahir;
    private String noHp;
    private String Alamat;
    private String createdAt;
    private String updateAt;

    public User(String id, String name, String image, String email, String jenisKelamin, String jenisKulit, String tanggalLahir, String noHp, String alamat, String createdAt, String updateAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.jenisKulit = jenisKulit;
        this.tanggalLahir = tanggalLahir;
        this.noHp = noHp;
        Alamat = alamat;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getJenisKulit() {
        return jenisKulit;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getAlamat() {
        return Alamat;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }
}
