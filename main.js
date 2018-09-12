const electron = require('electron'),
    app = electron.app,  // Module to control application life.
    Menu = electron.Menu,
    BrowserWindow = electron.BrowserWindow;

const { autoUpdater } = require("electron-updater");

function zoom(win, i) {
    if (!win) return;

    var level = i == 0 ? 0 : i == 1 ? 'webFrame.getZoomLevel() + 1' : 'webFrame.getZoomLevel() - 1';
    var js = 'if(typeof webFrame === "undefined") webFrame = require("electron").webFrame;' +
        'webFrame.setZoomLevel(' + level + ');';
    win.webContents.executeJavaScript(js);
}

const template = [
    {
        label: 'View',
        submenu: [
            {
                label: 'Print',
                accelerator: 'CmdOrCtrl+P',
                click: function(item, focusedWindow) {
                    if (focusedWindow)
                        mainWindow.webContents.print();
                }
            },
            {
                label: 'Reload',
                accelerator: 'CmdOrCtrl+R',
                click: function(item, focusedWindow) {
                    if (focusedWindow)
                        focusedWindow.reload();
                }
            },
            {
                label: 'Zoom in',
                accelerator: 'CmdOrCtrl+=',
                click: (item, focusedWindow) => zoom(focusedWindow, 1)
            },
            {
                label: 'Zoom out',
                accelerator: 'CmdOrCtrl+-',
                click: (item, focusedWindow) => zoom(focusedWindow, -1)
            },
            {
                label: 'Reset zoom level',
                accelerator: 'CmdOrCtrl+0',
                click: (item, focusedWindow) => zoom(focusedWindow, 0)
            },
            {
                label: 'Toggle Full Screen',
                accelerator: (function() {
                    if (process.platform == 'darwin')
                        return 'Ctrl+Command+F';
                    else
                        return 'F11';
                })(),
                click: function(item, focusedWindow) {
                    if (focusedWindow)
                        focusedWindow.setFullScreen(!focusedWindow.isFullScreen());
                }
            },
            {
                label: 'Toggle Developer Tools',
                accelerator: (function() {
                    if (process.platform == 'darwin')
                        return 'Alt+Command+I';
                    else
                        return 'Ctrl+Shift+I';
                })(),
                click: function(item, focusedWindow) {
                    if (focusedWindow)
                        focusedWindow.toggleDevTools();
                }
            },
            {
                label: 'Check for update',
                accelerator: (function() {
                    if (process.platform == 'darwin')
                        return 'Alt+Command+u';
                    else
                        return 'Ctrl+Shift+U';
                })(),
                click: function(item, focusedWindow) {
                    if (focusedWindow)
                        autoUpdater.checkForUpdatesAndNotify();
                }
            }
        ]
    },
    {
        label: 'Help',
        role: 'help',
        submenu: [
            {
                label: 'Learn More',
                click: function() { require('electron').shell.openExternal('http://electron.atom.io') }
            }
        ]
    }
];

if (process.platform === 'darwin') {
    template[0].submenu.push(    
        {
            type: 'separator'
        },
        {
            label: 'Quit',
            accelerator: 'CmdOrCtrl+Q',
            click: function() {
                app.quit();
            }
        }
    )
}

// Report crashes to our server.
// require('crash-reporter').start();

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is GCed.
var mainWindow = null;

// Quit when all windows are closed.
app.on('window-all-closed', function() {
    // On OS X it is common for applications and their menu bar
    // to stay active until the user quits explicitly with Cmd + Q
    if (process.platform != 'darwin') {
        app.quit();
    }
});

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
app.on('ready', function() {
    mainWindow = new BrowserWindow();
    mainWindow.loadURL('file://' + __dirname + '/index.html');
    mainWindow.maximize();

    const menu = Menu.buildFromTemplate(template);
    Menu.setApplicationMenu(menu);

    // Emitted when the window is closed.
    mainWindow.on('closed', function() {
        // Dereference the window object, usually you would store windows
        // in an array if your app supports multi windows, this is the time
        // when you should delete the corresponding element.
        mainWindow = null;
    });
});
