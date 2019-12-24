package ml.huytools.ycnanswer.Commons.Graphics.Animations;

import java.util.LinkedHashMap;

import ml.huytools.ycnanswer.Commons.Annotation.JsonName;
import ml.huytools.ycnanswer.Commons.App;
import ml.huytools.ycnanswer.Commons.Math.Vector2D;
import ml.huytools.ycnanswer.Commons.MVP.Model;
import ml.huytools.ycnanswer.Commons.MVP.ModelManager;
import ml.huytools.ycnanswer.Commons.Resource;

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
public class AnimationData extends Model {
    @JsonName(type = JsonName.Type.ModelManager, clazz = Action.class)
    public ModelManager<Action> actions;

    @JsonName(type = JsonName.Type.ModelManager, clazz = Image.class)
    public ModelManager<Image> images;


    public  static AnimationData CreateByResource(int resFileJson){

        /// Tạo Model  với Resource
        String json = Resource.readRawTextFile(resFileJson);
        AnimationData animationData = Model.ParseJson(AnimationData.class, json);

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




    public static class Image extends Model {
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
        public ModelManager<Image.Frame> frames;

        ///
        private boolean isLoaded = false;

        private ml.huytools.ycnanswer.Commons.Graphics.Image image;

        /// Frames
        public static class Frame extends Model {
            @JsonName
            public float x;

            @JsonName
            public float y;

            @JsonName
            public float w;

            @JsonName
            public float h;

            ///
            private ml.huytools.ycnanswer.Commons.Graphics.Image imageCrop;

            public ml.huytools.ycnanswer.Commons.Graphics.Image getImageCrop(){
                return imageCrop;
            }
        }

        private void init(){
            /// init image resource
            switch (type){
                case "resource":
                    int r = Resource.getResourceID(path);
                    image = ml.huytools.ycnanswer.Commons.Graphics.Image.LoadByResource(r);
                    if(scaleX != 1.0f || scaleY != 1.0f){
                        ml.huytools.ycnanswer.Commons.Graphics.Image imageScale = image.scale(new Vector2D(scaleX, scaleY));
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

            /// init crop image to mini - frames
            for(Image.Frame f:frames){
                float x = App.convertDpToPixel(f.x*scaleX);
                float y = App.convertDpToPixel(f.y*scaleY);
                float w = App.convertDpToPixel(f.w*scaleX);
                float h = App.convertDpToPixel(f.h*scaleY);
                f.imageCrop = image.crop(new Vector2D(x, y), new Vector2D(w, h));
            }
        }


        public ml.huytools.ycnanswer.Commons.Graphics.Image getImage() {
            return image;
        }
    }

    public static class Action extends Model {
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
        public ModelManager<Action.Frame> frames;

        public static class Frame extends Model {
            @JsonName
            public String imgID;

            @JsonName
            public int framePos;

            ///
            private ml.huytools.ycnanswer.Commons.Graphics.Image image;
            public ml.huytools.ycnanswer.Commons.Graphics.Image getImage(){
                return image;
            }
        }
    }

}
