/**

 * @author 1286084 Yuhao Liu
 * @date 30 March 2025
 */

public class Operator extends Thread{
    private Elevator elevator;
    public Operator(Elevator elevator) {
        this.elevator = elevator;
    }

    public void run(){
        while (!this.isInterrupted()) {
            synchronized (elevator) {
                try {
                    elevator.wait(Params.operatorPause());
                } catch (InterruptedException e) {
                    this.interrupt();
                }

                elevator.move();

                try {
                    Thread.sleep(Params.ELEVATOR_TIME);
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        }
    }


}
