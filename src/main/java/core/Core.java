package core;

import network.Course;
import network.StepikNetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Core {

    private static final int THREAD_COUNT = 10;

    private StepikNetworkService networkService;
    private CoursesContainer coursesContainer;

    private Runnable worker;

    Core() {
        coursesContainer = new CoursesContainer();
        networkService = new StepikNetworkService();

        worker = () -> {
            while (true) {
                try {
                    List<Course> courses = networkService.loadNextPage();
                    if (courses.size() != 0) {
                        coursesContainer.addCourses(courses);
                    }
                } catch (NoSuchElementException e) {
                    break;
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        };
    }

    void run(int countTopCourses) {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures.add(service.submit(worker));
        }

        while (futures.stream().anyMatch(future -> !future.isDone())) {
            //do nothing
        }

        coursesContainer.printTopCourses(countTopCourses);
        service.shutdown();
    }
}
