package com.example.chrome_extension;

public class TranslationRequest {
    private String texte;
    private String targetLanguage;

    public TranslationRequest(){}

    public TranslationRequest(String texte){
        this.texte = texte;
    }

    public TranslationRequest(String texte, String targetLanguage){
        this.texte = texte;
        this.targetLanguage = targetLanguage;
    }

    //getter et setter
    public String getTexte(){ return texte;}
    public void setTexte(String texte){ this.texte = texte; }
    public String getTargetLanguage(){ return targetLanguage;}
    public void setTargetLanguage(String targetLanguage){ this.targetLanguage = targetLanguage; }
}
