/**
 * Make the button set for team enroll clickable
 */
(function() {
	var ENROLL_INPUT = "#teamEnrollOption";
	var ENROLL_ITEM = ".team-item";
	var NAME_ATTRIBUTE = "data-id";
	var SELECTED_CLASS = "primary";
	var ADD_CLASS_TO = ".button";

	$(document).ready(function () {
		var items = $(ENROLL_ITEM);
		var input = $(ENROLL_INPUT);
		var currOption = $(ENROLL_ITEM + "." + SELECTED_CLASS);

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
