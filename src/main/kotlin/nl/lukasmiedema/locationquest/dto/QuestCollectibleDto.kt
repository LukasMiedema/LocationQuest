package nl.lukasmiedema.locationquest.dto

import nl.lukasmiedema.locationquest.entity.tables.pojos.QuestCollectible
import nl.lukasmiedema.locationquest.entity.tables.pojos.Collectible

/**
 * @author Lukas Miedema
 */
data class QuestCollectibleDto(val questCollectible: QuestCollectible, val collectible: Collectible)