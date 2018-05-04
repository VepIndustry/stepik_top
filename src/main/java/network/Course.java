package network;

public class Course implements Comparable<Course> {

    private long id;
    private String title;
    private int learners_count;

    Course(long id, String title, int learners_count) {
        this.id = id;
        this.title = title;
        this.learners_count = learners_count;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", learners_count=" + learners_count +
                '}';
    }

    @Override
    public int compareTo(Course o) {
        return Integer.compare(o.learners_count, this.learners_count);
    }
}
