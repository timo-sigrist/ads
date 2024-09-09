package ch.zhaw.ads;

public class HilbertServer implements CommandExecutor {

    private Turtle turtle;

    public HilbertServer() {
        this.turtle = new Turtle();
    }

    @Override
    public String execute(String command) {
        int depth = Integer.parseInt(command);
        double dist = 0.8 / (Math.pow(2, depth + 1) -1);
        this.turtle = new Turtle(0.1, 0.1);
        hilbert(depth, dist, -90);
        return turtle.getTrace();
    }

    private void hilbert(int depth, double dist, double angle) {
        if (depth > 0) {
            depth--;
            turtle.turn(-angle);
            hilbert(depth, dist /2, -angle);
            turtle.move(dist);
            turtle.turn(angle);
            hilbert(depth, dist /2, angle);
            turtle.move(dist);
            hilbert(depth, dist /2, angle);
            turtle.turn(angle);
            turtle.move(dist);
            hilbert(depth, dist /2, -angle);
            turtle.turn(-angle);
        } else {
            turtle.turn(-angle);
            turtle.move(dist);
            turtle.turn(angle);
            turtle.move(dist);
            turtle.turn(angle);
            turtle.move(dist);
            turtle.turn(-angle);
        }
    }
}