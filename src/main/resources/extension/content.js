let lastSelectedText = "";

document.addEventListener("selectionchange", () => {
    const text = window.getSelection().toString().trim();

    if(text && text !== lastSelectedText) {
        lastSelectedText = text;
        chrome.runtime.sendMessage({ type: "TEXT_SELECTED", text: lastSelectedText });
    }

});