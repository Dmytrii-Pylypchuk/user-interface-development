const editor = document.getElementById('editor');

window.api.loadFile((content) => {
    editor.value = content;
});

window.api.clearEditor(() => {
    editor.value = '';
});

editor.addEventListener('input', () => {
    window.api.notifyChange();
});