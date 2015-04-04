package models.services.ideone;

import java.io.IOException;
import java.util.Hashtable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class IdeoneService {

	private static final String URL = "http://ideone.com/api/1/service";

	private String user = "jigsawcoding";
	private String pass = "jigsawcoding";

	private HttpTransportSE createTransport() {
		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.debug = false;
		return transport;
	}

	private SoapObject createSoapObject(String method) {
		return new SoapObject(URL, method);
	}

	private Hashtable<String, Object> getData(SoapObject so) {
		Hashtable<String, Object> data = new Hashtable<>();
		int count = so.getPropertyCount();
		for (int i = 0; i < count; i++) {
			SoapObject item = (SoapObject) so.getProperty(i);
			String key = (String) item.getProperty(0);
			Object val = item.getProperty(1);
			data.put(key, val);
		}
		return data;
	}

	public String testFunction() throws Exception {

		Hashtable<String, Object> data;
		// transport
		HttpTransportSE transport = createTransport();
		// soap objeect
		SoapObject request = createSoapObject("testFunction");
		request.addProperty("user", user);
		request.addProperty("pass", pass);

		// serialización
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);

		// request
		transport.call("testFunction", envelope);
		SoapObject so = (SoapObject) envelope.getResponse();
		data = getData(so);
		return data.toString();
	}

	public String createSubmission(String sourceCode, Integer language,
			String input, Boolean run, Boolean priv) {
		try {
			Hashtable<String, Object> data = new Hashtable<>();
			// transport
			HttpTransportSE transport = createTransport();
			// soap object
			SoapObject request = createSoapObject("createSubmission");
			request.addProperty("user", user);
			request.addProperty("pass", pass);
			request.addProperty("sourceCode", sourceCode);
			request.addProperty("language", language);
			request.addProperty("input", input);
			request.addProperty("run", run);
			request.addProperty("private", priv);

			// serializacja
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);

			// request
			transport.call("createSubmission", envelope);
			SoapObject so = (SoapObject) envelope.getResponse();

			data = getData(so);
			return data.get("link").toString();
		} catch (IOException e) {
			System.out.println("CREATE SUBMISSION IOException");
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			System.out.println("CREATE SUBMISSION XmlPullParserException");
			e.printStackTrace();
		}catch (Exception e) {
			
			System.out.println("CREATE SUBMISSION Exception");
			e.printStackTrace();
		}
		return "";
	}
	
	public IdeoneSubmissionStatus getSubmissionStatus(String link){		
		IdeoneSubmissionStatus ret = null;
		Hashtable<String, Object> data;
		try {		

			HttpTransportSE transport = createTransport();
			SoapObject request = createSoapObject("getSubmissionStatus");
			request.addProperty("user", user);
			request.addProperty("pass", pass);
			request.addProperty("link", link);

			// serializacja
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);

			// request
			transport.call("getSubmissionStatus", envelope);
			SoapObject so = (SoapObject) envelope.getResponse();

			data = getData(so);
			ret =  new IdeoneSubmissionStatus();
			ret.setError(data.get("error").toString());
			ret.setResult((Integer)data.get("result"));
			ret.setStatus((Integer)data.get("status"));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			System.out.println("ISS XML Error");
		}
		return ret;
		
	}

	public IdeoneSubmissionDetails getSubmissionDetails(String link,
			Boolean withSource, Boolean withInput, Boolean withOutput,
			Boolean withStderr, Boolean withCmpinfo) {		
		Hashtable<String, Object> data = new Hashtable<>();

		try {
			HttpTransportSE transport = createTransport();
			SoapObject request = createSoapObject("getSubmissionDetails");
			request.addProperty("user", user);
			request.addProperty("pass", pass);
			request.addProperty("link", link);
			request.addProperty("withSource", withSource);
			request.addProperty("withInput", withInput);
			request.addProperty("withOutput", withOutput);
			request.addProperty("withStderr", withStderr);
			request.addProperty("withCmpinfo", withCmpinfo);

			// serializacja
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);

			// request
			transport.call("getSubmissionDetails", envelope);
			SoapObject so = (SoapObject) envelope.getResponse();

			data = getData(so);
			
			IdeoneSubmissionDetails ret = new IdeoneSubmissionDetails();
			ret.setError(data.get("error").toString());
			ret.setLangId((Integer) data.get("langId"));
			ret.setLangName(data.get("langName").toString());
			ret.setLangVersion(data.get("langVersion").toString());
			ret.setTime( Float.valueOf(((SoapPrimitive) data.get("time")).toString()));
			ret.setDate(data.get("date").toString());
			ret.setStatus((Integer)data.get("status"));
			ret.setResult((Integer)data.get("result"));
			ret.setMemory((Integer)data.get("memory"));
			ret.setSignal((Integer)data.get("signal"));
			ret.setIsPublic((Boolean) data.get("public"));
			ret.setLink(link);

			if (withSource.booleanValue()) {
				ret.setSource((String) data.get("source"));
			}
			if (withInput.booleanValue()) {
				ret.setInput((String) data.get("input"));
			}
			if (withOutput.booleanValue()) {
				ret.setOutput((String) data.get("output"));
			}
			if (withStderr.booleanValue()) {
				ret.setStderr((String) data.get("stderr"));
			}
			if (withCmpinfo.booleanValue()) {
				ret.setCmpinfo((String) data.get("cmpinfo"));
			}
			return ret;
		} catch (IOException ex) {
			System.out.println("IO Error");
		} catch (NumberFormatException ex) {
			System.out.println("Number Format Error");
		} catch (Exception ex) {
			System.out.println("Error");
		}

		return null;
	}

	public Hashtable<Integer, Object> getLanguages() {

		Hashtable<Integer, Object> ret = new Hashtable<>();
		Hashtable<String, Object> data = new Hashtable<>();

		try {
			HttpTransportSE transport = createTransport();
			transport.debug = true;
			SoapObject request = createSoapObject("getLanguages");
			request.addProperty("user", user);
			request.addProperty("pass", pass);

			// serialización
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);

			// request
			transport.call("getLanguages", envelope);
			SoapObject so = (SoapObject) envelope.getResponse();

			int count = so.getPropertyCount();
			for (int i = 0; i < count; ++i) {
				SoapObject item = (SoapObject) so.getProperty(i);
				String key = (String) item.getProperty(0);
				Object val = item.getProperty(1);
				data.put(key, val);
			}

			String error = (String) data.get("error");
			if (!error.equals("OK")) {
				System.out.println("Error occurred: " + error);
				return null;
			}

			SoapObject languages = (SoapObject) data.get("languages");
			count = languages.getPropertyCount();
			for (int i = 0; i < count; ++i) {
				SoapObject item = (SoapObject) languages.getProperty(i);
				Integer key = (Integer) item.getProperty(0);
				String val = (String) item.getProperty(1);
				ret.put(key, val);
			}

		} catch (IOException ex) {
			System.out.println("IO Error" + "\n" + ex.getMessage());
		} catch (NumberFormatException ex) {
			System.out.println("Number Format Error");
		} catch (Exception ex) {
			System.out.println("Error");
		}

		return ret;
	}

	public static String translateStatus(int status) {
		if (status < 0) {
			return "waiting for compilation";
		} else if (status == 0) {
			return "done";
		} else if (status == 1) {
			return "compilation";
		} else if (status == 3) {
			return "running";
		}
		return "unknown status";
	}

	public static String translateState(int state) {
		if (state < 0) {
			return "waiting for compilation";
		}
		if (state == 0) {
			return "done";
		}
		if (state == 1) {
			return "compilation";
		}
		// if( state == 3 ){
		return "running";
	}

	public static String translateResult(int result) {
		switch (result) {
			case 0 :
				return "not running";
			case 11 :
				return "compilation error";
			case 12 :
				return "runtime error";
			case 13 :
				return "time limit exceeded";
			case 15 :
				return "success";
			case 17 :
				return "memory limit exceeded";
			case 19 :
				return "illegal system call";
			case 20 :
				return "internal error";
		}
		return "unknown result";
	}
	
	

	
}
