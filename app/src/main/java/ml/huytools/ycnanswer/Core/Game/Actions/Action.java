package ml.huytools.ycnanswer.Core.Game.Actions;

import ml.huytools.ycnanswer.Core.Game.Scene;

public abstract class Action {
    protected Scene.Node node;
    protected boolean finish;

    protected Action(){
        finish = false;
    }

    protected abstract void OnActionSetup();
    protected abstract void OnActionRestart();
    protected abstract boolean OnActionUpdate();

    public void setup(Scene.Node node){
        this.node = node;
        OnActionSetup();
        restart();
    }

    public void restart(){
        finish = false;
        OnActionRestart();
    }

    public boolean update(){
        if(finish){
            return false;
        }
        return OnActionUpdate();
    }

    public boolean isFinish() {
        return finish;
    }

    protected void setFinish(boolean finish) {
        this.finish = finish;
    }

    protected long getTimeCurrent(){
        return System.currentTimeMillis();
    }
}
