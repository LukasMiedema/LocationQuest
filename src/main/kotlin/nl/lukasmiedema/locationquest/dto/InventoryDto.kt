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
	 * Checks if the inventory has the items in the other inventory.
	 */
	fun has(other: InventoryDto): Boolean {
		return other.items.all { this[it.collectible.collectibleId] >= it.count }
	}

	/**
	 * An item in the inventory
	 */
	data class InventoryItem(val collectible: Collectible, val count: Int)

}