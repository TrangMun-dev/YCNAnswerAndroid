package ml.huytools.ycnanswer.Core.Game.Animate;

import java.util.LinkedHashMap;

import ml.huytools.ycnanswer.Core.Annotation.JsonName;
import ml.huytools.ycnanswer.Core.App;
import ml.huytools.ycnanswer.Core.MVP.Entity;
import ml.huytools.ycnanswer.Core.MVP.EntityManager;
import ml.huytools.ycnanswer.Core.Math.Vector2D;
import ml.huytools.ycnanswer.Core.Resource;

/***
 *
 * Cấu trúc:
 *  {
 *      "image": [
 *          {
 *              "type" : "...",     /// resource|storage
 *              "path" : "..."      /// R.[...].[name] | /a/b.type
 *              "frames" : [
 *  *               {"x": ..., "y": ..., "w": ..., "h": ...},   // frames pos 0
 *                    ....                                        // frames pos n
 *  *               ]
 *          }
 *      ]
 *      ,
 *      "actions" : [
 *          {
 *              "name" : "idle",
 *              "position_frames" : [0, 1, 3, ...]
 *          },
 *          ....    // action different
 *      ]
 *  }
 */
public class AnimationData extends Entity {
    @JsonName(type = JsonName.Type.ModelManager, clazz = Action.class)
    public EntityManager<Action> actions;

    @JsonName(type = JsonName.Type.ModelManager, clazz = Image.class)
    public EntityManager<Image> images;


    public  static AnimationData CreateByResource(int resFileJson){

        /// Tạo Entity  với Resource
        String json = Resource.readRawTextFile(resFileJson);
        AnimationData animationData = Entity.ParseJson(AnimationData.class, json);

        /// Tải resource tương ứng
        animationData.init();

        return animationData;
    }

    public void init(){
        /// Load image
        for(Image image:images){
            image.init();
        }

        /// Init hashmap
        LinkedHashMap<String, Image> imageLinkedHashMap = new LinkedHashMap<>();
        for(Image image:images){
            imageLinkedHashMap.put(image.id, image);
        }

        /// Cập nhật image trên action
        for(Action action:actions){
            for(Action.Frame frame:action.frames){
                frame.image = imageLinkedHashMap.get(frame.imgID).frames.get(frame.framePos).imageCrop;
            }
        }
    }




    public static class Image extends Entity {
        @JsonName
        public String id;

        @JsonName
        public String type; /// resource

        @JsonName
        public String path;

        @JsonName
        public float scaleX = 1.0f;

        @JsonName
        public float scaleY = 1.0f;

        @JsonName(type = JsonName.Type.ModelManager, clazz = Image.Frame.class)
        public EntityManager<Frame> frames;

        ///
        private boolean isLoaded = false;

        private ml.huytools.ycnanswer.Core.Game.Graphics.Image image;

        /// Frames
        public static class Frame extends Entity {
            @JsonName
            public float x;

            @JsonName
            public float y;

            @JsonName
            public float w;

            @JsonName
            public float h;

            ///
            private ml.huytools.ycnanswer.Core.Game.Graphics.Image imageCrop;

            public ml.huytools.ycnanswer.Core.Game.Graphics.Image getImageCrop(){
                return imageCrop;
            }
        }

        private void init(){
            /// init image resource
            switch (type){
                case "resource":
                    int r = Resource.getResourceID(path);
                    image = ml.huytools.ycnanswer.Core.Game.Graphics.Image.LoadByResource(r);
                    if(scaleX != 1.0f || scaleY != 1.0f){
                        ml.huytools.ycnanswer.Core.Game.Graphics.Image imageScale = image.createScale(new Vector2D(scaleX, scaleY));
                        // free image
                        image.free();
                        // swap
                        image = imageScale;
                    }
                    break;
            }

            isLoaded = image!=null && image.getBitmap() != null;
            if(!isLoaded){
                return;
            }

            /// init createCrop image to mini - frames
            for(Image.Frame f:frames){
                float x = App.convertDpToPixel(f.x*scaleX);
                float y = App.convertDpToPixel(f.y*scaleY);
                float w = App.convertDpToPixel(f.w*scaleX);
                float h = App.convertDpToPixel(f.h*scaleY);
                f.imageCrop = image.createCrop(new Vector2D(x, y), new Vector2D(w, h));
            }
        }


        public ml.huytools.ycnanswer.Core.Game.Graphics.Image getImage() {
            return image;
        }
    }

    public static class Action extends Entity {
        @JsonName
        public String name;

        @JsonName
        public int time;

        @JsonName
        public String timing = "Linear";

        @JsonName
        public boolean reverse = false;

        @JsonName
        public boolean infinite = true;

        @JsonName(type = JsonName.Type.ModelManager, clazz = Action.Frame.class)
        public EntityManager<Frame> frames;

        public static class Frame extends Entity {
            @JsonName
            public String imgID;

            @JsonName
            public int framePos;

            ///
            private ml.huytools.ycnanswer.Core.Game.Graphics.Image image;
            public ml.huytools.ycnanswer.Core.Game.Graphics.Image getImage(){
                return image;
            }
        }
    }

}
