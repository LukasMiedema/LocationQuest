
/**
 * Quick-n-dirty script which allows any element to become a "link" by using the clickable class and having a
 * data-href attribute.
 */
$(document).ready(function() {
	$(".clickable").click(function(e) {
		debugger;
		var href = e.currentTarget.getAttribute("data-href");
		if (href) {
			window.location.href = href;
		} else {
			console.log("No data-href attribute on " + e.target)
		}
	})
});
