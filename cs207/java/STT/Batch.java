public class Batch {
    private String nameOfBatch;
    private String[] subjectList;
    private String teacherAssigned;
    private int year;
    private String branch;

    // Updated constructor
    public Batch(String nameOfBatch, String[] subjectList, int year, String branch) {
        this.nameOfBatch = nameOfBatch;
        this.subjectList = subjectList;
        this.year = year;
        this.branch = branch;
    }

    public void setSubjectList(String[] subjectList) {
        this.subjectList = subjectList;
    }

    public void assignTeacher(String teacherAssigned) {
        this.teacherAssigned = teacherAssigned;
    }

    public String getNameOfBatch() {
        return nameOfBatch;
    }

    public String[] getSubjectList() {
        return subjectList;
    }

    public String getTeacherAssigned() {
        return teacherAssigned;
    }

    public int getYear() {
        return year;
    }

    public String getBranch() {
        return branch;
    }
}
