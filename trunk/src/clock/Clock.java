/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author beniton
 */
public class Clock extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Calendar calendar = Calendar.getInstance();
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Clock");
        StackPane clock = new StackPane();
        clock.setStyle("-fx-background-color:goldenrod");
        final ImageView dial =  new ImageView(new Image(Clock.class.getResourceAsStream("clockdial.png")));
        final ImageView secs =  new ImageView(new Image(Clock.class.getResourceAsStream("secsgold.png")));
        final ImageView mins =  new ImageView(new Image(Clock.class.getResourceAsStream("minsgold.png")));
        final ImageView hours =  new ImageView(new Image(Clock.class.getResourceAsStream("hoursgold.png")));
        Timeline timer = new Timeline();
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.getKeyFrames().add(new KeyFrame(Duration.seconds(1),new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min  = calendar.get(Calendar.MINUTE);
                int sec  = calendar.get(Calendar.SECOND);
                hours.setRotate(hour*30);
                mins.setRotate(min*6);
                secs.setRotate(sec*6);
             }
        }));
        timer.play();
        clock.getChildren().addAll(dial, secs,mins,hours);
        primaryStage.setScene(new Scene(clock, 378, 378));
        primaryStage.show();
    }
}
