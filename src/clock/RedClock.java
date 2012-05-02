package clock;



import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This sample is an animated stopwatch. Click the green button to start the
 * stopwatch and click the red button to stop it.
 *
 * @see javafx.scene.effect.DropShadow
 * @see javafx.scene.effect.GaussianBlur
 * @see javafx.scene.effect.Light
 * @see javafx.scene.effect.Lighting
 * @see javafx.scene.image.Image
 * @see javafx.scene.image.ImageView
 * @see javafx.scene.shape.Circle
 * @see javafx.scene.Group
 * @see javafx.scene.shape.Ellipse
 * @see javafx.scene.shape.Rectangle
 * @see javafx.scene.text.Font
 * @see javafx.scene.text.Text
 * @see javafx.scene.text.TextAlignment
 * @see javafx.scene.text.TextBoundsType
 * @see javafx.scene.transform.Rotate
 * @see javafx.util.Duration
 *
 */
public class RedClock extends Application {

    private Watch watch;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        watch = new Watch();
        myLayout();
        root.getChildren().add(watch);
        watch.time.play();
    }

    private void myLayout() {
        watch.setLayoutX(15);
        watch.setLayoutY(10);
    }

    private class Watch extends Parent {
        //visual nodes

        private final Dial mainDial;
        private final Group background = new Group();
        private Timeline time = new Timeline();

        public Watch() {
            mainDial = new Dial(117, true, 12, 60, Color.RED, true);
            configureBackground();
            myLayout();
            configureTimeline();
            getChildren().addAll(background, mainDial);
        }

        private void configureTimeline() {
            time.setCycleCount(Timeline.INDEFINITE);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(47), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    calculate();
                }
            });
            time.getKeyFrames().add(keyFrame);
        }

        private void configureBackground() {
         
            Circle circle2 = new Circle();
            circle2.setCenterX(140);
            circle2.setCenterY(140);
            circle2.setRadius(128);
            circle2.setFill(Color.TRANSPARENT);
            circle2.setStroke(Color.web("#0A0A0A"));
            circle2.setStrokeWidth(0.9);

            Circle circle3 = new Circle();
            circle3.setCenterX(140);
            circle3.setCenterY(140);
            circle3.setRadius(140);
            circle3.setFill(Color.TRANSPARENT);
            
            Stop[] stops = new Stop[]{ new Stop(0, Color.DARKRED),new Stop(1, Color.RED) ,new Stop(2, Color.GRAY) ,new Stop(3, Color.LIGHTGRAY) , new Stop(4, Color.BLACK)};
            LinearGradient lg = new LinearGradient(0, 140, 0, 0, false, CycleMethod.NO_CYCLE, stops);
            circle3.setStroke(lg);
            circle3.setStrokeWidth(20);
            

            Ellipse ellipse = new Ellipse(140, 95, 180, 95);
            Circle ellipseClip = new Circle(140, 140, 140);
            ellipse.setFill(Color.web("#535450"));
            ellipse.setStrokeWidth(0);
            GaussianBlur ellipseEffect = new GaussianBlur();
            ellipseEffect.setRadius(10);
            ellipse.setEffect(ellipseEffect);
            ellipse.setOpacity(0.1);
            ellipse.setClip(ellipseClip);
             
            background.getChildren().addAll(circle2, circle3, ellipse);
            
        }

        private void myLayout() {
            mainDial.setLayoutX(140);
            mainDial.setLayoutY(140);
        }

        private final Calendar calendar = Calendar.getInstance();

        private void calculate() {
            calendar.setTimeInMillis(System.currentTimeMillis());
            int seconds = calendar.get(Calendar.SECOND);
            int mins = calendar.get(Calendar.MINUTE);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            refreshTimeDisplay(mins, seconds, hours);
        }

        private void refreshTimeDisplay(int mins, int seconds, int hours) {
            double handAngle = ((360 / 60) * seconds);
            double minutesHandAngle = ((360 / 60.0) * mins);
            double hoursAngle = ((360/12)*hours);
            mainDial.setAngle(handAngle,minutesHandAngle,hoursAngle);
        }

       
    }

    private class Dial extends Parent {

        private final double radius;
        private final Color color;
        private final Color FILL_COLOR = Color.web("#0A0A0A");
        private final Font NUMBER_FONT = new Font("Arial Black", 13);
        private final Text name = new Text();
        private final Group hand = new Group();
        private final Group minsHand = new Group();
        private final Group hoursHand = new Group();
        
        private final Group handEffectGroup = new Group(hand);
        private final DropShadow handEffect = new DropShadow();
        
        private final Group minsEffectGroup = new Group(minsHand);
        private final Group hoursEffectGroup = new Group(hoursHand);
        
        private int numOfMarks;
        private int numOfMinorMarks;

        public Dial(double radius, boolean hasNumbers, int numOfMarks, int numOfMinorMarks, Color color, boolean hasEffect) {
            this.color = color;
            this.radius = radius;
            this.numOfMarks = numOfMarks;
            this.numOfMinorMarks = numOfMinorMarks;

            configureHand();
            configureMinsHand();
            configureHoursHand();
            if (hasEffect) {
                configureEffect();
            }
            if (hasNumbers) {
                getChildren().add(createNumbers());
            }
            getChildren().addAll(
                    createTickMarks(),
                    handEffectGroup,minsEffectGroup,hoursEffectGroup);
        }

        public Dial(double radius, boolean hasNumbers, int numOfMarks, int numOfMinorMarks, String name, Color color, boolean hasEffect) {
            this(radius, hasNumbers, numOfMarks, numOfMinorMarks, color, hasEffect);
            configureName(name);
            getChildren().add(this.name);
        }

        private Group createTickMarks() {
            Group group = new Group();

            for (int i = 0; i < numOfMarks; i++) {
                double angle = (360 / numOfMarks) * (i);
                group.getChildren().add(createTic(angle, 2, 1.0));
            }

            for (int i = 0; i < numOfMinorMarks; i++) {
                double angle = (360 / numOfMinorMarks) * i;
                group.getChildren().add(createTic(angle, 1, 0.8));
            }
            return group;
        }

        private Circle createTic(double angle, double width, double height) {
            Rectangle rectangle = new Rectangle(-width / 2, -height / 2, width, height);
            rectangle.setFill(Color.BLACK);
            rectangle.setRotate(angle);
            rectangle.setEffect(new Lighting());
            
            Circle circle = new Circle(width);
            rectangle.setLayoutX(radius * Math.cos(Math.toRadians(angle)));
            rectangle.setLayoutY(radius * Math.sin(Math.toRadians(angle)));
            
            circle.setLayoutX(radius * Math.cos(Math.toRadians(angle)));
            circle.setLayoutY(radius * Math.sin(Math.toRadians(angle)));
            
            return circle;
        }

        private void configureName(String string) {
            Font font = new Font(9);
            name.setText(string);
            name.setBoundsType(TextBoundsType.VISUAL);
            name.setLayoutX(-name.getBoundsInLocal().getWidth() / 2 + 4.8);
            name.setLayoutY(radius * 1 / 2 + 4);
            name.setFill(FILL_COLOR);
            name.setFont(font);
        }

        private Group createNumbers() {
            Group numbers = new Group();
            for (int i = 1; i <= 12; i++) {
                double angle = (360 / 12) * (i);
                numbers.getChildren().add(createNumber("" + i, angle, 0));
            }
            return numbers;
        }

        private Text createNumber(String number, double angle, double layoutY) {
            Text text = new Text(number);
            if (number.equals("12")) {
                text.setLayoutX(105 * Math.cos(Math.toRadians(angle - 90)) - 7);
                text.setLayoutY(105 * Math.sin(Math.toRadians(angle - 90)) + 7);
            } else {
                text.setLayoutX(105 * Math.cos(Math.toRadians(angle - 90)) - 5);
                text.setLayoutY(105 * Math.sin(Math.toRadians(angle - 90)) + 5);
            }
            text.setTextAlignment(TextAlignment.CENTER);
            text.setFill(FILL_COLOR);
            text.setFont(NUMBER_FONT);
            return text;
        }

        public void setAngle(double secsAngle , double minsAngle, double hoursAngle) {
            Rotate rotate = new Rotate(secsAngle);
            hand.getTransforms().clear();
            hand.getTransforms().add(rotate);
            rotate = new Rotate(minsAngle);
            minsHand.getTransforms().clear();
            minsHand.getTransforms().add(rotate);
            rotate = new Rotate(hoursAngle);
            hoursHand.getTransforms().clear();
            hoursHand.getTransforms().add(rotate);
        }

        private void configureHand() {
            Circle circle = new Circle(0, 0, radius / 18);
            circle.setFill(color);
            Rectangle rectangle1 = new Rectangle(-0.5 - radius / 140, +radius / 7 - radius / 1.08, radius / 70 + 1, radius / 1.08);
            rectangle1.setFill(color);
             Circle circle1 = new Circle(.8);
            circle1.setFill(color);
            circle1.setLayoutY(radius / 7 - radius / 1.08);
            hand.getChildren().addAll(circle, rectangle1,circle1);
        }

        private void configureMinsHand() {
            Circle circle = new Circle(0, 0, radius / 18);
            circle.setFill(color);
            Rectangle rectangle1 = new Rectangle(-0.5 - radius / 140, +radius / 7 - radius / 1.58, radius / 70 + 1, radius / 1.58);
            rectangle1.setFill(color);
            Circle circle1 = new Circle(3);
            circle1.setFill(color);
            circle1.setLayoutY(radius / 7 - radius / 1.58);
            minsHand.getChildren().addAll(circle, circle1,rectangle1);
        }
        
        private void configureHoursHand() {
            Circle circle = new Circle(0, 0, radius / 18);
            circle.setFill(color);
            Rectangle rectangle1 = new Rectangle(-0.5 - radius / 140, +radius / 7 - radius / 2.08, radius / 70 + 1, radius / 2.08);
            rectangle1.setFill(color);
            Circle circle1 = new Circle(3);
            circle1.setFill(color);
            //circle.setLayoutX(2);
            circle1.setLayoutY(radius / 7 - radius / 2.08);
            hoursHand.getChildren().addAll(circle, circle1,rectangle1);
        }
        
        private void configureEffect() {
            handEffect.setOffsetX(radius / 40);
            handEffect.setOffsetY(radius / 40);
            handEffect.setRadius(6);
            handEffect.setColor(Color.web("#000000"));

            Lighting lighting = new Lighting();
            Light.Distant light = new Light.Distant();
            light.setAzimuth(225);
            lighting.setLight(light);
            handEffect.setInput(lighting);
            handEffectGroup.setEffect(handEffect);
            minsEffectGroup.setEffect(handEffect);
            hoursEffectGroup.setEffect(handEffect);
            
            
        }
    }
 

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
