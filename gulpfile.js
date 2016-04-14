/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/9/16)
 */

//Gulp main
var gulp = require("gulp");
var uglify = require("gulp-uglify");
var del = require("del");
var copy = require("gulp-copy");
var concat = require("gulp-concat");
var wiredep = require("gulp-wiredep");
var sourcemaps = require('gulp-sourcemaps');
var git = require('gulp-git');
var sass = require('gulp-sass');
var refresh = require('gulp-refresh');
var markedown = require('gulp-markdown');
var highlight = require('gulp-highlight');

//configs
var paths = {
    main: "src/index.html",
    scripts: [
        'src/js/*.js',
        'src/js/**/*.js'
    ],
    styles: [
        'src/scss/*.scss'
    ],
    parts: [
        'src/scss/**/*.scss'
    ],
    lib: [
        "src/lib/**/*.min.js",
        "src/lib/**/*.min.css"
    ],
    views: [
        "src/views/*.md",
        "src/views/*.html"
    ],
    site: {
        root: "site",
        scripts: "site/js",
        styles: "site/css",
        lib: "site/lib",
        views: "site/views"
    }
};

gulp.task('clean', function (done) {
    del([paths.site.root], done);
});

gulp.task("lib", function () {
    return gulp.src(paths.lib)
        .pipe(copy(paths.site.lib, {prefix: 2}))
        .pipe(refresh())
});

gulp.task("scripts", function () {
    return gulp.src(paths.scripts)
        .pipe(concat('all.min.js'))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.site.scripts))
        .pipe(refresh())
});

gulp.task("index", function () {
    return gulp.src(paths.main)
        .pipe(wiredep({
            fileTypes: {
                html: {
                    replace: {
                        js: function (path) {
                            path = path.replace(/\.js$/, '.min.js').replace(/(\.min)+.js/, '.min.js');
                            return "<script type='application/javascript' src='" + path + "'></script>";
                        }
                    }
                }
            },
            overrides: {
                'angular-ui': {
                    main: ['/build/angular-ui.min.js', '/build/angular-ui.min.css']
                }
            }
        }))
        .pipe(gulp.dest(paths.site.root))
        .pipe(refresh())

});

gulp.task('sass', function () {
    return gulp.src(paths.styles)
        .pipe(sourcemaps.init())
        .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.site.styles))
        .pipe(refresh())
});


gulp.task('git-site', function () {
    return gulp.src(paths.site.root)
        .pipe(git.add());
});

gulp.task('watch', function () {
    refresh.listen({
        port: 13001
    });
    gulp.watch(paths.site.root, ['git-site']);
    gulp.watch(paths.parts, ['sass']);
    gulp.watch(paths.scripts, ['scripts']);
    gulp.watch(paths.lib, ['lib']);
    gulp.watch(paths.main, ['index']);
    gulp.watch(paths.views, ['views']);
});


gulp.task("views", function () {
    return gulp.src(paths.views)
        .pipe(markedown())
        .pipe(highlight())
        .pipe(gulp.dest(paths.site.views))
        .pipe(refresh())
});

gulp.task("default", ["generate", "watch"]);

gulp.task("generate", ["lib", "scripts", "sass", "views", "index", "git-site"]);

gulp.task("regenerate", ["clean", "generate"]);
