var gulp = require('gulp');
var electron = require('gulp-electron');
var packageJson = require('./package.json');

gulp.task('electron', function() {
    gulp.src("")
        .pipe(electron({
            src: ['assets', 'bower_components', 'package.json', 'main.js', 'index.html'],
            packageJson: packageJson,
            release: './release',
            cache: './cache',
            version: 'v0.36.3',
            packaging: false,
            platforms: ['win32-ia32'],
            platformResources: {
                win: {
                    "version-string": packageJson.version,
                    "file-version": packageJson.version,
                    "product-version": packageJson.version,
                    //"icon": 'gulp-electron.ico'
                }
            }
        }))
        .pipe(gulp.dest(""));
});
