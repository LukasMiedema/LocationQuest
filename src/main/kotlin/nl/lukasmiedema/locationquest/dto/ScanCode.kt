package nl.lukasmiedema.locationquest.dto

/**
 * After a code has been scanned (which is a GET due to QR code limitations) the browser is redirected back
 * to the dashboard. The state of how the scanning went is stored in the ScanStateHolder.
 */
enum class ScanCode {

	/**
	 * The quest can be claimed.
	 */
	CLAIMABLE,

	/**
	 * The quest cannot be claimed because some items are missing in the inventory of the team.
	 */
	UNSATISFIED_DEPENDENCY,

	/**
	 * The quest cannot be claimed because it already has been claimed.
	 */
	DUPLICATE,

	/**
	 * The quest cannot be claimed at this time because it's not the next logical quest.
	 */
	INELIGIBLE
}