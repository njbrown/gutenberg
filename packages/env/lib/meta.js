/**
 * External dependencies
 */
const path = require( 'path' );
const fs = require( 'fs' ).promises;

/**
 * Options for meta parsing.
 *
 * @typedef WPEnvMetaOptions
 * @property {string}  workDirectoryPath Path to the work directory located in ~/.wp-env.
 */

const META_FILE_NAME = 'wp-env-meta.json';

/**
 * This function can be used to compare a possible new meta value against an
 * existing meta value. For example, we can use this to check if the configuration
 * has changed in a new run of wp-env start.
 *
 * @param {string}           key     A unique identifier for the meta.
 * @param {any}              value   The value to check against the existing meta.
 * @param {WPEnvMetaOptions} options Parsing options
 *
 * @return {boolean} If true, the value is different from the meta which exists.
 */
async function didMetaChange( key, value, options ) {
	const existingValue = await getMeta( key, options );
	return value !== existingValue;
}

/**
 * This function persists the given meta value to the meta file. It creates the
 * file if it does not exist yet, and overwrites the existing meta value for the
 * given key if it already exists.
 *
 * @param {string}           key     A unique identifier for the meta.
 * @param {any}              value   The value to persist.
 * @param {WPEnvMetaOptions} options Parsing options
 */
async function setMeta( key, value, options ) {
	const existingMeta = getMetaFile( options );
	existingMeta[ key ] = value;

	await fs.writeFile(
		getPathToMetaFile( options.workDirectoryPath ),
		JSON.stringify( existingMeta )
	);
}

/**
 * This function retrieves the meta associated with the given key from the file.
 * Returns undefined if the key does not exist or if the meta file has not been
 * created yet.
 *
 * @param {string}           key     The unique identifier for the meta value.
 * @param {WPEnvMetaOptions} options Parsing options
 *
 * @return {any?} The meta value. Undefined if it has not been set or if the meta
 *                file has not been created.
 */
async function getMeta( key, options ) {
	const meta = await getMetaFile( options );
	return meta[ key ];
}

/**
 * Returns the data stored in the meta file as a JS object. Instead of throwing
 * an error, simply returns an empty object if the file cannot be retrieved.
 *
 * @param {WPEnvMetaOptions} options Parsing options
 *
 * @return {Object} The data from the meta file. Empty if the file does not exist.
 */
async function getMetaFile( { workDirectoryPath } ) {
	const filename = getPathToMetaFile( workDirectoryPath );
	try {
		const rawMeta = await fs.readFile( filename );
		return JSON.parse( rawMeta );
	} catch {
		return {};
	}
}

function getPathToMetaFile( workDirectoryPath ) {
	return path.resolve( workDirectoryPath, META_FILE_NAME );
}

module.exports = { didMetaChange, setMeta };
