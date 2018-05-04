package core;

import network.Course;

import java.util.*;

public class CoursesContainer {

    private final List<Course> courses;

    CoursesContainer() {
        courses = new ArrayList<>();
    }

    synchronized void addCourse(Course course) {
        courses.add(course);
        System.out.print("\rЗагружено курсов: " + this.courses.size());
    }

    synchronized void addCourses(Collection<Course> courses) {
        this.courses.addAll(courses);
        System.out.print("\rЗагружено курсов: " + this.courses.size());
    }

    synchronized void printTopCourses(int count) {
        System.out.println();
        Collections.sort(courses);
        count = count < courses.size() ? count : courses.size();
        for (int i = 0; i < count; i++) {
            System.out.println(courses.get(i));
        }
    }

}
