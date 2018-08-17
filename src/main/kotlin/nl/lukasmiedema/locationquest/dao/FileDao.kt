package nl.lukasmiedema.locationquest.dao

import nl.lukasmiedema.locationquest.entity.Tables
import nl.lukasmiedema.locationquest.entity.tables.pojos.File
import nl.lukasmiedema.locationquest.exception.ResourceNotFoundException
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Lukas Miedema
 */
@Repository
@Transactional
class FileDao {

	@Autowired private lateinit var sql: DSLContext

	/**
	 * Retrieve the file by the given file id. If the file does not exist null is returned.
	 */
	fun getFile(fileId: UUID): File? = sql
			.selectFrom(Tables.FILE)
			.where(Tables.FILE.FILE_ID.eq(fileId))
			.fetchOneInto(File::class.java)

	/**
	 * List all files in the system.
	 */
	fun getFiles(): List<File> = sql
			.selectFrom(Tables.FILE)
			.fetchInto(File::class.java)

	/**
	 * Update all attributes of a file. Will throw a constraint violation exception if the file was used anywhere
	 * and the primary key changes.
	 */
	fun updateFile(currentId: UUID, newId: UUID, mimeType: String, file: ByteArray) {
		sql
				.update(Tables.FILE)
				.set(Tables.FILE.FILE_ID, newId)
				.set(Tables.FILE.MIME_TYPE, mimeType)
				.set(Tables.FILE.FILE_, file)
				.where(Tables.FILE.FILE_ID.eq(currentId))
				.execute()
	}

	/**
	 * Update metadata attributes of a file. Will throw a constraint violation exception if the file was used anywhere
	 * and the primary key changes.
	 */
	fun updateFile(currentId: UUID, newId: UUID, mimeType: String) {
		sql
				.update(Tables.FILE)
				.set(Tables.FILE.FILE_ID, newId)
				.set(Tables.FILE.MIME_TYPE, mimeType)
				.where(Tables.FILE.FILE_ID.eq(currentId))
				.execute()
	}

	/**
	 * Insert a new file into the database.
	 */
	fun insertFile(uuid: UUID, mimeType: String, file: ByteArray) {
		sql
				.insertInto(Tables.FILE)
				.set(Tables.FILE.FILE_ID, uuid)
				.set(Tables.FILE.MIME_TYPE, mimeType)
				.set(Tables.FILE.FILE_, file)
				.execute()
	}
}
