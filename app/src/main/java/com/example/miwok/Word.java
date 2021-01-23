package com.example.miwok;

public class Word {
    private String defualtTranslation;
    private String miwokTranslation;
    private int imgAddress = NO_IMAGE_STATE;
    private int  audioAdress;

    private static final int NO_IMAGE_STATE = -1;

    public Word(String defualt, String miwok, int audioId){
        defualtTranslation = defualt;
        miwokTranslation = miwok;
        audioAdress = audioId;
    }
    public Word(String defualt, String miwok, int imgAd, int audioId){
        defualtTranslation = defualt;
        miwokTranslation = miwok;
        imgAddress = imgAd;
        audioAdress = audioId;

    }
    public  int getAudioAdress(){ return audioAdress;}
    public String getMiwokTranslation(){
        return miwokTranslation;
    }

    public String getDefaultTranslation() {
        return defualtTranslation;
    }

    public int getImgAddress(){return imgAddress;}

    public boolean hasImage(){return imgAddress != NO_IMAGE_STATE;};

}
