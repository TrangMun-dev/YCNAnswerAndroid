package ml.huytools.ycnanswer.Views.GameComponents;

import ml.huytools.ycnanswer.Core.Game.Actions.ActionDelay;
import ml.huytools.ycnanswer.Core.Game.Actions.ActionRepeat;
import ml.huytools.ycnanswer.Core.Game.Actions.ActionRepeatForever;
import ml.huytools.ycnanswer.Core.Game.Actions.ActionSequence;
import ml.huytools.ycnanswer.Core.Game.Actions.ActionTimings.ActionCubicBezier;
import ml.huytools.ycnanswer.Core.Game.Actions.ActionTimings.ActionRotateBy;
import ml.huytools.ycnanswer.Core.Game.Graphics.Drawing.CircleShape;
import ml.huytools.ycnanswer.Core.Game.Scenes.NodeGroup;
import ml.huytools.ycnanswer.Core.Math.Vector2D;

public class Loading extends NodeGroup {
    final int NumCircle = 7;
    Vector2D size;
    CircleShape circle[];

    public Loading(){
        circle = new CircleShape[NumCircle];
        for(int i=0; i<NumCircle; i++){
            circle[i] = new CircleShape();
            circle[i].setColor(200, 255, 255, 255);
            circle[i].setRadius(15);
            circle[i].setOrigin(-50, 0);
            circle[i].runAction(
                    ActionSequence.create(
                            ActionDelay.create((i+1)*120),
                            ActionRepeatForever.create(
                                    ActionCubicBezier.EaseInOut(ActionRotateBy.create(360, 200*NumCircle))
                            )
                    )
            );
            add(circle[i]);
        }
    }

    public void setBoundingSize(Vector2D size){
        this.size = size;

        for(int i=0; i<NumCircle; i++){
            circle[i].setPosition(this.size.div(2));
        }

    }

}
