public class Admin {
    private String adminId;
    private String name;
    private String contactDetails;

    public Admin(String adminId, String name, String contactDetails) {
        this.adminId = adminId;
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public void viewRequests() {
        System.out.println("Viewing all timetable requests.");
    }

    public void approveTimetable(Timetable timetable) {
        timetable.setApproved(true);
        System.out.println("Timetable approved.");
    }

    public void denyOrApproveRequest(boolean approve) {
        if (approve) {
            System.out.println("Request approved.");
        } else {
            System.out.println("Request denied.");
        }
    }

    public void login() {
        System.out.println("Admin logged in.");
    }
}
