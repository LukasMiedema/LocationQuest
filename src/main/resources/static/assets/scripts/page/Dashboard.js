/**
 * Make the items on the history tab expandable
 */
(function() {
	var ITEM = ".expandable";
	var COLLAPSED_CLASS = "collapsed";

	$(document).ready(function() {
		var items = $(ITEM);

		console.log(items);

		items.click(function(e) {
			console.log($(this));
			$(this).toggleClass(COLLAPSED_CLASS);
		});
	});
})();