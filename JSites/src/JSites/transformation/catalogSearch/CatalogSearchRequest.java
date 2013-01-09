package JSites.transformation.catalogSearch;

public class CatalogSearchRequest {
	private String attr_query, attr_order, attr_page;

	public String getAttr_query() {
		return attr_query;
	}

	public void setAttr_query(String attr_query) {
		this.attr_query = attr_query;
	}

	public String getAttr_order() {
		return attr_order;
	}

	public void setAttr_order(String attr_order) {
		this.attr_order = attr_order;
	}

	public String getAttr_page() {
		return attr_page;
	}

	public void setAttr_page(String attr_page) {
		this.attr_page = attr_page;
	}

	public CatalogSearchRequest(String attr_query, String attr_order,
			String attr_page) {
		this.attr_order=attr_order;
		this.attr_page=attr_page;
		this.attr_query=attr_query;
	}

}
