public class ThreadsRunnableDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("mainThread=" + Thread.currentThread().getName());
        System.out.println("daemonDefault=" + Thread.currentThread().isDaemon());

        Thread worker = new Thread(() -> {
            System.out.println("workerName=" + Thread.currentThread().getName());
            System.out.println("workerAlive=" + Thread.currentThread().isAlive());
        }, "handbook-worker");

        worker.start();
        worker.join();
        System.out.println("afterJoinAlive=" + worker.isAlive());
    }
}
