package nl.lukasmiedema.locationquest.dto

/**
 * Items a quest yields and requires represented as two inventories.
 * @author Lukas Miedema
 */
data class QuestInventoryDto(val yieldsInventory: InventoryDto, val requiresInventory: InventoryDto)