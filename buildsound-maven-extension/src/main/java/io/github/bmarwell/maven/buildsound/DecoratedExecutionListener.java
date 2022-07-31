package io.github.bmarwell.maven.buildsound;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.List;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionListener;

public class DecoratedExecutionListener implements ExecutionListener {

  private final List<ExecutionListener> executionListeners;

  public DecoratedExecutionListener(List<ExecutionListener> executionListeners) {
    this.executionListeners = unmodifiableList(executionListeners);
  }

  public DecoratedExecutionListener(ExecutionListener... listeners) {
    this.executionListeners = unmodifiableList(Arrays.asList(listeners));
  }

  @Override
  public void projectDiscoveryStarted(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.projectDiscoveryStarted(event));
  }

  @Override
  public void sessionStarted(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.sessionStarted(event));
  }

  @Override
  public void sessionEnded(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.sessionEnded(event));
  }

  @Override
  public void projectSkipped(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.projectSkipped(event));
  }

  @Override
  public void projectStarted(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.projectStarted(event));
  }

  @Override
  public void projectSucceeded(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.projectSucceeded(event));
  }

  @Override
  public void projectFailed(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.projectFailed(event));
  }

  @Override
  public void mojoSkipped(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.mojoSkipped(event));
  }

  @Override
  public void mojoStarted(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.mojoStarted(event));
  }

  @Override
  public void mojoSucceeded(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.mojoSucceeded(event));
  }

  @Override
  public void mojoFailed(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.mojoFailed(event));

  }

  @Override
  public void forkStarted(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.forkStarted(event));
  }

  @Override
  public void forkSucceeded(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.forkSucceeded(event));
  }

  @Override
  public void forkFailed(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.forkFailed(event));
  }

  @Override
  public void forkedProjectStarted(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.forkedProjectStarted(event));
  }

  @Override
  public void forkedProjectSucceeded(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.forkedProjectSucceeded(event));
  }

  @Override
  public void forkedProjectFailed(ExecutionEvent event) {
    executionListeners.forEach(executionListener -> executionListener.forkedProjectFailed(event));
  }
}
