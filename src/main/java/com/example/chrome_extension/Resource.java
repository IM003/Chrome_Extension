package com.example.chrome_extension;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


@Path("/translate")
public class Resource {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TranslationRequest translatePrompt(TranslationRequest request) throws Exception {

        String texte = request.getTexte();
        //Recuperer la langue de destination
        String langue = request.getTargetLanguage();

        //Le prompt de traduction
        String prompt = String.format("Tu es un traducteur professionel. Traduit le texte suivant en %s."
                +"Donne directement la traduction! Pas d'explication, pas de commentaire: \"%s\"",
                langue, texte);

        //l'envoi à l'api
        GeminiService Gemini = new GeminiService();
        return new TranslationRequest(Gemini.callGemini(prompt));
    }
}