package models.services.ideone;
public class IdeoneSubmissionStatus {
	private String error;
	private Integer status;
	private Integer result;
	
	public IdeoneSubmissionStatus() {
		super();
	}
	public IdeoneSubmissionStatus(String error, Integer status, Integer result) {
		super();
		this.error = error;
		this.status = status;
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "IdeoneSubmissionStatus [error=" + error + ", status=" + status
				+ ", result=" + result + "]";
	}
	

}