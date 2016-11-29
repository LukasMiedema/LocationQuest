var gulp = require("gulp");
var webpack = require("gulp-webpack");

gulp.task("package", function() {
	gulp.src(["src/main/web/**"])
		.pipe(webpack({
			output: {
				filename: "dist.js"
			},
			module: {
				loaders: [
					{ test: /\.ts$/, loader: "ts-loader" }
				]
			}
		}))
		.pipe(gulp.dest("target/generated-resources/static/assets"));
});
