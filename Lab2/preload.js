const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('api', {
    sendModified: () => ipcRenderer.send('content-modified'),
    requestContent: () => ipcRenderer.invoke('request-content'),
    onLoadFile: (callback) => ipcRenderer.on('load-file', (e, data) => callback(data)),
    onClear: (callback) => ipcRenderer.on('clear-editor', callback),
    onGetContent: (callback) => ipcRenderer.on('get-content', callback)
});
