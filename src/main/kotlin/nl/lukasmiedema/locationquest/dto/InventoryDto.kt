package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.Collectible

/**
 * @author Lukas Miedema
 */
data class InventoryDto(private val lookupTable: Map<Int, InventoryItem>) {
	constructor(items: List<InventoryItem>) : this(items.associateBy({it.collectible.collectibleId}, { it }))

	val items: Collection<InventoryItem>
		get() = lookupTable.values

	/**
	 * Get the number of the collectible in the items.
	 */
	operator fun get(collectibleId: Int): Int = lookupTable[collectibleId]?.count ?: 0

	/**
	 * Checks if the inventory has the items provided in the map
	 */
	fun has(required: List<QuestCollectibleDto>): Boolean {
		for (c in required) {
			if (this[c.collectible.collectibleId] < c.questCollectible.requires) {
				return false
			}
		}
		return true
	}

	/**
	 * An item in the inventory
	 */
	data class InventoryItem(val collectible: Collectible, val count: Int)

}