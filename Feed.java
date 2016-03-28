class Feed {
	int feedId;
	int feedQuantity;
	String feedDate;
	protected Feed(int feedId, int feedQuantity, String feedDate) {
		super();
		this.feedId = feedId;
		this.feedQuantity = feedQuantity;
		this.feedDate = feedDate;
	}
	protected int getFeedId() {
		return feedId;
	}
	protected void setFeedId(int feedId) {
		this.feedId = feedId;
	}
	protected int getFeedQuantity() {
		return feedQuantity;
	}
	protected void setFeedQuantity(int feedQuantity) {
		this.feedQuantity = feedQuantity;
	}
	protected String getFeedDate() {
		return feedDate;
	}
	protected void setFeedDate(String feedDate) {
		this.feedDate = feedDate;
	}
	
}
