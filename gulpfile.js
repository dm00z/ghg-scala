const gulp = require('gulp'),
    symdest = require('gulp-symdest'),
    del = require('del'),
    electron = require('gulp-atom-electron'),
    runSequence = require('run-sequence');

const paths = ['assets/**/*', 'mui/**/*', 'bower_components/**/*', 'package.json', 'main.js', 'index.html'];

gulp.task('clean', function() {  
  return del(['app']);
});

gulp.task('package-darwin', function() {
    return gulp.src(paths, {base: "."})
        .pipe(electron({
            version: '2.0.9',
            platform: 'darwin'
        }))
        .pipe(symdest('app/darwin'))
})

gulp.task('package-win32', function() {
    return gulp.src(paths, {base: "."})
        .pipe(electron({
            version: '2.0.9',
            platform: 'win32',
            arch: 'ia32'
        }))
        .pipe(gulp.dest('app/win32-x64'));
})

gulp.task('dist', function(cb) {
    runSequence(
        ['clean'],
        ['package-darwin', 'package-win32'],
        // this callback is executed at the end, if any of the previous tasks errored, 
        // the first param contains the error
        function (err) {
          //if any error happened in the previous tasks, exit with a code > 0
          if (err) {
            var exitCode = 2;
            console.log('[ERROR] gulp build task failed', err);
            console.log('[FAIL] gulp build task failed - exiting with code ' + exitCode);
            return process.exit(exitCode);
          }
          else {
            return cb();
          }
        }            
    );
});

gulp.task('default', ['dist']);