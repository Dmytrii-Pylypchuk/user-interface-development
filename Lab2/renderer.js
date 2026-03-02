const editor = document.getElementById('editor');

editor.addEventListener('input', () => {
    window.api.sendModified();
});

window.api.onLoadFile((content) => {
    editor.value = content;
});

window.api.onClear(() => {
    editor.value = '';
});

window.api.onGetContent(() => {
    return editor.value;
});
