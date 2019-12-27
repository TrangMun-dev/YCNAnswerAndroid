package ml.huytools.ycnanswer.Core.Game.Actions;

import ml.huytools.ycnanswer.Core.LinkedListQueue;

public class ActionSequence extends Action {
    protected Action[] actions;
    int pos;
    int size;

    private ActionSequence(){
        pos = 0;
    }

    public static ActionSequence create(Action... actions){
        ActionSequence actionSequence = new ActionSequence();
        actionSequence.actions = actions;
        actionSequence.size = actions.length;
        return actionSequence;
    }

    @Override
    protected void OnActionSetup() {
        for(Action action:actions){
            action.setup(node);
        }
    }

    @Override
    protected void OnActionRestart() {
        pos = 0;
        for(Action action:actions){
            action.restart();
        }
    }

    @Override
    protected boolean OnActionUpdate() {
        Action action = actions[pos];
        action.update();
        if(action.isFinish()){
            pos++;

            if(pos >= size){
                setFinish(true);
            }
            else {
                /// restart next
                /// cài đặt đối tượng theo thông tin hiện tại
                actions[pos].restart();
            }
        }
        return false;
    }
}
