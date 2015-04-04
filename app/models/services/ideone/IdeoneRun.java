package models.services.ideone;

import java.util.concurrent.Callable;

public class IdeoneRun implements Callable<String> {

	final static int STATE_DONE = 0;
	final static int STATE_RUNNING = 1;

	protected int mState;
	protected int status = 0;
	protected String source = "";
	protected String input = "";
	protected int lang = 1; // C++
	
	
	public IdeoneRun(String source, String input) {
		this.source = source;
		this.input = input;
	}
	public IdeoneRun(String source, String input, Integer languageId) {
		this.source = source;
		this.input = input;
		this.lang = languageId;
	}

	@Override
	public String call() {
		mState = STATE_RUNNING;

		IdeoneService ideoneService = new IdeoneService();

		String link = ideoneService.createSubmission(source, lang, input, true,
				false);

		while (mState == STATE_RUNNING) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			IdeoneSubmissionStatus iss = null;
			iss = ideoneService.getSubmissionStatus(link);
			status = iss.getStatus();
			if (status == 0) {
				this.mState = STATE_DONE;
			}
		}
		
//		IdeoneSubmissionDetails details = ideoneService.getSubmissionDetails(
//				link, true, true, true, true, true);
//		
//		resultado = Json.newObject();
//		resultado.put("status", IdeoneService.translateStatus(details.getStatus()));
//		resultado.put("result", IdeoneService.translateResult(details.getResult()));
//		resultado.put("input", details.getInput());
//		resultado.put("output", details.getOutput());
//		resultado.put("date", details.getDate());
//		resultado.put("time", details.getTime());
//		resultado.put("memory", details.getMemory());
//		resultado.put("error", details.getError());
//		resultado.put("link", link);
//		resultado.put("cmpinfo", details.getCmpinfo());
		
		return link;
		
	}

}
