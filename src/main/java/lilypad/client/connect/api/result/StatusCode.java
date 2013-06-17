package lilypad.client.connect.api.result;

public enum StatusCode {

	/**
	 * Failure of any reason relative to what the result.
	 * entitles.
	 */
	INVALID_GENERIC,
	/**
	 * Failure due to the fact of the player not having
	 * the required role to make the request.
	 */
	INVALID_ROLE,
	/**
	 * Success.
	 */
	SUCCESS;
}
