package ch.zhaw.ads;

public class SnowflakeServer implements CommandExecutor {

    private Turtle turtle;
    private double dist = 0.7;

    @Override
    public String execute(String command) {
        this.turtle = new Turtle(0.1, 0.8);
        int iteration = Integer.parseInt(command);
        snowflake(iteration, this.dist);
        turtle.turn(-120);
        snowflake(iteration, this.dist);
        turtle.turn(-120);
        snowflake(iteration, this.dist);
        return turtle.getTrace();

    }

    private void snowflake(int stufe, double dist) {
        if (stufe > 0) {
            dist = dist / 3;
            stufe --;
            snowflake(stufe,dist);
            turtle.turn(60);
            snowflake(stufe,dist);
            turtle.turn(-120);
            snowflake(stufe,dist);
            turtle.turn(60);
            snowflake(stufe,dist);
        } else {
            turtle.move(dist);
        }
    }

}