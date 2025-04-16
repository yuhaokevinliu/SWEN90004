/**

 * @author 1286084 Yuhao Liu
 * @date 30 March 2025
 */

public class Station extends Thread implements Location{

    private Cart cart = null;

    private int id;
    public Station(int i) {
        this.id = i;
    }

    private Boolean empty = true;

    public Boolean isEmpty(){return this.empty;}

    private boolean holdGem;


    public synchronized void putGem() {
        while(this.holdGem){
            try {
                wait(); // wait for the cart to pick up the gem
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.holdGem = true;
        //System.out.println("Miner" + id + "PutGem");
        notifyAll();
    }

    private void loadCart(){
        this.cart.gems +=1;
        this.holdGem =false;
        System.out.println(this.cart + "loaded with a gem");
    }


    public synchronized void receive(Cart c){
        while(!this.empty) {
            try {
                wait(); // wait for the cart arrive
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.cart = c;
        this.empty = false;
        System.out.println(this.cart + "delivered to station " + id);
        notifyAll();

    }


    public synchronized Cart send(){
        while(!this.holdGem || this.empty){  //no gem or no cart
            try {
                wait(); // wait for the cart to pick up the gem
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        this.loadCart();
        Cart result = this.cart;
        this.cart = null;
        this.empty = true;
        System.out.println(result + " departed from station." + id);
        notifyAll();
        return result;
    }


}
