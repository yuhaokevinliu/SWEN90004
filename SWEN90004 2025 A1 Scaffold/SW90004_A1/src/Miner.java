/**

 * @author 1286084 Yuhao Liu
 * @date 30 March 2025
 */

public class Miner extends Thread{

    private Station station;
    public Miner(Station station) {
        this.station = station;
    }

    public void run(){

        while (true) {
            this.station.putGem();

            try {
                Thread.sleep(Params.MINING_TIME);  //simulating mining time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
