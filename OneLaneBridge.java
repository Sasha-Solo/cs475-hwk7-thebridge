
public class OneLaneBridge extends Bridge{

    /**
     * Global vars
     */
    private Object entry = new Object(); //CV for entering 
    private Object exit = new Object(); //CV for exiting 
    private int bridgeLim; 

    public OneLaneBridge(int bridgeLim){
        this.bridgeLim = bridgeLim; 
    }

    /**
     * Called by a car when the car wants to enter the bridge.
     * @param car being run on the bridge
     */
    @Override
    public void arrive(Car car) throws InterruptedException{
        synchronized(entry){
            //System.out.println("Car called enter:");// + car.toString());
            boolean carDir = car.getDirection(); //get incoming car's direction
            //System.out.println("Car direction:" + carDir);
            if (bridge.size() == 0){
                direction = carDir; //incoming car decides direction of bridge
                car.setEntryTime(currentTime); //set time
                synchronized(this){
                    bridge.add(car); //add car
                    System.out.println("Bridge (dir=" + direction + "): " + car.toString());
                }
                currentTime++;
            }
            else{
                while (direction != car.getDirection() || bridge.size() >= bridgeLim){
                    entry.wait();
                    if (bridge.size() == 0){
                        direction = car.getDirection();
                    }
                } 
                car.setEntryTime(currentTime); //set time
                synchronized(this){
                    bridge.add(car); //add car
                }
                if (bridge.size() == 0){
                    synchronized(this){
                        System.out.println("Bridge (dir=" + direction + "): []");
                    }
                }
                else{
                    synchronized(this){
                        System.out.print("Bridge (dir=" + direction + "):");
                        System.out.println(bridge);
                    }
                }
                currentTime++;
            }
        }
    }
    
    /**
     * Called by a car when it wants to exit the bridge.
     * @param car being run on the bridge
     */
    @Override
    public void exit(Car car) throws InterruptedException{
        synchronized(exit){
            while(!car.equals(bridge.get(0))){ //if you aren't at the front, wait
                exit.wait();
            }
            if (car.equals(bridge.get(0))){ //if the front car wants to leave, it can
                synchronized(this){
                    bridge.remove(car); //remove car
                }
                if (bridge.size() == 0){
                    synchronized(this){
                        System.out.println("Bridge (dir=" + direction + "): []");
                    }
                }
                else{
                    synchronized(this){
                        System.out.print("Bridge (dir=" + direction + "):");
                        System.out.println(bridge);
                    }
                }
                exit.notifyAll(); //notify other cars waiting to leave
            }
            synchronized(entry){
                 if (bridge.size() < bridgeLim){
                    entry.notifyAll(); //cars can enter if bridge has not reached capacity
                }   
            }
        }
    }
}