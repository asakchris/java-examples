#### Thread timeout
There are 2 things needs to be done:
1. Wait for defined timeout interval
2. Stop the thread
##### Wait for defined timeout
- This can be achieved by: 
  - Thread.sleep() method
  - Future.get() method with timeout
##### Stopping a thread
- We don't have stop method in Thread class
- ExecutorService.shutdown()
  - No new tasks accepted
  - Previously submitted tasks are executed
- ExecutorService.shutdownNow()
  - No new tasks accepted
  - Previously submitted tasks waiting in the queue are returned
  - Tasks being run by the thread(s) are **attempted** to stop
- Future.cancel() method does not guarantee that it's going to stop the task, it will only attempt to stop the thread
- Java threads cannot be killed. They are cooperative. We need to ask politely.
- Two ways to stop thread
  - Interrupts
    - Task itself needs to listen for the interrupts (Thread.currentThread().isInterrupted()), so within the task we need a condition to check thread is interrupted or not
    - Concept of Interrupt is used in every concurrent utility in an attempt to stop the thread
    - ExecutorService.shutdownNow() method invokes interrupt method on all running threads in the thread pool
    - Future.cancel()  method invokes interrupt method on current task
  - Volatile
    - Add volatile instance variable in Thread instance and run the thread only if the value is true
  - AtomicBoolean
    - Add AtomicBoolean instance variable in Thread instance and run the thread only if the value is true
- **Important**
  - Interrupt & Volatile works only if the task is performing any Java operation. If doesn't work if task is performing IO operation.