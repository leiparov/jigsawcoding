package models.services.ideone;
public class IdeoneSubmissionDetails {
	private String error;
	private Integer langId;
	private String langName;
	private String langVersion;
	private String date;
	private Float time;
	private Integer status;
	private Integer result;
	private Integer memory;
	private Integer signal;
	private Boolean isPublic;
	private String source;
	private String input;
	private String output;
	private String stderr;
	private String cmpinfo;
	private String link;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Integer getLangId() {
		return langId;
	}
	public void setLangId(Integer langId) {
		this.langId = langId;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	public String getLangVersion() {
		return langVersion;
	}
	public void setLangVersion(String langVersion) {
		this.langVersion = langVersion;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Float getTime() {
		return time;
	}
	public void setTime(Float time) {
		this.time = time;
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
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getSignal() {
		return signal;
	}
	public void setSignal(Integer signal) {
		this.signal = signal;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getStderr() {
		return stderr;
	}
	public void setStderr(String stderr) {
		this.stderr = stderr;
	}
	public String getCmpinfo() {
		return cmpinfo;
	}
	public void setCmpinfo(String cmpinfo) {
		this.cmpinfo = cmpinfo;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public String toString() {
		return "IdeoneSubmissionDetails ["
				+ (error != null ? "error=" + error + ", " : "")
				+ (langId != null ? "langId=" + langId + ", " : "")
				+ (langName != null ? "langName=" + langName + ", " : "")
				+ (langVersion != null
						? "langVersion=" + langVersion + ", "
						: "") + (date != null ? "date=" + date + ", " : "")
				+ (time != null ? "time=" + time + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (result != null ? "result=" + result + ", " : "")
				+ (memory != null ? "memory=" + memory + ", " : "")
				+ (signal != null ? "signal=" + signal + ", " : "")
				+ (isPublic != null ? "isPublic=" + isPublic + ", " : "")
				+ (source != null ? "source=" + source + ", " : "")
				+ (input != null ? "input=" + input + ", " : "")
				+ (output != null ? "output=" + output + ", " : "")
				+ (stderr != null ? "stderr=" + stderr + ", " : "")
				+ (cmpinfo != null ? "cmpinfo=" + cmpinfo : "") + "]";
	}

}