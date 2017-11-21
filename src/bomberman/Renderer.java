package bomberman;

import bomberman.animations.Sprite;
import bomberman.entity.player.Player;
import bomberman.scenes.Sandbox;
import bomberman.utils.ImageUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Renderer {

    /**
     *
     * @author CoreyHendrey
     *
     * gc is GraphicsContext on which we draw the sprite time is the current
     * time duration that we need to sync up to, ie the time duration of the
     * tick from the gameloop
     *
     * This works by first finding the top left pixel of the animation that is
     * playing, with spriteLocationOnSheet(X/Y). It then figures out which from
     * we are up to using findCurrentFrame().
     *
     * It then draws the frame at x, y using the height and width.
     *
     * It uses 'actualSize' to find the next sprite, because of padding on the
     * width and height.
     *
     */

    static Image img;

    public static void init() {
        img = ImageUtils.loadImage("src/Resources/img/sprites.png");
    }

    public static void playAnimation(Sprite sprite,Player p) {
        double time = GameLoop.getTickDuration();
        GraphicsContext gc = Sandbox.getGraphicsContext();
        if(sprite.hasValidSpriteImages){
            playAnimation(sprite.spriteImages,sprite.playSpeed,p.positionX,p.positionY,sprite.width,sprite.width);
        }
        else{
            playAnimation(gc, time, sprite.actualSize, sprite.spriteLocationOnSheetX, sprite.spriteLocationOnSheetY, sprite.numberOfFrames, p.positionX,p.positionY, sprite.width, sprite.height, sprite.scale, sprite.resersePlay, sprite.playSpeed);
        }
    }

    public static void playAnimation(Image[] imgs,double speed,int x,int y,int w, int h){
        double time = GameLoop.getTickDuration();
        GraphicsContext gc = Sandbox.getGraphicsContext();
        int numberOfFrames = imgs.length;
        int index = findCurrentFrame(time, numberOfFrames, speed);
        gc.drawImage(img,x,y,w,h);
    }
    
    public static void playAnimation(GraphicsContext gc, double time, int actualSize, int startingPointX, int startingPointY, int numberOfFrames, int x, int y, int width, int height, double scale, boolean reversePlay, double playSpeed) {
        
        double speed = playSpeed >= 0 ? playSpeed : 0.3;
        
        // index reporesents the index of image to be drawn from the set of images representing frames of animation
        int index = findCurrentFrame(time, numberOfFrames, speed);
        
        // newX represents the X coardinate of image in the spritesheet image to be drawn on screen
        int newSpriteSheetX = reversePlay ? startingPointX + index * actualSize : startingPointX;
        // newY represents the X coardinate of image in the spritesheet image to be drawn on screen
        int newSpriteSheetY = reversePlay ? startingPointY : startingPointY + index * actualSize;

        System.out.println("index="+index + " newSpriteSheetX=" + newSpriteSheetX + " newSpriteSheetY=" + newSpriteSheetY + " width=" + width + " height=" + height + " x=" + x + " y=" + y + " width=" + width * scale + " height=" + height * scale);
                    //img,             sx,              sy,     w,     h,  dx, dy,        dw,             dh
        gc.drawImage(img, newSpriteSheetX, newSpriteSheetY, width, height,  x,  y, width * scale, height * scale);
    }

    private static int findCurrentFrame(double time, int totalFrames, double speed) {
        return (int) (time % (totalFrames * speed) / speed);
    }
}
