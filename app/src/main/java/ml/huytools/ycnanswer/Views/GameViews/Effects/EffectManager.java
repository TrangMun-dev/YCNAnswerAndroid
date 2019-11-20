package ml.huytools.ycnanswer.Views.GameViews.Effects;

import android.graphics.Canvas;

import java.util.Collection;
import java.util.LinkedList;

import ml.huytools.ycnanswer.Views.GameViews.IRender;

public class EffectManager implements IRender {

    LinkedList<Effect> effects;

    public EffectManager(){
        effects = new LinkedList<>();
    }

    public void add(Effect effect){
        effects.add(effect);
    }

    public void removeAll(){
        effects.clear();
    }

    public void remove(Effect effect){
        effects.remove(effect);
    }

    @Override
    public void update(int sleep){
        LinkedList<Effect> effectRM = new LinkedList<>();
        for(Effect effect:effects){
            effect.update(sleep);
            if(effect.canRemove()){
                effectRM.add(effect);
            }
        }

        for(Effect effect:effectRM){
            effects.remove(effect);
        }
    }

    @Override
    public void draw(Canvas canvas){
        for(Effect effect:effects){
            effect.draw(canvas);
        }
    }
}
