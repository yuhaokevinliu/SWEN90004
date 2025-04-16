/**

 * @author 1286084 Yuhao Liu
 * @date 30 March 2025
 */

public class Elevator extends Thread implements Location{

    //elevator position
    private Cart cart = null;

    private int cartCount = 0;

    private Boolean maxOut(){return this.cartCount >= Params.STATIONS;}
    private boolean atTop = true;

    private boolean empty = true;   //if cart in elevator

    private boolean waitDown = false; // carts waiting to go down
    private boolean waitUp = false;   // carts waiting to go up
    private Cart parkingSlotUp = null;
    private Cart parkingSlotDown = null;

    public Boolean isEmpty(){return this.empty;}


    //receive check for the parking slot down
    @Override
    public synchronized void receive(Cart c) {
        //this.waitDown = true;
        while(this.waitDown){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.waitDown = true;
        this.parkingSlotDown = c;
        System.out.println(c + " Delivered to Elevator");
        notifyAll();
    }


    @Override
    public synchronized Cart send() {
        //no cart or cart has gem means it's going upward
        while(empty || holdGem()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Cart result = this.cart;
        this.cart = null;
        this.empty = true;
        System.out.println(result + " collected from elevator");
        notifyAll();
        return result;
    }

    //in case not to depart just loaded cart
    //also use for send cart to stations
    private boolean holdGem(){
        if (this.cart == null){
            return false;
        }

        if (this.cart.gems >0){
            return true;
        }
        return false;
    }

    public synchronized void arrive(Cart c){
        while(waitUp  || maxOut()) {  //wait if there is already one waiting
            try {

                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //waitUp = true;
        }
        cartCount++;
        waitUp = true;
        parkingSlotUp = c;
        System.out.println(c.toString() + " arrives at the elevator");
        notifyAll();
    }

    // have cart, at top, cart has gem
    public synchronized Cart depart(){
        while(this.empty || !this.atTop || !holdGem()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        cartCount--;
        Cart temp = this.cart;
        this.cart = null;
        this.empty = true;
        notifyAll();
        return temp;
    }

    private void moveUp(){
        if (this.parkingSlotDown != null) { //see if going to grab a cart up
            this.cart = this.parkingSlotDown;
            this.parkingSlotDown = null;
            this.waitDown = false;
            this.empty = false;
            System.out.println("Elevator ascends with  " + cart);
        }
        else{
            System.out.println("Elevator ascends (empty) ");
        }
        this.atTop = true;
        notifyAll();
    }

    private void moveDown(){
        if(this.parkingSlotUp != null) {
            this.cart = this.parkingSlotUp;
            this.parkingSlotUp = null;
            this.waitUp = false;
            this.empty = false;

            System.out.println("Elevator descends with  " + cart);
        }
        else{
            System.out.println("Elevator descends (empty) ");
        }
        this.atTop = false;
        notifyAll();


    }

    public synchronized void move(){

//        if (!this.empty) {
//            System.out.println("Elevator move skipped (not empty)");
//            return;
//        }

        //deliver a cart || travel to pick up
        if (atTop && this.empty && waitUp && parkingSlotUp != null) {
            moveDown();  // cart waiting to go down
        }

        else if (!atTop && this.empty && waitDown && parkingSlotDown != null) {
            moveUp();  // cart waiting to go up

        } else if (this.empty) {
                if (atTop) {

                    moveDown();
                } else {

                    moveUp();
                }

            }
        //System.out.println("Elevator status → atTop: " + atTop + ", empty: " + empty + ", waitUp: " + waitUp + ", waitDown: " + waitDown);

        notifyAll(); // inform other threads elevator moved
        }

}
