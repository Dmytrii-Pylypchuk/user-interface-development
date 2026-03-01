const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('api', {
    loadFile: (callback) => ipcRenderer.on('load-file', (e, data) => callback(data)),
    clearEditor: (callback) => ipcRenderer.on('clear-editor', callback),
    notifyChange: () => ipcRenderer.send('content-changed'),
    saveToFile: (content) => ipcRenderer.invoke('save-to-file', content)
});