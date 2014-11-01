package models.services.ideone;

public class IdeoneMain {
	public static void main(String args[]) {
		IdeoneService ideone = new IdeoneService();
		try {
			System.out.println(ideone.testFunction());
			System.out.println(ideone.getLanguages().get(1));
			System.out.println(ideone.getLanguages().get(55));
//			String source = "#include <iostream>\n "
//					+ "using namespace std;\n "
//					+ "int main() {\n "
//					+ "cout<<\"Hola Leibnitz\";\n"
//					+ "return 0;\n"
//					+ "}";
			//System.out.println(ideone.createSubmission(source, 1, "", true, false));
			String link = "X8EzF3";
			//String link = "ueqZFx";
			System.out.println(ideone.getSubmissionStatus(link));
			System.out.println(ideone.getSubmissionDetails(link, true, true, true, true, true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
