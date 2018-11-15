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

/**
 * Make the answer buttons clickable
 */
(function() {
	var INPUT = "#questAnswer";
	var ITEM = ".answer-item";
	var NAME_ATTRIBUTE = "data-index";
	var SELECTED_CLASS = "primary";
	var ADD_CLASS_TO = ".button";

	$(document).ready(function () {
		var items = $(ITEM);
		var input = $(INPUT);
		var currOption = $(ITEM + "." + SELECTED_CLASS);

		items.click(function(e) {
			var $this = $(e.currentTarget);
			var $btn = $this.find(ADD_CLASS_TO);
			var name = $this.attr(NAME_ATTRIBUTE);

			currOption.removeClass(SELECTED_CLASS);
			$btn.addClass(SELECTED_CLASS);
			currOption = $btn;

			input.val(name)
		});
	});
})();
