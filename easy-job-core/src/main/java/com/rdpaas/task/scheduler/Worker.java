package com.rdpaas.task.scheduler;

import com.rdpaas.task.common.Task;
import com.rdpaas.task.common.TaskDetail;
import com.rdpaas.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class Worker implements Callable<String> {
    private final TaskRepository taskRepository;

    private final Task task;

    private final TaskExecutor taskExecutor;

    /**
     * 正在执行的任务的Future
     */
    private final Map<Long, Future> doingFutures = new HashMap<>();

    @Override
    public String call() {
        log.info("Begin to execute task:{}", task.getId());
        TaskDetail detail = null;

        try {
            detail = taskRepository.start(task);

            if (detail == null)
                return null;

            task.getInvokor().invoke();
            taskExecutor.finish(task, detail);
            log.info("finished execute task:{}", task.getId());

            doingFutures.remove(task.getId()); // 执行完后删了
        } catch (Exception e) {
            log.error("execute task:{} error,cause by:{}", task.getId(), e);
            try {
                taskRepository.fail(task, detail, e.getCause().getMessage());
            } catch (Exception e1) {
                log.error("fail task:{} error,cause by:{}", task.getId(), e);
            }
        }

        return null;
    }
}
