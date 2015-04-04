package models.services.ideone;

public class IdeoneMain {
	public static void main(String args[]) {
		IdeoneService ideone = new IdeoneService();
		try {
//			System.out.println(ideone.testFunction());
			System.out.println(ideone.getLanguages());
//			System.out.println(ideone.getLanguages().get(1));
//			System.out.println(ideone.getLanguages().get(55));
//			String source = "#include <iostream>\n "
//					+ "using namespace std;\n "
//					+ "int main() {\n "
//					+ "cout<<\"Hola Leibnitz\";\n"
//					+ "return 0;\n"
//					+ "}";
//			System.out.println(ideone.createSubmission(source, 1, "", true, false));
			String link = "hACTit";
//			String link = "";
//			System.out.println(ideone.getSubmissionStatus(link));
//			IdeoneSubmissionDetails isd = ideone.getSubmissionDetails(link, true, true, true, true, true);
			System.out.println(ideone.getSubmissionDetails(link, true, true, true, true, true));
//			System.out.println(isd.getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
