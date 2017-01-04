
/**
 * Quick-n-dirty script which allows any element to become a "link" by using the clickable class and having a
 * data-href attribute.
 */
$(document).ready(function() {
	$(".clickable").click(function(e) {
		var href = e.currentTarget.getAttribute("data-href")
		if (href) {
			var dest = document.createElement("a")
			dest.href = href;
			dest.click()
		} else {
			console.log("No data-href attribute on " + e.target)
		}
	})
})