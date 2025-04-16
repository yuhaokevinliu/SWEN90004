/**

 * @author 1286084 Yuhao Liu
 * @date 30 March 2025
 */

public class Engine extends Thread{

    private Location from;
    private Location to;


    public Engine(Location location, Location location1) {
        this.from = location;
        this.to = location1;
    }




    public void run() {
        while (true) {
            Cart cart = from.send(); // might block if nothing is there

            // Retry until destination accepts cart
            while (true) {
                synchronized (to) {
                    if (to.isEmpty()) {
                        to.receive(cart);
                        break;
                    }
                }

                try {
                    Thread.sleep(10);  // small delay before retry
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(Params.ENGINE_TIME); // simulate transfer time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
