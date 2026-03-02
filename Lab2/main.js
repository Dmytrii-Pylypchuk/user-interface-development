const { app, BrowserWindow, Menu, dialog, ipcMain } = require('electron');
const fs = require('fs');
const path = require('path');

let mainWindow;
let currentFilePath = null;
let isModified = false;

function createWindow() {
    mainWindow = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            preload: path.join(__dirname, 'preload.js')
        }
    });

    mainWindow.loadFile('index.html');
}

async function askToSaveIfNeeded() {
    if (!isModified) return true;

    const result = await dialog.showMessageBox(mainWindow, {
        type: 'question',
        buttons: ['Save', 'Don’t Save', 'Cancel'],
        defaultId: 0,
        cancelId: 2,
        message: 'You have unsaved changes. Save before continuing?'
    });

    if (result.response === 0) {
        return await saveFile();
    }
    if (result.response === 1) return true;

    return false;
}

async function saveFile() {
    if (!currentFilePath) {
        const { filePath } = await dialog.showSaveDialog({
            filters: [{ name: 'Text Files', extensions: ['txt'] }]
        });

        if (!filePath) return false;
        currentFilePath = filePath;
    }

    const content = await mainWindow.webContents.invoke('request-content');
    fs.writeFileSync(currentFilePath, content);

    isModified = false;
    return true;
}

ipcMain.handle('request-content', async () => {
    return await mainWindow.webContents.send('get-content');
});

ipcMain.on('content-modified', () => {
    isModified = true;
});

app.whenReady().then(() => {
    createWindow();

    const template = [
        {
            label: 'File',
            submenu: [
                {
                    label: 'New',
                    click: async () => {
                        if (!(await askToSaveIfNeeded())) return;

                        currentFilePath = null;
                        mainWindow.webContents.send('clear-editor');
                        isModified = false;
                    }
                },
                {
                    label: 'Open',
                    click: async () => {
                        if (!(await askToSaveIfNeeded())) return;

                        const { canceled, filePaths } = await dialog.showOpenDialog({
                            properties: ['openFile'],
                            filters: [{ name: 'Text Files', extensions: ['txt'] }]
                        });

                        if (!canceled) {
                            currentFilePath = filePaths[0];
                            const content = fs.readFileSync(currentFilePath, 'utf-8');
                            mainWindow.webContents.send('load-file', content);
                            isModified = false;
                        }
                    }
                },
                {
                    label: 'Save',
                    click: async () => {
                        await saveFile();
                    }
                },
                { type: 'separator' },
                {
                    label: 'Exit',
                    click: async () => {
                        if (await askToSaveIfNeeded()) app.quit();
                    }
                }
            ]
        }
    ];

    Menu.setApplicationMenu(Menu.buildFromTemplate(template));
});
