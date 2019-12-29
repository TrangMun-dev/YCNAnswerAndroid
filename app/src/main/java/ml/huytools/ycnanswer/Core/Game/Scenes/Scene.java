package ml.huytools.ycnanswer.Core.Game.Scenes;

import android.graphics.Canvas;

import java.util.Comparator;

import ml.huytools.ycnanswer.Core.LinkedListQueue;

public class Scene extends SceneBase {
    private NodeGroup nodeGroup;
    private boolean hasUpdateSort;

    public Scene(){
        nodeGroup = new NodeGroup();
    }

    public void add(Node node){
        nodeGroup.add(node);
    }

    public void remove(Node node){
        nodeGroup.remove(node);
    }

    public int getSizeNode(){
        return nodeGroup.size();
    }

    @Override
    public void OnDraw(Canvas canvas) {
        nodeGroup.draw(canvas);
    }

    @Override
    public boolean OnUpdate(){
        return nodeGroup.update();
    }
}
