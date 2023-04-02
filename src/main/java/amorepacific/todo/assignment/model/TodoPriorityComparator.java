package amorepacific.todo.assignment.model;

import amorepacific.todo.assignment.entity.Todo;

import java.util.Comparator;


public class TodoPriorityComparator implements Comparator<Todo> {
    @Override
    public int compare(Todo o1, Todo o2) {
        //priority - 중요도, 순서 순으로 우선순위 오름차 정렬
        if (o1.getPriority().getImportance() > o2.getPriority().getImportance()) {
            return 1;
        } else if (o1.getPriority().getImportance() < o2.getPriority().getImportance()) {
            return -1;
        } else {
            if (o1.getPriority().getOrder() > o2.getPriority().getOrder()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
