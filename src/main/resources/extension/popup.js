document.addEventListener('DOMContentLoaded', () => {
    const translateButton = document.getElementById('translateButton');
    const resultDiv = document.getElementById('result');
    const selectDiv = document.getElementById('selected');
    const selectLanguage = document.getElementById('selectLanguage');

        
    translateButton.addEventListener('click', async () => {
        resultDiv.innerText = "Lecture de la sélection…";
        const langue = selectLanguage.value;

        // Récupérer le texte sélectionné dans l'onglet actif
        try {
            const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });

            chrome.scripting.executeScript(
                {
                    target: { tabId: tab.id },
                    func: () => window.getSelection().toString()
                },
                async (results) => {
                    if (!results || !results[0] || !results[0].result) {
                        selectDiv.innerText = "Aucun texte sélectionné.";
                        return;
                    }

                    const selectedText = results[0].result.trim();

                    if (selectedText.length === 0) {
                        selectDiv.innerText = "Sélection vide.";
                        return;
                    }
                    selectDiv.innerText = "Texte sélectionné : " + selectedText;
                    await sendToBackend(selectedText, langue);
                }
            );

        } catch (err) {
            console.error(err);
            resultDiv.innerText = "Erreur extension Chrome.";
        }
        
        
    });

    async function sendToBackend(text, langue) {
        resultDiv.innerText = "Traduction en cours…";

        try {
            const response = await fetch(
                "http://localhost:8080/Chrome_Extension-1.0-SNAPSHOT/app/translate",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        texte: text,
                        targetLanguage: langue
                    })
                }
            );

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            const data = await response.json();

            if (!data.texte) {
                throw new Error("Réponse serveur invalide (champ texte manquant)");
            }

            resultDiv.innerText = "Résultat : " + data.texte;

        } catch (err) {
            console.error(err);
            resultDiv.innerText = "Erreur : " + err.message;
        }
    }
});
