package lilypad.server.connect.query;

public class QueryLookupService<T> {

	@SuppressWarnings("unchecked")
	private Query<T>[] queries = new Query[256];
	
	public void submit(Query<T> query) {
		this.queries[query.getId()] = query;
	}
	
	public Query<T> getById(int id) {
		return this.queries[id];
	}
	
}
