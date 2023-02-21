package backend;

public class Tester {

    public static void main(String[] args) {

        // Create a new collection with original data set contained in
        // ./project1/data.csv
        PatientCollection c = new PatientCollection("./src/backend/data.csv");
        System.out.println("New patient collection built from original data set.");
        System.out.println(c);

        // Print Patient contained in collection and patient not in collection
        System.out.println();
        System.out.println("[Print patient 1]\n " + c.getPatient("1"));
        System.out.println("[Print patient 99] [expect \"null\"]\n " + c.getPatient("99"));
        System.out.println();

        // Add patients to collection from "newdata.csv"
        System.out.println("Adding new patients to collection. [expect duplicate error]\n "
                + c.addPatientsFromFile("./src/backend/newdata.csv"));
        System.out.println();
        System.out.println("Updated Patient Collection:\n" + c);

        // Add patients to collection from "newdata2.csv"
        System.out.println("Adding patients from file. [expect line format error and duplicates]\n"
                + c.addPatientsFromFile("./src/backend/newdata2.csv"));

        System.out.println("List of Patient Ids: " + c.getIds());
        System.out.println();
        System.out.println("Removed Patient 30 from collection.\n " + c.removePatient("30"));
        System.out.println();
        System.out.println("List of Patient Ids: " + c.getIds());
        System.out.println();
        System.out.println("[Print patient 30] [expect \"null\"]\n " + c.getPatient("30"));
        System.out.println();
        System.out.println("Try to remove Patient 30 from collection. [expect null]\n " + c.removePatient("30"));
        System.out.println();

        // update a patient's response
        System.out.println("[Print patient 32]\n " + c.getPatient("32"));
        System.out.println("Set patient 32 response to \"DP\".");
        c.setResultForPatient("32", "DP");
        System.out.println("[Print patient 32]\n " + c.getPatient("32"));

        // write patient collection to file
        System.out.println("Write collection to file.");
        c.writeFile();

        // create new patient collection from current patient collection data
        PatientCollection c1 = new PatientCollection();
        System.out.println("Create new patient collection from saved file.\n" + c1);

    }

}